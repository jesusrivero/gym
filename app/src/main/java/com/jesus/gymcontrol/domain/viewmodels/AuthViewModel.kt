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
import com.jesus.gymcontrol.data.repository.SessionManager
import com.jesus.gymcontrol.domain.usecase.usuario.LoginUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.RecoverPasswordUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.RegisterUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.UpdateRolUseCase
import com.jesus.gymcontrol.presentation.navegation.AppRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
	private val registerUseCase: RegisterUseCase,
	private val loginUseCase: LoginUseCase,
	private val recoverUseCase: RecoverPasswordUseCase,
	private val updateRolUseCase: UpdateRolUseCase,
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
	var rol by mutableStateOf("Dueño")
	var rol2 by mutableStateOf("Administrador")
	var rol3 by mutableStateOf("Cliente")
	var codigo by mutableStateOf("")
	
	fun registerUser(email: String, password: String, name: String, idcard: String) {
		viewModelScope.launch {
			isLoading = true
			errorMessage = null
			isSuccess = false
			
			val result = registerUseCase(name.trim(), email.trim(), password.trim(), idcard.trim())
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
						val userDocSnapshot = FirebaseFirestore.getInstance()
							.collection("users")
							.document(uid)
							.get()
							.await()
						
						val userData = userDocSnapshot.data
						val rol = userData?.get("rol") as? String
						val gymCode = userData?.get("gimnasioCode") as? String
						
						sessionManager.setUserSessionData(
							uid = uid,
							rol = rol ?: "",
							gymCode = gymCode ?: ""
						)
						
						sessionManager.saveRoleState(!rol.isNullOrBlank())
						Log.e("LOGIN", "rol: $rol")
						sessionManager.saveLoginState(true)
						
						
						
						
						navigateBasedOnRole(navController)
						
					} catch (e: Exception) {
						errorMessage = "Error al obtener datos del usuario"
						Log.e("LOGIN", "Firestore Exception", e)
					}
				} else {
					errorMessage = "No se encontró UID"
					Log.e("LOGIN", "UID es null")
				}
			}.onFailure {
				errorMessage = it.message
				Log.e("LOGIN", "Login failed", it)
			}
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
		codigo: String,
		navController: NavController,
	) {
		val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
		
		if (codigo.isBlank()) {
			errorMessage = "Debes ingresar un código"
			return
		}
		
		viewModelScope.launch {
			isLoading = true
			errorMessage = null
			isSuccess = false
			
			val result = updateRolUseCase(uid, rol, codigo)
			isLoading = false
			result.onSuccess {
				isSuccess = true
				
				sessionManager.setUserSessionData(uid, rol, codigo)
				sessionManager.saveLoginState(true)
				sessionManager.saveRoleState(true)
				
				
			}.onFailure {
				errorMessage = it.message
			}
		}
	}
	
	
	fun navigateBasedOnRole(navController: NavController) {
		val rol = sessionManager.getRol()
		
		when (rol?.lowercase()) {
			"cliente" -> {
				navController.navigate(AppRoutes.ClientMainScreen) {
					popUpTo(AppRoutes.StartScreen) { inclusive = true }
				}
			}
			
			"administrador" -> {
				navController.navigate(AppRoutes.MainScreen) {
					popUpTo(AppRoutes.StartScreen) { inclusive = true }
				}
			}
			
			"dueño" -> {
				navController.navigate(AppRoutes.OwnerMainScreen) {
					popUpTo(AppRoutes.StartScreen) { inclusive = true }
				}
			}
			
			else -> {
				navController.navigate(AppRoutes.StartScreen) {
					popUpTo(AppRoutes.StartScreen) { inclusive = true }
				}
			}
		}
	}
	
	
	fun logout() {
		viewModelScope.launch {
			sessionManager.clearSession()
		}
		FirebaseAuth.getInstance().signOut()
	}
	
	//ESTA FUNCION VA A ACTUALIZAR LOS DATOS FALTANTES DEL USUARIO
//    fun updateDatesUser(
//        cedula: String,
//        edad: String,
//        numero: String,
//        sexo: String
//    ) {
//        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
//
//        viewModelScope.launch {
//            isLoading = true
//            errorMessage = null
//            isSuccess = false
//
//            val result: Result<Unit> = updateDatesUserUseCase(uid, cedula, edad, numero, sexo)
//
//            isLoading = false
//            result.onSuccess {
//                isSuccess = true
//            }.onFailure {
//                errorMessage = it.message
//            }
//        }
//    }

}