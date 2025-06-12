package com.jesus.gymcontrol.domain.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.jesus.gymcontrol.domain.model.Gym
import com.jesus.gymcontrol.domain.usecase.usuario.CreateGymUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.GenerateCodeUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.GetAllGymUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.GetGymByOwnerUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.MarkCodeAsUseUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.ValidateClientCodeUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.ValidateOwnerCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.onSuccess

@HiltViewModel
class GymViewModel @Inject constructor(
    private val createGymUseCase: CreateGymUseCase,
    private val getAllGymUseCase: GetAllGymUseCase,
    private val firebaseAuth: FirebaseAuth,
    private val validateGymCodeUseCase: ValidateClientCodeUseCase,
    private val markCodeAsUseUseCase: MarkCodeAsUseUseCase,
    private val generateCodeUseCase: GenerateCodeUseCase,
    private val getGymByOwnerUseCase: GetGymByOwnerUseCase,
    private val validateOwnerCodeUseCase: ValidateOwnerCodeUseCase,
    private val validateClientCodeUseCase: ValidateClientCodeUseCase
) : ViewModel() {

    var gyms by mutableStateOf<List<Gym>>(emptyList())
    var searchQuery by mutableStateOf("")
    var name by mutableStateOf("")
    var direction by mutableStateOf("")
    var phone by mutableStateOf("")
    var code by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var isSuccess by mutableStateOf(false)

    var isCodeValid by mutableStateOf<Boolean?>(null)
        private set
    var codeValidationError by mutableStateOf<String?>(null)

    var gymCode by mutableStateOf<String?>(null)
        private set

    var codeGenerationError by mutableStateOf<String?> (null)

    var generatedCode by mutableStateOf<String?>(null)
        private set

    var isGenerating by mutableStateOf(false)
        private set

    var selectedGym by mutableStateOf<Gym?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set



    fun createGym() {
        val uid = firebaseAuth.currentUser?.uid ?: return

        val name = name.trim()
        val direction = direction.trim()
        val phone = phone.trim()
        val code = code.trim()

        viewModelScope.launch {
            isLoading = true
            isSuccess = false
            errorMessage = null

            val result = createGymUseCase(
                uid = uid,
                code = code,
                name = name,
                direction = direction,
                phone = phone
            )

            isLoading = false
            result.onSuccess {
                isSuccess = true
                markCodeAsUsed(code)
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

    fun validateGymCode(code: String, gymCode: String) {
        viewModelScope.launch {
            Log.d("GymViewModel", "validando")
            isCodeValid = null
            codeValidationError = null

            val result = validateGymCodeUseCase(code.trim(), gymCode.trim())
            result.onSuccess {
                isCodeValid = it
                Log.d("GymViewModel", "Resultado de validacion: $it")
                if (!it)
                    codeValidationError = "Codigo invalido o ya usado"
            }.onFailure {
                Log.d("GymViewModel", "Error al validar el codigo")
                codeValidationError = "Error al validar el codigo: ${it.message}"
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

    fun markCodeAsUsed(code: String) {
        viewModelScope.launch {
            val result = markCodeAsUseUseCase(code)
            result.onSuccess {
                Log.d("GymViewModel", "Código marcado como usado exitosamente")
            }.onFailure {
                errorMessage = "Error al marcar el código como usado: ${it.message}"
                Log.e("GymViewModel", errorMessage ?: "Error desconocido")
            }
        }
    }

    fun generateClientCode(gymCode: String) {
        if (gymCode.isBlank()) {
            errorMessage = "No se encontró el código del gimnasio"
            return
        }

        viewModelScope.launch {
            isGenerating = true
            errorMessage = null

            val result = generateCodeUseCase(gymCode)
            result
                .onSuccess { generatedCode = it }
                .onFailure { error -> errorMessage = error.message }

            isGenerating = false
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
            errorMessage = "UID invalido"
            return
        }
        viewModelScope.launch {
            val result = getGymByOwnerUseCase(uid)
            result.onSuccess { gym -> gymCode = gym.code
            }.onFailure {
                errorMessage = it.message ?: "Error al cargar el gimnasio"
            }
        }
    }
    fun seledGym(gym:Gym){
        selectedGym = gym
    }

//    fun generateClientCode(gymCode: String) {
//        viewModelScope.launch {
//            isGenerating = true
//            errorMessage = null
//
//            val result = generateCodeUseCase(gymCode)
//
//            result
//                .onSuccess { code -> generatedCode = code }
//                .onFailure { error -> errorMessage = error.message }
//
//            isGenerating = false
//        }
//    }

//    fun clearGeneratedCode() {
//        generatedCode = null
//    }


}