package nonabili.nonabiliserver.follow.service

import nonabili.nonabiliserver.common.repository.UserRepository
import nonabili.nonabiliserver.follow.dto.response.FollowInfoResponse
import nonabili.nonabiliserver.follow.dto.response.UserInfoResponse
import nonabili.nonabiliserver.follow.entity.Follow
import nonabili.nonabiliserver.follow.repository.FollowRepository
import nonabili.nonabiliserver.common.util.error.CustomError
import nonabili.nonabiliserver.common.util.error.ErrorState
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.*
import kotlin.math.log

@Service
class FollowService(val followRepository: FollowRepository, val userRepository: UserRepository) {
    val log = LoggerFactory.getLogger(javaClass)
    fun postFollowUser(userIdx: String, followingIdx: String) {
        val follower = userRepository.findUserByIdx(UUID.fromString(userIdx)) ?: throw CustomError(ErrorState.NOT_FOUND_USER)
        val following = userRepository.findUserByIdx(UUID.fromString(followingIdx)) ?: throw CustomError(ErrorState.NOT_FOUND_USER)
        followRepository.findFollowByFollowerAndFollowing(follower, following)
            ?.let { throw CustomError(ErrorState.AREADY_FOLLOWED) }

        followRepository.save(Follow(
            follower = follower,
            following = following
        ))
    }
    fun deleteFollowUser(userIdx: String, followingIdx: String) {
        val follower = userRepository.findUserByIdx(UUID.fromString(userIdx)) ?: throw CustomError(ErrorState.NOT_FOUND_USER)
        val following = userRepository.findUserByIdx(UUID.fromString(followingIdx)) ?: throw CustomError(ErrorState.NOT_FOUND_USER)
        val follow = followRepository.findFollowByFollowerAndFollowing(follower, following)
            ?: throw CustomError(ErrorState.NOT_FOUND_FOLLOW)
        followRepository.delete(follow)
    }

    fun getFollowInfo(userIdx: String): FollowInfoResponse {
        val user = userRepository.findUserByIdx(UUID.fromString(userIdx)) ?: throw CustomError(ErrorState.NOT_FOUND_USER)
        val followCount = followRepository.findFollowerAndFollowingCounts(user)
        return FollowInfoResponse(
            follower = followCount.follower,
            following = followCount.following
        )
    }

    fun getFollowingUserInfo(userIdx: String, page: Int): Page<UserInfoResponse> {
        val user = userRepository.findUserByIdx(UUID.fromString(userIdx)) ?: throw CustomError(ErrorState.NOT_FOUND_USER)
        val follows = followRepository.findFollowsByFollowerOrderByDateDesc(user, PageRequest.of(page, 100))

        log.info("total elements: " + follows.totalElements.toString())
        log.info("total pages: " + follows.totalPages.toString())
        val userInfos = PageImpl(follows.content.map {
            val user = it.following
            UserInfoResponse(userName = user.name, image = user.image?.url)
                                                     }, follows.pageable, follows.totalElements)
        return userInfos
    }

    fun getFollowerUserInfo(userIdx: String, page: Int): Page<UserInfoResponse> {
        val user = userRepository.findUserByIdx(UUID.fromString(userIdx)) ?: throw CustomError(ErrorState.NOT_FOUND_USER)
        val follows = followRepository.findFollowsByFollowingOrderByDateDesc(user, PageRequest.of(page, 100))

        log.info("total elements: " + follows.totalElements.toString())
        log.info("total pages: " + follows.totalPages.toString())
        val userInfos = PageImpl(follows.content.map {
            val user = it.following
            UserInfoResponse(userName = user.name, image = user.image?.url)
        }, follows.pageable, follows.totalElements)
        return userInfos
    }
}