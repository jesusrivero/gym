package com.jesus.gymcontrol.domain.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.jesus.gymcontrol.data.repository.SessionManager
import com.jesus.gymcontrol.domain.repository.AuthRepository
import com.jesus.gymcontrol.domain.usecase.usuario.UpdateDatesUserUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.UpdateRolUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.auth.CheckidcardExistsUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.auth.LoginUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.auth.RecoverPasswordUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.auth.RegisterUseCase
import com.jesus.gymcontrol.presentation.navegation.AppRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
	private val auth: FirebaseAuth,
	private val firestore: FirebaseFirestore,
	private val registerUseCase: RegisterUseCase,
	private val loginUseCase: LoginUseCase,
	private val recoverUseCase: RecoverPasswordUseCase,
	private val updateRolUseCase: UpdateRolUseCase,
	private val updateDatesUserUseCase: UpdateDatesUserUseCase,
	private val checkidcardExistsUseCase: CheckidcardExistsUseCase,
	private val authRepository: AuthRepository,
	val sessionManager: SessionManager,
) : ViewModel() {
	
	var name by mutableStateOf("")
	var email by mutableStateOf("")
	var password by mutableStateOf("")
	var isLoading by mutableStateOf(false)
	var errorMessage by mutableStateOf<String?>(null)
	var isSuccess by mutableStateOf(false)
	var recoverSuccess by mutableStateOf(false)
	
	var isLoggedInState by mutableStateOf(false)
	var isRoleAssignedState by mutableStateOf(false)
	var sessionLoaded by mutableStateOf(false)
		private set
	
	private val _updateDatesSuccess = MutableStateFlow<Boolean?>(null)
	val updateDatesSuccess: StateFlow<Boolean?> = _updateDatesSuccess
	
	var rol by mutableStateOf("Dueño")
	var rol2 by mutableStateOf("Administrador")
	var rol3 by mutableStateOf("Cliente")
	
	var isUserActive by mutableStateOf<Boolean?>(null)
		private set
	
	
	
	
	fun checkUserStatusOnStart(onResult: (String) -> Unit) {
		val currentUser = auth.currentUser
		if (currentUser == null) {
			onResult("sin_sesion") // o null, como prefieras
			return
		}
		
		firestore.collection("users")
			.document(currentUser.uid)
			.get()
			.addOnSuccessListener { document ->
				val state = document.getString("state") ?: "inactivo"
				isUserActive = (state == "activo")
				onResult(state)  // Devuelve el estado exacto
			}
			.addOnFailureListener {
				isUserActive = false
				onResult("error")
			}
	}

	
	fun registerUser(email: String, password: String, name: String, lastname:String, idcard: String) {
		viewModelScope.launch {
			isLoading = true
			errorMessage = null
			isSuccess = false
			
			val result = registerUseCase(name.trim(),lastname.trim(), email.trim(), password.trim(), idcard.trim())
			isLoading = false
			result.onSuccess {
				isSuccess = true
			}.onFailure {
				errorMessage = it.message
			}
		}
	}
	
	
	fun loginUser(
		email: String,
		password: String,
		navController: NavController,
	) {
		viewModelScope.launch {
			isLoading = true
			errorMessage = null
			isSuccess = false
			
			val result = loginUseCase(email.trim(), password.trim())
			isLoading = false
			
			result.onSuccess {
				val uid = FirebaseAuth.getInstance().currentUser?.uid
				if (uid != null) {
					try {
						val firestore = FirebaseFirestore.getInstance()
						
						val userDocSnapshot = firestore
							.collection("users")
							.document(uid)
							.get()
							.await()
						
						val userData = userDocSnapshot.data
						val rol = userData?.get("rol") as? String
						val gymCode = userData?.get("gimnasioCode") as? String
						val state = userData?.get("state") as? String ?: "inactivo"
						
						// ✅ Guardamos el token FCM
						FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
							if (task.isSuccessful) {
								val token = task.result
								firestore.collection("users").document(uid)
									.update("fcmToken", token)
							}
						}
						
						// ✅ Guardamos sesión local
						sessionManager.setUserSessionData(
							uid = uid,
							rol = rol ?: "",
							gymCode = gymCode ?: ""
						)
						sessionManager.saveRoleState(!rol.isNullOrBlank())
						sessionManager.saveLoginState(true)
						
						// ✅ Verificar estado
						when (state) {
							"activo" -> navigateBasedOnRole(navController, rol)
							"pendiente" -> {
								navController.navigate(AppRoutes.MainScreen) {
									popUpTo(0) { inclusive = true }
								}
							}
							else -> {
								navController.navigate(AppRoutes.InactiveScreen) {
									popUpTo(0) { inclusive = true }
								}
							}
						}
						
					} catch (e: Exception) {
						errorMessage = "Error al obtener datos del usuario"
						Log.e("LOGIN", "Firestore Exception", e)
					}
				} else {
					errorMessage = "Error: UID no encontrado"
				}
			}.onFailure {
				errorMessage = it.localizedMessage ?: "Error al iniciar sesión"
			}
		}
	}
	
	
	fun currentGymCode(): String? {
		return sessionManager.getGymCode()
	}
	
	
	
	
	fun updateDatesUser(
		uid: String,
		idcard: String,
		phone: String,
		name: String,
		lastname: String
	) {
		val gymCode = currentGymCode() ?: return  // 👈 Aquí lo tomas
		
		viewModelScope.launch {
			isLoading = true
			errorMessage = null
			_updateDatesSuccess.value = null
			
			val result = updateDatesUserUseCase(uid, idcard, phone, name, lastname, gymCode)
			
			result.onSuccess {
				_updateDatesSuccess.value = true
			}.onFailure { e ->
				errorMessage = e.message
				_updateDatesSuccess.value = false
			}
			
			isLoading = false
		}
	}
	
	
	
	fun recoverPassword(email: String) {
		viewModelScope.launch {
			isLoading = true
			errorMessage = null
			recoverSuccess = false
			
			val result = recoverUseCase(email.trim())
			isLoading = false
			result.onSuccess {
				recoverSuccess = true
			}.onFailure {
				errorMessage = it.message
			}
		}
	}
	
	fun loadSessionState() {
		isLoggedInState = sessionManager.isLoggedIn()
		isRoleAssignedState = sessionManager.isRoleAssigned()
		sessionLoaded = true
	}
	
	
	fun newDatesUserLogin(
		rol: String,
		code: String,
		navController: NavController,
	) {
		val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
		
		if (code.isBlank()) {
			errorMessage = "Debes ingresar un código"
			return
		}
		
		viewModelScope.launch {
			isLoading = true
			errorMessage = null
			isSuccess = false
			
			val result = updateRolUseCase(uid, rol, code)
			isLoading = false
			result.onSuccess {
				isSuccess = true
				
				sessionManager.setUserSessionData(uid, rol, code)
				sessionManager.saveLoginState(true)
				sessionManager.saveRoleState(true)
				
				
			}.onFailure {
				errorMessage = it.message
			}
		}
	}
	
	
	
	fun navigateBasedOnRole(navController: NavController, rolParam: String? = null) {
		val rol = rolParam ?: sessionManager.getRol()?.lowercase()
		
		when (rol) {
			"cliente" -> {
				navController.navigate(AppRoutes.ClientMainScreen) {
					popUpTo(0) { inclusive = true }
				}
			}
			
			"administrador" -> {
				navController.navigate(AppRoutes.MainScreen) { // mejor nombre
					popUpTo(0) { inclusive = true }
				}
			}
			
			"dueño" -> {
				navController.navigate(AppRoutes.MainScreen) { // mejor nombre
					popUpTo(0) { inclusive = true }
				}
			}
			
			else -> {
				navController.navigate(AppRoutes.SelectedRolScreen) {
					popUpTo(0) { inclusive = true }
				}
			}
		}
	}
	
	
	fun checkGymStatusOnStart(onResult: (String) -> Unit) {
		val gymId = sessionManager.getGymId()
		if (gymId.isNullOrBlank()) {
			onResult("desconocido")
			return
		}
		
		firestore.collection("gimnasios").document(gymId).get()
			.addOnSuccessListener { doc ->
				val state = doc.getString("state") ?: "desconocido"
				onResult(state)
			}
			.addOnFailureListener {
				onResult("desconocido")
			}
	}
	
	fun checkidcardExists(idcard: String, onResult: (exists: Boolean) -> Unit) {
		viewModelScope.launch {
			val exists = checkidcardExistsUseCase(idcard)
			onResult(exists)
		}
	}
	
	fun currentUid(): String? {
		return FirebaseAuth.getInstance().currentUser?.uid
	}
	
	fun logout() {
		viewModelScope.launch {
			sessionManager.clearSession()
		}
		FirebaseAuth.getInstance().signOut()
	}
}