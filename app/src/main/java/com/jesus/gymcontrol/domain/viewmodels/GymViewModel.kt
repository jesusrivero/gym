package com.jesus.gymcontrol.domain.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jesus.gymcontrol.domain.model.Gym
import com.jesus.gymcontrol.domain.usecase.usuario.CreateGymUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.GenerateCodeUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.getDates.GetAllGymUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.getDates.GetGymByOwnerUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.MarkCodeAsUseUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.validateCode.ValidateAdminCodeUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.validateCode.ValidateClientCodeUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.validateCode.ValidateOwnerCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.onSuccess


@HiltViewModel
class GymViewModel @Inject constructor(
    private val createGymUseCase: CreateGymUseCase,
    private val getAllGymUseCase: GetAllGymUseCase,
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val markCodeAsUseUseCase: MarkCodeAsUseUseCase,
    private val generateCodeUseCase: GenerateCodeUseCase,
    private val getGymByOwnerUseCase: GetGymByOwnerUseCase,
    private val validateOwnerCodeUseCase: ValidateOwnerCodeUseCase,
    private val validateClientCodeUseCase: ValidateClientCodeUseCase,
    private val validateAdminCodeUseCase: ValidateAdminCodeUseCase,
	private val getAvailableCodesUseCase: com.jesus.gymcontrol.domain.usecase.usuario.codes.GetAvailableCodesUseCase
) : ViewModel() {

    var gyms by mutableStateOf<List<Gym>>(emptyList())
    var searchQuery by mutableStateOf("")
    var name by mutableStateOf("")
		var rif by mutableStateOf("")
    var direction by mutableStateOf("")
    var phone by mutableStateOf("")
    var code by mutableStateOf("")
    val rol by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var isSuccess by mutableStateOf(false)

    var isCodeValid by mutableStateOf<Boolean?>(null)
        private set
    var codeValidationError by mutableStateOf<String?>(null)

    var gymCode by mutableStateOf<String?>(null)
        private set
	
    var generatedCode by mutableStateOf<String?>(null)
        private set

    var isGenerating by mutableStateOf(false)
        private set

    var selectedGym by mutableStateOf<Gym?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var setSelectedRoleForCode by mutableStateOf("cliente")
        private set
	

	private val _availableCodes = MutableStateFlow<List<String>>(emptyList())
	val availableCodes: StateFlow<List<String>> = _availableCodes
	
	private val _codesError = MutableStateFlow<String?>(null)
	val codesError: StateFlow<String?> = _codesError
	
	fun loadAvailableCodes() {
		viewModelScope.launch {
			val result = getAvailableCodesUseCase()
			result.onSuccess { codes ->
				_availableCodes.value = codes
			}.onFailure { error ->
				_codesError.value = error.message
			}
		}
	}
	
	
	
	fun generateCodeForRole() {
        val uid = firebaseAuth.currentUser?.uid ?: return

        viewModelScope.launch {
            isGenerating = true
            errorMessage = null
            generatedCode = null

            // Obtener el código del gimnasio del dueño
            val gymResult = getGymByOwnerUseCase(uid)
            gymResult.onSuccess { gym ->
                val gymCode = gym.code
                val codeResult = generateCodeUseCase(gymCode, setSelectedRoleForCode)
                codeResult.onSuccess {
                    generatedCode = it
                }.onFailure {
                    errorMessage = it.message
                }
            }.onFailure {
                errorMessage = it.message ?: "No se pudo obtener el gimnasio"
            }

            isGenerating = false
        }
    }

    fun SetSelectedRoleForCode(role:String){
        setSelectedRoleForCode = role
    }


    fun createGym() {
        val uid = firebaseAuth.currentUser?.uid ?: return

        val name = name.trim()
        val direction = direction.trim()
	      val rif = rif.trim()
        val phone = phone.trim()
        val code = code.trim()
        val rol = rol.trim()


        viewModelScope.launch {
            isLoading = true
            isSuccess = false
            errorMessage = null

            val result = createGymUseCase(
                uid = uid,
                code = code,
                name = name,
                direction = direction,
	              rif = rif,
                phone = phone,


            )

            isLoading = false
            result.onSuccess {
                isSuccess = true
                markCodeAsUsed(code, rol)
            }.onFailure {
                errorMessage = it.message
            }
        }
    }

    fun fetchAllGyms() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            Log.d("GymViewModel", "Iniciando fetchAllGyms...")

            val result = getAllGymUseCase()
            result.onSuccess {
                gyms = it
                Log.d("GymViewModel", "Gimnasios obtenidos: ${it.size}")
                it.forEach { gym ->
                    Log.d("GymViewModel", "Gym: ${gym.name}, código: ${gym.code}")
                }
            }.onFailure {
                errorMessage = it.message
                Log.e("GymViewModel", "Error al obtener gimnasios: ${it.message}", it)
            }

            isLoading = false
            Log.d("GymViewModel", "Finalizó fetchAllGyms. isLoading = $isLoading")
        }
    }

    fun filteredGyms(): List<Gym> {
        return gyms.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    fun validateAdminCode(code: String, gymCode: String) {
        viewModelScope.launch {
            isCodeValid = null
            codeValidationError = null

            val result = validateAdminCodeUseCase(code.trim(), gymCode.trim())
            result.onSuccess { isValid ->
                isCodeValid = isValid
                if (!isValid) {
                    codeValidationError = "Código inválido, ya usado o no autorizado"
                }
            }.onFailure {
                codeValidationError = "Error al validar el código: ${it.message}"
            }
        }
    }

    fun validateOwnerCode(code: String) {
        viewModelScope.launch {
            isCodeValid = null
            codeValidationError = null

            val result = validateOwnerCodeUseCase(code.trim())
            result.onSuccess {
                isCodeValid = it
                if (!it) {
                    codeValidationError = "Código inválido, ya usado o no autorizado para crear gimnasios"
                }
            }.onFailure {
                codeValidationError = "Error al validar el código: ${it.message}"
            }
        }
    }

    fun validateClientCode(code: String, gymCode: String) {
        viewModelScope.launch {
            isCodeValid = null
            codeValidationError = null

            val result = validateClientCodeUseCase(code.trim(), gymCode.trim())
            result.onSuccess {
                isCodeValid = it
                if (!it) {
                    codeValidationError = "Código inválido, ya usado o no pertenece a este gimnasio"
                }
            }.onFailure {
                codeValidationError = "Error al validar el código: ${it.message}"
            }
        }
    }

    fun markCodeAsUsed(code: String, rol: String) {
        viewModelScope.launch {
            val result = markCodeAsUseUseCase(code, rol)
            result.onSuccess {
                Log.d("GymViewModel", "Código marcado como usado exitosamente")
            }.onFailure {
                errorMessage = "Error al marcar el código como usado: ${it.message}"
                Log.e("GymViewModel", errorMessage ?: "Error desconocido")
            }
        }
    }


    fun clearErrorMessage() {
        errorMessage = null
    }

    fun resetValidation(){
        isCodeValid = null
        codeValidationError = null
    }

    fun loadCurrentUserGymCode(uid: String) {
        if (uid.isBlank()) {
            errorMessage = "UID inválido"
            return
        }
        viewModelScope.launch {
            try {
                val docSnapshot = firestore.collection("users")
                    .document(uid)
                    .get()
                    .await()

                if (docSnapshot.exists()) {
                    val code = docSnapshot.getString("gimnasioCode") // o como hayas nombrado ese campo
                    if (!code.isNullOrBlank()) {
                        gymCode = code
                    } else {
                        errorMessage = "Código de gimnasio no encontrado para este administrador"
                    }
                } else {
                    errorMessage = "Administrador no encontrado"
                }
            } catch (e: Exception) {
                errorMessage = e.message ?: "Error al cargar el código del gimnasio"
            }
        }
    }
  
	

}