package com.jesus.gymcontrol.domain.usecase.usuario


import com.jesus.gymcontrol.domain.model.Gym
import com.jesus.gymcontrol.domain.repository.UserRepository
import javax.inject.Inject

class AssignGymToUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(uid: String, gym: Gym): Result<Unit> {
        return repository.assignGymToUser(uid, gym)
    }
}