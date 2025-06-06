package com.jesus.gymcontrol.infraestructure.di

import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.jesus.gymcontrol.data.db.AppDatabase
import com.jesus.gymcontrol.data.db.dao.UsuariosDatabaseDao
import com.jesus.gymcontrol.data.repository.AuthRepositoryImpl
import com.jesus.gymcontrol.data.repository.UsuarioRepositoryIMPL
import com.jesus.gymcontrol.data.sharedPreferences.PreferencesManager
import com.jesus.gymcontrol.domain.repository.AuthRepository
import com.jesus.gymcontrol.domain.repository.UsuarioRepository
import com.jesus.gymcontrol.domain.usecase.usuario.LoginUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.RegisterUseCase
import com.jesus.gymcontrol.infraestructure.MyApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideRegisterUseCase(repository: AuthRepository): RegisterUseCase {
        return RegisterUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideLoginUseCase(repository: AuthRepository): LoginUseCase {
        return LoginUseCase(repository)
    }

    @Provides
    fun provideAppDatabase(): AppDatabase {
        return Room.databaseBuilder(
            MyApp.myApp.baseContext,
            AppDatabase::class.java,
            "db_usuarios"
        ).build()
    }

    @Provides
    fun provideUsuariosDao(database: AppDatabase): UsuariosDatabaseDao {
        return database.usuariosDao()
    }

    @Provides
    fun provideUsuarioRepository(dao: UsuariosDatabaseDao): UsuarioRepository {
        return UsuarioRepositoryIMPL(dao)
    }

    @Provides
    fun provideSharedManager(): PreferencesManager {
        return PreferencesManager(MyApp.myApp.baseContext)
    }
}