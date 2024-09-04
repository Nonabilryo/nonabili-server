package nonabili.nonabiliserver.follow.repository

import nonabili.nonabiliserver.follow.entity.Follow
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface FollowRepository: JpaRepository<Follow, UUID> {
    fun findFollowByFollowerAndFollowing(follower: UUID, following: UUID): Follow?
    fun findFollowsByFollower(follower: UUID): List<Follow>
    fun findFollowsByFollowingOrderByDateDesc(following: UUID, pageable: Pageable): Page<Follow>
    fun findFollowsByFollowerOrderByDateDesc(follower: UUID, pageable: Pageable): Page<Follow>

    fun countFollowByFollowing(following: UUID): Long
    fun countFollowByFollower(follower: UUID): Long
}