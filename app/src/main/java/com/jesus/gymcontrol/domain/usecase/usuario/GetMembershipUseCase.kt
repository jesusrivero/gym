package com.jesus.gymcontrol.domain.usecase.usuario

import com.jesus.gymcontrol.domain.model.Membership
import com.jesus.gymcontrol.domain.repository.MembershipRepository
import javax.inject.Inject

class GetMembershipsUseCase @Inject constructor(
    private val repository: MembershipRepository
) {
    suspend operator fun invoke(): Result<List<Membership>> = repository.getMemberships()
}

