package com.jesus.gymcontrol.domain.usecase.usuario.membership

import com.jesus.gymcontrol.domain.repository.MembershipRepository
import javax.inject.Inject

class ToggleMembershipStateUseCase @Inject constructor(
	private val membershipRepository: MembershipRepository
) {
	suspend operator fun invoke(membershipId: String, gymCode: String, newState: Boolean): Result<Unit> {
		return membershipRepository.setMembershipState(membershipId, gymCode,newState)
	}
}