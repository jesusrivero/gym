package com.jesus.gymcontrol.domain.usecase.usuario.membership

import com.jesus.gymcontrol.domain.repository.MembershipRepository
import javax.inject.Inject

class ToggleMembershipStateUseCase @Inject constructor(
	private val repository: MembershipRepository
) {
	suspend operator fun invoke(membershipId: String, gymCode: String, newState: String): Result<Unit> {
		return repository.setMembershipState(membershipId, gymCode, newState)
	}
}
