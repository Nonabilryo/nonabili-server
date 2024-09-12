package nonabili.nonabiliserver.follow.controller

import nonabili.nonabiliserver.follow.dto.response.FollowInfoResponse
import nonabili.nonabiliserver.follow.service.FollowService
import nonabili.nonabiliserver.common.util.ResponseFormat
import nonabili.nonabiliserver.common.util.ResponseFormatBuilder
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/follow")
class FollowController(val followService: FollowService) { // todo 헤더 필요없는거 gateway에서 설정변경(get)
    @PostMapping("/{followingIdx}")
    fun postFollowUser(@RequestHeader requestHeader: HttpHeaders, @PathVariable followingIdx: String): ResponseEntity<ResponseFormat<Any>> {
        val userIdx = requestHeader.get("userIdx")!![0]
        followService.postFollowUser(userIdx, followingIdx)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.noData())
    }

    @DeleteMapping("/{followingIdx}")
    fun deleteFollowUser(@RequestHeader requestHeader: HttpHeaders, @PathVariable followingIdx: String): ResponseEntity<ResponseFormat<Any>> {
        val userIdx = requestHeader.get("userIdx")!![0]
        followService.deleteFollowUser(userIdx, followingIdx)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.noData())
    }
    @GetMapping()
    fun getMyFollowInfo(principal: Principal): ResponseEntity<ResponseFormat<FollowInfoResponse>> {
        val result = followService.getFollowInfo(principal.name)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success"}.build(result))
    }

    @GetMapping("/{userIdx}")
    fun getFollowInfo(@PathVariable userIdx: String): ResponseEntity<ResponseFormat<FollowInfoResponse>> {
        val result = followService.getFollowInfo(userIdx)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success"}.build(result))
    }
    @GetMapping("/following/{userIdx}")
    fun getFollowing(@RequestHeader requestHeader: HttpHeaders, @PathVariable userIdx: String, @RequestParam(required = false, defaultValue = "0", value = "page") page: Int): ResponseEntity<ResponseFormat<Any>> {
//        val userId = requestHeader.get("userId")!![0]
        val result = followService.getFollowingUserInfo(userIdx, page)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.build(result))
    }

    @GetMapping("/follower/{userIdx}")
    fun getFollower(@RequestHeader requestHeader: HttpHeaders, @PathVariable userIdx: String, @RequestParam(required = false, defaultValue = "0", value = "page") page: Int): ResponseEntity<ResponseFormat<Any>> {
//        val userId = requestHeader.get("userId")!![0]
        val result = followService.getFollowerUserInfo(userIdx, page)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.build(result))
    }
}