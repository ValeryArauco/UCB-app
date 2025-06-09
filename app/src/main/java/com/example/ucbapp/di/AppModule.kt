package com.example.ucbapp.di

import android.content.Context
import com.example.data.ElementoRepository
import com.example.data.MateriaRepository
import com.example.data.PushNotificationRepository
import com.example.data.RecuperatorioRepository
import com.example.data.SaberRepository
import com.example.data.UpdateRepository
import com.example.data.UserRepository
import com.example.data.login.ILoginRemoteDataSource
import com.example.data.push.IPushDataSource
import com.example.data.registrarAvance.IElementoRemoteDataSource
import com.example.data.registrarAvance.IMateriaRemoteDataSource
import com.example.data.registrarAvance.IRecuperatorioRemoteDataSource
import com.example.data.registrarAvance.ISaberRemoteDataSource
import com.example.data.registrarAvance.IUpdateRemoteDataSource
import com.example.framework.login.LoginRemoteDataSource
import com.example.framework.push.FirebaseNotificationDataSource
import com.example.framework.registrarAvance.ElementoRemoteDataSource
import com.example.framework.registrarAvance.MateriaRemoteDataSource
import com.example.framework.registrarAvance.RecuperatorioRemoteDataSource
import com.example.framework.registrarAvance.SaberRemoteDataSource
import com.example.framework.registrarAvance.UpdateRemoteDataSource
import com.example.framework.service.RetrofitBuilder
import com.example.usecases.CreateRecuperatorio
import com.example.usecases.DeleteRecuperatorio
import com.example.usecases.DoLogin
import com.example.usecases.GetElementos
import com.example.usecases.GetMaterias
import com.example.usecases.GetRecuperatorios
import com.example.usecases.GetSaberes
import com.example.usecases.IsUserAllowed
import com.example.usecases.ObtainToken
import com.example.usecases.UpdateElemento
import com.example.usecases.UpdateMateria
import com.example.usecases.UpdateRecuperatorio
import com.example.usecases.UpdateSaber
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providerRetrofitBuilder(
        @ApplicationContext context: Context,
    ): RetrofitBuilder = RetrofitBuilder(context)

    @Provides
    @Singleton
    fun materiaRemoteDataSource(retrofiService: RetrofitBuilder): IMateriaRemoteDataSource = MateriaRemoteDataSource(retrofiService)

    @Provides
    @Singleton
    fun materiaRepository(remoteDataSource: IMateriaRemoteDataSource): MateriaRepository = MateriaRepository(remoteDataSource)

    @Provides
    @Singleton
    fun provideGetMaterias(materiaRepository: MateriaRepository): GetMaterias = GetMaterias(materiaRepository)

    @Provides
    @Singleton
    fun provideLogin(materiaRepository: MateriaRepository): DoLogin = DoLogin(materiaRepository)

    @Provides
    @Singleton
    fun provideObtainToken(pushNotificationRepository: PushNotificationRepository): ObtainToken = ObtainToken(pushNotificationRepository)

    @Provides
    @Singleton
    fun providePushNotificationRepository(pushDataSource: IPushDataSource): PushNotificationRepository =
        PushNotificationRepository(pushDataSource)

    @Provides
    @Singleton
    fun provideIPushDataSource(): IPushDataSource = FirebaseNotificationDataSource()

    @Provides
    @Singleton
    fun loginRemoteDataSource(retrofiService: RetrofitBuilder): ILoginRemoteDataSource = LoginRemoteDataSource(retrofiService)

    @Provides
    @Singleton
    fun userRepository(remoteDataSource: ILoginRemoteDataSource): UserRepository = UserRepository(remoteDataSource)

    @Provides
    @Singleton
    fun provideUserValidation(userRepository: UserRepository): IsUserAllowed = IsUserAllowed(userRepository)

    @Provides
    @Singleton
    fun elementoRemoteDataSource(retrofiService: RetrofitBuilder): IElementoRemoteDataSource = ElementoRemoteDataSource(retrofiService)

    @Provides
    @Singleton
    fun elementoRepository(remoteDataSource: IElementoRemoteDataSource): ElementoRepository = ElementoRepository(remoteDataSource)

    @Provides
    @Singleton
    fun provideGetElementos(elementoRepository: ElementoRepository): GetElementos = GetElementos(elementoRepository)

    @Provides
    @Singleton
    fun saberRemoteDataSource(retrofiService: RetrofitBuilder): ISaberRemoteDataSource = SaberRemoteDataSource(retrofiService)

    @Provides
    @Singleton
    fun saberRepository(remoteDataSource: ISaberRemoteDataSource): SaberRepository = SaberRepository(remoteDataSource)

    @Provides
    @Singleton
    fun provideGetSabers(saberRepository: SaberRepository): GetSaberes = GetSaberes(saberRepository)

    @Provides
    @Singleton
    fun recuperatorioRemoteDataSource(retrofiService: RetrofitBuilder): IRecuperatorioRemoteDataSource =
        RecuperatorioRemoteDataSource(retrofiService)

    @Provides
    @Singleton
    fun recuperatorioRepository(remoteDataSource: IRecuperatorioRemoteDataSource): RecuperatorioRepository =
        RecuperatorioRepository(remoteDataSource)

    @Provides
    @Singleton
    fun provideGetRecuperatorios(recuperatorioRepository: RecuperatorioRepository): GetRecuperatorios =
        GetRecuperatorios(recuperatorioRepository)

    @Provides
    @Singleton
    fun updateRemoteDataSource(retrofiService: RetrofitBuilder): IUpdateRemoteDataSource = UpdateRemoteDataSource(retrofiService)

    @Provides
    @Singleton
    fun updateRepository(remoteDataSource: IUpdateRemoteDataSource): UpdateRepository = UpdateRepository(remoteDataSource)

    @Provides
    @Singleton
    fun provideUpdateSaber(updateRepository: UpdateRepository): UpdateSaber = UpdateSaber(updateRepository)

    @Provides
    @Singleton
    fun provideUpdateRecuperatorio(updateRepository: UpdateRepository): UpdateRecuperatorio = UpdateRecuperatorio(updateRepository)

    @Provides
    @Singleton
    fun provideCreateRecuperatorio(updateRepository: UpdateRepository): CreateRecuperatorio = CreateRecuperatorio(updateRepository)

    @Provides
    @Singleton
    fun provideDeleteRecuperatorio(updateRepository: UpdateRepository): DeleteRecuperatorio = DeleteRecuperatorio(updateRepository)

    @Provides
    @Singleton
    fun provideUpdateElemento(updateRepository: UpdateRepository): UpdateElemento = UpdateElemento(updateRepository)

    @Provides
    @Singleton
    fun provideUpdateMateria(updateRepository: UpdateRepository): UpdateMateria = UpdateMateria(updateRepository)
}
