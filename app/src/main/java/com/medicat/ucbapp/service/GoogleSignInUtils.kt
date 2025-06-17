package com.medicat.ucbapp.service

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.credentials.CredentialManager
import androidx.credentials.CredentialOption
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.GetCredentialInterruptedException
import androidx.credentials.exceptions.GetCredentialProviderConfigurationException
import androidx.credentials.exceptions.GetCredentialUnsupportedException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.medicat.ucbapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

class GoogleSignInUtils {
    companion object {
        private const val TAG = "GoogleSignInUtils"

        fun doGoogleSignIn(
            context: Context,
            scope: CoroutineScope,
            launcher: ManagedActivityResultLauncher<Intent, ActivityResult>?,
            login: () -> Unit,
        ) {
            val credentialManager = CredentialManager.create(context)
            val request =
                GetCredentialRequest
                    .Builder()
                    .addCredentialOption(getCredentialOptions(context))
                    .build()

            scope.launch {
                try {
                    Log.d(TAG, "Iniciando proceso de autenticación con Google")
                    val result = credentialManager.getCredential(context, request)

                    Log.d(TAG, "Credencial obtenida, tipo: ${result.credential.type}")

                    when (result.credential) {
                        is CustomCredential -> {
                            if (result.credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                                try {
                                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
                                    val googleTokenId = googleIdTokenCredential.idToken

                                    Log.d(TAG, "Token ID obtenido, procediendo con Firebase Auth")

                                    // Verificar que Firebase esté inicializado antes de proceder
                                    if (!isFirebaseInitialized()) {
                                        Log.e(TAG, "Firebase no está inicializado")
                                        throw Exception("Firebase no está disponible")
                                    }

                                    val authCredential = GoogleAuthProvider.getCredential(googleTokenId, null)

                                    // Agregar timeout para evitar cuelgues indefinidos
                                    val authResult =
                                        withTimeout(30000) {
                                            // 30 segundos timeout
                                            Firebase.auth.signInWithCredential(authCredential).await()
                                        }

                                    val user = authResult.user

                                    Log.d(TAG, "Usuario autenticado: ${user?.email}, isAnonymous: ${user?.isAnonymous}")

                                    user?.let {
                                        if (!it.isAnonymous && !it.email.isNullOrEmpty()) {
                                            Log.d(TAG, "Login exitoso, ejecutando callback")
                                            // Ejecutar en el hilo principal si es necesario
                                            withContext(Dispatchers.Main) {
                                                login.invoke()
                                            }
                                        } else {
                                            Log.w(TAG, "Usuario anónimo o sin email")
                                            throw Exception("Usuario no válido")
                                        }
                                    } ?: throw Exception("Usuario nulo después de autenticación")
                                } catch (e: Exception) {
                                    Log.e(TAG, "Error procesando credencial de Google", e)
                                    // Limpiar estado de Firebase si hay error
                                    try {
                                        Firebase.auth.signOut()
                                    } catch (signOutException: Exception) {
                                        Log.e(TAG, "Error al hacer sign out", signOutException)
                                    }
                                    throw e
                                }
                            } else {
                                Log.w(TAG, "Tipo de credencial no soportado: ${result.credential.type}")
                                throw Exception("Tipo de credencial no válido")
                            }
                        }
                        else -> {
                            Log.w(TAG, "Credencial no es CustomCredential")
                            throw Exception("Tipo de credencial no reconocido")
                        }
                    }
                } catch (e: NoCredentialException) {
                    Log.d(TAG, "No hay credenciales disponibles, lanzando intent para seleccionar cuenta")
                    try {
                        launcher?.launch(getIntent())
                    } catch (intentException: Exception) {
                        Log.e(TAG, "Error lanzando intent de selección de cuenta", intentException)
                    }
                } catch (e: GetCredentialException) {
                    Log.e(TAG, "Error obteniendo credenciales", e)
                    handleCredentialException(e)
                } catch (e: TimeoutCancellationException) {
                    Log.e(TAG, "Timeout en autenticación con Firebase", e)
                } catch (e: Exception) {
                    Log.e(TAG, "Error general en Google Sign In", e)
                }
            }
        }

        private fun handleCredentialException(exception: GetCredentialException) {
            when (exception) {
                is GetCredentialCancellationException -> {
                    Log.d(TAG, "Usuario canceló la selección de credenciales")
                }
                is GetCredentialInterruptedException -> {
                    Log.w(TAG, "Proceso de credenciales interrumpido")
                }
                is GetCredentialProviderConfigurationException -> {
                    Log.e(TAG, "Error de configuración del proveedor de credenciales")
                }
                is GetCredentialUnsupportedException -> {
                    Log.e(TAG, "Credenciales no soportadas en este dispositivo")
                }
                else -> {
                    Log.e(TAG, "Error desconocido obteniendo credenciales: ${exception.message}")
                }
            }
        }

        private fun isFirebaseInitialized(): Boolean =
            try {
                Firebase.auth
                true
            } catch (e: Exception) {
                Log.e(TAG, "Firebase no inicializado", e)
                false
            }

        private fun getIntent(): Intent =
            Intent(Settings.ACTION_ADD_ACCOUNT).apply {
                putExtra(Settings.EXTRA_ACCOUNT_TYPES, arrayOf("com.google"))
            }

        private fun getCredentialOptions(context: Context): CredentialOption =
            try {
                GetGoogleIdOption
                    .Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setAutoSelectEnabled(false)
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .build()
            } catch (e: Exception) {
                Log.e(TAG, "Error creando opciones de credencial", e)
                throw e
            }
    }
}
