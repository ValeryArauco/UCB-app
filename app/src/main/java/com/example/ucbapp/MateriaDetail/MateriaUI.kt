package com.example.ucbapp.MateriaDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.domain.Materia

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MateriaUI(
    materia: Materia,
    onBackPressed: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color(0xFFF2F2F2)),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
        ) {
            Text(
                text = materia.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp),
            )

//                when (val ui = materiasState) {
//                    is MateriasViewModel.MateriasUIState.Loading -> {
//                        Box(
//                            contentAlignment = Alignment.Center,
//                            modifier = Modifier.fillMaxSize(),
//                        ) {
//                            CircularProgressIndicator()
//                        }
//                    }
//                    is MateriasViewModel.MateriasUIState.Loaded -> {
//                        LazyColumn(
//                            verticalArrangement = Arrangement.spacedBy(16.dp),
//                        ) {
//                            items(ui.materias) { materia ->
//                                MateriaCard(materia = materia, onClick = { onSuccess(materia) })
//                            }
//                        }
//                    }
//                    is MateriasViewModel.MateriasUIState.Error -> {
//                        val errorMessage = (materiasState as MateriasViewModel.MateriasUIState.Error).message
//                        ErrorView(
//                            message = errorMessage,
//                            onRetry = { },
//                        )
//                    }
//                }
        }
    }
}
