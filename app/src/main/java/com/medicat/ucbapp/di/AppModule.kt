package com.medicat.ucbapp.di

import android.content.Context
import com.medicat.data.ElementoRepository
import com.medicat.data.MateriaRepository
import com.medicat.data.PushNotificationRepository
import com.medicat.data.RecuperatorioRepository
import com.medicat.data.SaberRepository
import com.medicat.data.UpdateRepository
import com.medicat.data.UserRepository
import com.medicat.data.login.ILoginRemoteDataSource
import com.medicat.data.push.IPushDataSource
import com.medicat.data.registrarAvance.IElementoRemoteDataSource
import com.medicat.data.registrarAvance.IMateriaRemoteDataSource
import com.medicat.data.registrarAvance.IRecuperatorioRemoteDataSource
import com.medicat.data.registrarAvance.ISaberRemoteDataSource
import com.medicat.data.registrarAvance.IUpdateRemoteDataSource
import com.medicat.framework.login.LoginRemoteDataSource
import com.medicat.framework.push.FirebaseNotificationDataSource
import com.medicat.framework.registrarAvance.ElementoRemoteDataSource
import com.medicat.framework.registrarAvance.MateriaRemoteDataSource
import com.medicat.framework.registrarAvance.RecuperatorioRemoteDataSource
import com.medicat.framework.registrarAvance.SaberRemoteDataSource
import com.medicat.framework.registrarAvance.UpdateRemoteDataSource
import com.medicat.framework.service.RetrofitBuilder
import com.medicat.usecases.CreateRecuperatorio
import com.medicat.usecases.DeleteRecuperatorio
import com.medicat.usecases.DoLogin
import com.medicat.usecases.GetElementos
import com.medicat.usecases.GetMateria
import com.medicat.usecases.GetMaterias
import com.medicat.usecases.GetRecuperatorios
import com.medicat.usecases.GetSaberes
import com.medicat.usecases.IsUserAllowed
import com.medicat.usecases.ObtainToken
import com.medicat.usecases.UpdateElemento
import com.medicat.usecases.UpdateMateria
import com.medicat.usecases.UpdateRecuperatorio
import com.medicat.usecases.UpdateSaber
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

    @Provides
    @Singleton
    fun provideGetMateria(updateRepository: UpdateRepository): GetMateria = GetMateria(updateRepository)
}
