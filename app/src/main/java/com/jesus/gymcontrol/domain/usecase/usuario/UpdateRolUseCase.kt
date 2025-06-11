package com.jesus.gymcontrol.domain.usecase.usuario

import com.jesus.gymcontrol.domain.repository.AuthRepository

class UpdateRolUseCase (private val repository: AuthRepository){
    suspend operator fun invoke(
        uid: String,
        rol: String,
        code: String)= repository.updateRolAndCode(uid, rol, code)
}