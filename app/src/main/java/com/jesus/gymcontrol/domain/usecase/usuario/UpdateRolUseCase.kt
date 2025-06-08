package com.jesus.gymcontrol.domain.usecase.usuario

import com.jesus.gymcontrol.domain.repository.AuthRepository

class UpdateRolUseCase (private val repository: AuthRepository){
    suspend operator fun invoke(
        uid: String,
        rol: String,
        codigo: String)= repository.updateRolAndCode(uid, rol, codigo)
}