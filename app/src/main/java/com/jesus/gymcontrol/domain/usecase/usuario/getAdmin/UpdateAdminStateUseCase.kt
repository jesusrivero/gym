package com.jesus.gymcontrol.domain.usecase.usuario.getAdmin

import com.jesus.gymcontrol.domain.repository.UserAdminRepository
import javax.inject.Inject


class UpdateAdminStateUseCase @Inject constructor(
	private val repository: UserAdminRepository
) {
	suspend operator fun invoke(gymCode: String, adminUid: String, newState: String): Result<Unit> {
		return repository.updateAdminState(gymCode, adminUid, newState)
	}
}