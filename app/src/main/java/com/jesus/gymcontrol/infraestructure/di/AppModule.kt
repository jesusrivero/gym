package com.jesus.gymcontrol.infraestructure.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jesus.gymcontrol.data.db.AppDatabase
import com.jesus.gymcontrol.data.db.dao.UsuariosDatabaseDao
import com.jesus.gymcontrol.data.repository.AuthRepositoryImpl
import com.jesus.gymcontrol.data.repository.GymRepositoryImpl
import com.jesus.gymcontrol.data.repository.MembershipRepositoryImpl
import com.jesus.gymcontrol.data.repository.PaymentRepositoryImpl
import com.jesus.gymcontrol.data.repository.PromotionRepositoryImpl
import com.jesus.gymcontrol.data.repository.SessionManager
import com.jesus.gymcontrol.data.repository.UserAdminRepositoryImpl
import com.jesus.gymcontrol.data.repository.UserRepositoryImpl
import com.jesus.gymcontrol.data.repository.UsuarioRepositoryIMPL
import com.jesus.gymcontrol.data.repository.notification.NotificacionRepositoryImpl
import com.jesus.gymcontrol.data.repository.report.ReportesRepositoryImpl
import com.jesus.gymcontrol.data.sharedPreferences.PreferencesManager
import com.jesus.gymcontrol.domain.repository.AuthRepository
import com.jesus.gymcontrol.domain.repository.GymRepository
import com.jesus.gymcontrol.domain.repository.MembershipRepository
import com.jesus.gymcontrol.domain.repository.PaymentRepository
import com.jesus.gymcontrol.domain.repository.PromotionRepository
import com.jesus.gymcontrol.domain.repository.UserAdminRepository
import com.jesus.gymcontrol.domain.repository.UserRepository
import com.jesus.gymcontrol.domain.repository.UsuarioRepository
import com.jesus.gymcontrol.domain.repository.notification.NotificacionRepository
import com.jesus.gymcontrol.domain.repository.report.ReportesRepository
import com.jesus.gymcontrol.domain.usecase.usuario.AddPaymentUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.AssignGymToUserUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.CreateGymUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.CreateMembershipUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.CreatePromotionUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.DeleteMembershipUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.DeletePromotionUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.EditMembershipUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.GenerateCodeUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.getDates.GetAllGymUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.getDates.GetAllPaymentsUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.getDates.GetGymUserSummaryUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.getDates.GetMembershipsUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.getDates.GetMembershipsWithUserCountUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.getDates.GetPromotionUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.getDates.GetUserByGymUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.getDates.GetUsersCountByPromotionUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.auth.LoginUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.auth.RegisterUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.UpdateDatesUserUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.UpdatePromotionUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.UpdateRolUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.UpdateUserProfileUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.codes.GetAvailableCodesUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.notification.AddNotificacionUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.notification.DeleteNotificacionUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.notification.GetNotificacionesUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.notification.PurgeNotificacionesUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.password.ChangePasswordUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.report.GenerateClientsReportUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.report.GenerateMembershipsReportUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.report.GeneratePaymentsReportUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.report.GeneratePromotionsReportUseCase
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
		firestore: FirebaseFirestore,
	): AuthRepository {
		return AuthRepositoryImpl(firebaseAuth, firestore)
	}
	
	@Provides
	@Singleton
	fun provideGymRepository(
		firestore: FirebaseFirestore,
	): GymRepository {
		return GymRepositoryImpl(firestore)
	}
	/////////////////////////////////////
	@Provides
	@Singleton
	fun provideUserRepository(
		firestore: FirebaseFirestore,
	): UserRepository {
		return UserRepositoryImpl(firestore, FirebaseAuth.getInstance())
	}
	//////////////////////////////
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
		userRepository: UserRepository,
	): GetGymUserSummaryUseCase {
		return GetGymUserSummaryUseCase(userRepository)
	}
	
	
	@Provides
	fun provideUserAdminRepository(
		firestore: FirebaseFirestore,
		@ApplicationContext context: Context,
	): UserAdminRepository = UserAdminRepositoryImpl(firestore, context)
	
	@Provides
	@Singleton
	fun provideGetUserByGymUseCase(
		userRepository: UserRepository,
	): GetUserByGymUseCase = GetUserByGymUseCase(userRepository)
	
	
	@Provides
	fun provideMembershipRepository(
		firestore: FirebaseFirestore,
		auth: FirebaseAuth,
	): MembershipRepository = MembershipRepositoryImpl(firestore, auth)
	
	@Provides
	fun provideCreateMembershipUseCase(
		repository: MembershipRepository,
	): CreateMembershipUseCase = CreateMembershipUseCase(repository)
	
	@Provides
	fun provideGetMembershipsUseCase(repository: MembershipRepository) =
		GetMembershipsUseCase(repository)
	
	@Provides
	fun provideDeleteMembershipUseCase(repository: MembershipRepository): DeleteMembershipUseCase {
		return DeleteMembershipUseCase(repository)
	}
	
	@Provides
	fun provideGetMembershipsWithUserCountUseCase(
		repository: MembershipRepository,
	): GetMembershipsWithUserCountUseCase {
		return GetMembershipsWithUserCountUseCase(repository)
	}
	
	@Provides
	fun provideEditMembership(
		repository: MembershipRepository,
	): EditMembershipUseCase {
		return EditMembershipUseCase(repository)
	}
	
	@Provides
	fun providePagoRepository(
		firestore: FirebaseFirestore,
	): PaymentRepository = PaymentRepositoryImpl(firestore)
	
	@Provides
	fun provideAddPagoUseCase(
		repository: PaymentRepository,
	): AddPaymentUseCase = AddPaymentUseCase(repository)
	
	
	@Provides
	fun provideGetAllPaymentsUseCase(
		repository: PaymentRepository,
	): GetAllPaymentsUseCase = GetAllPaymentsUseCase(repository)
	
	
	@Provides
	@Singleton
	fun providePromotionRepository(
		firestore: FirebaseFirestore,
		auth: FirebaseAuth,
	): PromotionRepository = PromotionRepositoryImpl(firestore, auth)
	
	
	@Provides
	@Singleton
	fun provideCreatePromotionUseCase(
		repository: PromotionRepository,
	): CreatePromotionUseCase = CreatePromotionUseCase(repository)
	
	
	@Provides
	@Singleton
	fun provideGetPromotionUseCase(
		repository: PromotionRepository,
	): GetPromotionUseCase = GetPromotionUseCase(
		repository
	)
	
	@Provides
	@Singleton
	fun provideUpdatePromotionUseCase(
		repository: PromotionRepository,
	): UpdatePromotionUseCase {
		return UpdatePromotionUseCase(repository)
	}
	
	@Provides
	@Singleton
	fun provideDeletePromotionUseCase(
		repository: PromotionRepository,
	): DeletePromotionUseCase {
		return DeletePromotionUseCase(repository)
	}
	
	@Provides
	@Singleton
	fun provideGetUsersCountByPromotionUseCase(
		repository: PromotionRepository,
	): GetUsersCountByPromotionUseCase {
		return GetUsersCountByPromotionUseCase(repository)
	}
	
	@Provides
	fun provideUpdateUserProfileUseCase(
		userRepository: UserRepository
	): UpdateUserProfileUseCase = UpdateUserProfileUseCase(userRepository)
	
	
	//REPORTES
	@Provides
	fun provideReportesRepository(
		firestore: FirebaseFirestore
	): ReportesRepository = ReportesRepositoryImpl(firestore)
	
	@Provides
	fun provideGeneratePaymentsReportUseCase(
		repository: ReportesRepository
	): GeneratePaymentsReportUseCase = GeneratePaymentsReportUseCase(repository)
	
	@Provides
	fun provideGenerateClientsReportUseCase(
		repository: ReportesRepository
	): GenerateClientsReportUseCase = GenerateClientsReportUseCase(repository)
	
	
	@Provides
	fun provideGenerateMembershipsReportUseCase(
		repository: ReportesRepository
	): GenerateMembershipsReportUseCase = GenerateMembershipsReportUseCase(repository)
	
	
	@Provides
	fun provideGeneratePromotionsReportUseCase(
		repository: ReportesRepository
	): GeneratePromotionsReportUseCase = GeneratePromotionsReportUseCase(repository)
	
	
	@Provides
	fun provideChangePasswordUseCase(authRepository: AuthRepository): ChangePasswordUseCase {
		return ChangePasswordUseCase(authRepository)
	}
	
	@Provides
	@Singleton
	fun provideNotificacionRepository(
		firestore: FirebaseFirestore,
		sessionManager: SessionManager // si tu repo lo necesita
	): NotificacionRepository {
		return NotificacionRepositoryImpl(firestore, sessionManager)
	}
	
	@Provides
	fun provideAddNotificacionUseCase(repository: NotificacionRepository): AddNotificacionUseCase {
		return AddNotificacionUseCase(repository)
	}
	
	@Provides
	fun provideGetNotificacionesUseCase(repository: NotificacionRepository): GetNotificacionesUseCase {
		return GetNotificacionesUseCase(repository)
	}
	
	@Provides
	fun provideDeleteNotificacionUseCase(repository: NotificacionRepository): DeleteNotificacionUseCase {
		return DeleteNotificacionUseCase(repository)
	}
	
	@Provides
	fun providePurgeNotificacionesUseCase(repository: NotificacionRepository): PurgeNotificacionesUseCase {
		return PurgeNotificacionesUseCase(repository)
	}
	
	@Provides
	fun provideGetAvailableCodesUseCase(
		repository: GymRepository
	): GetAvailableCodesUseCase = GetAvailableCodesUseCase(repository)
	
}
