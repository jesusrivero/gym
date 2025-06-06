package com.jesus.gymcontrol.domain.usecase.usuario

import com.jesus.gymcontrol.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
	
    private val repository: AuthRepository
){
    suspend operator fun invoke(email: String, password: String): Result<Unit>
    {
        return repository.loginUser(email, password)
    }


}