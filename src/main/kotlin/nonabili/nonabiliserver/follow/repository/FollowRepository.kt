package nonabili.nonabiliserver.follow.repository

import nonabili.nonabiliserver.user.entity.User
import nonabili.nonabiliserver.follow.entity.Follow
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface FollowRepository: JpaRepository<Follow, UUID> {
    fun findFollowByFollowerAndFollowing(follower: User, following: User): Follow?
    fun findFollowsByFollower(follower: User): List<Follow>
    fun findFollowsByFollowingOrderByDateDesc(following: User, pageable: Pageable): Page<Follow>
    fun findFollowsByFollowerOrderByDateDesc(follower: User, pageable: Pageable): Page<Follow>

//    fun countFollowByFollowing(following: User): Long
//    fun countFollowByFollower(follower: User): Long
    @Query("SELECT " +
            "(SELECT COUNT(f) FROM Follow f WHERE f.follower = :user) AS followerCount, " +
            "(SELECT COUNT(f) FROM Follow f WHERE f.following = :user) AS followingCount " +
            "FROM Follow f " +
            "WHERE f.follower = :user OR f.following = :user")
    fun findFollowerAndFollowingCounts(user: User): FollowCount

}