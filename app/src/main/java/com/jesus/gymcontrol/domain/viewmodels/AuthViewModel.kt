package com.jesus.gymcontrol.domain.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jesus.gymcontrol.data.repository.SessionManager
import com.jesus.gymcontrol.domain.usecase.usuario.LoginUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.RecoverPasswordUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.RegisterUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.UpdateDatesUserUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.UpdateRolUseCase
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
    private val sessionManager: SessionManager,
//    private val updateDatesUserUseCase: UpdateDatesUserUseCase,
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

    var rol by mutableStateOf("Dueño")
    var rol2 by mutableStateOf("Administrador")
    var rol3 by mutableStateOf("Cliente")
    var codigo by mutableStateOf("")



    fun registerUser(email: String, password: String, name: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            isSuccess = false

            val result = registerUseCase(name.trim(), email.trim(), password.trim())
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
        onLoginSuccess: (hasRole: Boolean) -> Unit
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
                        Log.d("LOGIN", "Documento Firestore completo: $userData")

                        val rol = userData?.get("rol") as? String
                        Log.d("LOGIN", "Valor del rol obtenido: $rol")


                        sessionManager.saveLoginState(true)

                        val hasRole = !rol.isNullOrBlank()
                        sessionManager.saveRoleState(hasRole)
                        onLoginSuccess(hasRole)


                    } catch (e: Exception) {
                        errorMessage = "Error al obtener datos del usuario"
                        Log.e("LOGIN", "Excepción al acceder a Firestore", e)

                    }
                } else {
                    errorMessage = "No se encontró UID"
                    Log.e("LOGIN", "UID es null")

                }
            }.onFailure {
                errorMessage = it.message
                Log.e("LOGIN", "Error durante login", it)

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
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        isLoggedInState = sessionManager.isLoggedIn()
        isRoleAssignedState = sessionManager.isRoleAssigned()
    }

    fun clearLoginState() {
        isSuccess = false
        errorMessage = null
    }

    fun newDatesUserLogin(rol: String, codigo: String) {
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

                sessionManager.saveRoleState(true)

            }.onFailure {
                errorMessage = it.message
            }
        }
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



    fun logout() {
        viewModelScope.launch {
            sessionManager.clearSession()
        }
        FirebaseAuth.getInstance().signOut()
    }
}