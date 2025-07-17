package com.jesus.gymcontrol.domain.usecase.usuario.getAdmin

import com.jesus.gymcontrol.domain.model.getAdmin.AdminSummary
import com.jesus.gymcontrol.domain.repository.UserAdminRepository
import javax.inject.Inject

class GetGymAdminsUseCase @Inject constructor(
	private val repository: UserAdminRepository
) {
	suspend operator fun invoke(gymCode: String): Result<List<AdminSummary>> {
		return repository.getAdministradoresByGym(gymCode)
	}
}
