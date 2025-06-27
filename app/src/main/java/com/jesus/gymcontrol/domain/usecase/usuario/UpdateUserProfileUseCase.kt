package com.jesus.gymcontrol.domain.usecase.usuario

import com.jesus.gymcontrol.domain.model.UserUpdate
import com.jesus.gymcontrol.domain.repository.UserRepository

class UpdateUserProfileUseCase(
	private val userRepository: UserRepository
) {
	suspend operator fun invoke(
		uid: String,
		gymCode: String,
		userUpdate: UserUpdate
	): Result<Unit> {
		return userRepository.updateUserProfile(uid, gymCode, userUpdate)
	}
}