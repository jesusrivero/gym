package com.jesus.gymcontrol.domain.repository

import com.jesus.gymcontrol.domain.model.Membership
import com.jesus.gymcontrol.domain.model.MembershipWithCount

interface MembershipRepository {
    suspend fun createMembership(membership: Membership):
            Result<Unit>

    suspend fun getMemberships(): Result<List<Membership>>

    suspend fun deleteMembershipIfNoUsers(membership: Membership): Result<Unit>

    suspend fun getMembershipsWithUserCount(): Result<List<MembershipWithCount>>

    suspend fun updateMembership(membership: Membership): Result<Unit>

}