package com.jesus.gymcontrol.domain.usecase.usuario.getDates

import com.jesus.gymcontrol.domain.model.ListUser
import com.jesus.gymcontrol.domain.repository.UserRepository
import javax.inject.Inject

class GetUserByGymUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(gymCode: String):
            Result<List<ListUser>> {
        return userRepository.getUserByGym(gymCode)
    }
}