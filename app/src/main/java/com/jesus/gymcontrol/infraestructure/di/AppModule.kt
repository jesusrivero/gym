package com.jesus.gymcontrol.infraestructure.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jesus.gymcontrol.data.db.AppDatabase
import com.jesus.gymcontrol.data.db.dao.UsuariosDatabaseDao
import com.jesus.gymcontrol.data.repository.AuthRepositoryImpl
import com.jesus.gymcontrol.data.repository.GymRepositoryImpl
import com.jesus.gymcontrol.data.repository.SessionManager
import com.jesus.gymcontrol.data.repository.UserAdminRepositoryImpl
import com.jesus.gymcontrol.data.repository.UserRepositoryImpl
import com.jesus.gymcontrol.data.repository.UsuarioRepositoryIMPL
import com.jesus.gymcontrol.data.sharedPreferences.PreferencesManager
import com.jesus.gymcontrol.domain.repository.AuthRepository
import com.jesus.gymcontrol.domain.repository.GymRepository
import com.jesus.gymcontrol.domain.repository.UserAdminRepository
import com.jesus.gymcontrol.domain.repository.UserRepository
import com.jesus.gymcontrol.domain.repository.UsuarioRepository
import com.jesus.gymcontrol.domain.usecase.usuario.AssignGymToUserUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.CreateGymUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.GenerateCodeUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.GetAllGymUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.GetGymUserSummaryUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.LoginUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.RegisterUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.UpdateDatesUserUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.UpdateRolUseCase
import com.jesus.gymcontrol.infraestructure.MyApp
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
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth, firestore)
    }

    @Provides
    @Singleton
    fun provideGymRepository(
        firestore: FirebaseFirestore
    ): GymRepository {
        return GymRepositoryImpl(firestore)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        firestore: FirebaseFirestore
    ): UserRepository {
        return UserRepositoryImpl(firestore)
    }

    @Provides
    @Singleton
    fun provideCreateGymUseCase(repository: GymRepository): CreateGymUseCase {
        return CreateGymUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetAllGymUseCase(repository: GymRepository): GetAllGymUseCase {
        return GetAllGymUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideAssignGymToUserUseCase(repository: UserRepository): AssignGymToUserUseCase {
        return AssignGymToUserUseCase(repository)
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

    @Provides
    fun provideUpdateUserRolUseCase(repository: AuthRepository): UpdateRolUseCase {
        return UpdateRolUseCase(repository)
    }

    @Provides
    fun provideUpdateDatesUserUseCase(repository: AuthRepository): UpdateDatesUserUseCase {
        return UpdateDatesUserUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSessionManager(@ApplicationContext context: Context): SessionManager {
        return SessionManager(context)
    }

    @Provides
    @Singleton
    fun provideGenerateCodeUseCase(repository: GymRepository): GenerateCodeUseCase {
        return GenerateCodeUseCase(repository)
    }

    @Provides
    fun provideGetGymUserSummaryUseCase(
        userRepository: UserRepository
    ): GetGymUserSummaryUseCase {
        return GetGymUserSummaryUseCase(userRepository)
    }


    @Provides
    fun provideUserAdminRepository(
        firestore: FirebaseFirestore,
        @ApplicationContext context: Context
    ): UserAdminRepository = UserAdminRepositoryImpl(firestore, context)


}
