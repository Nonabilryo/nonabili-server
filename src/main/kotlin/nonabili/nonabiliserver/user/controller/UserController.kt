package nonabili.nonabiliserver.user.controller

import jakarta.validation.Valid
import nonabili.nonabiliserver.user.dto.request.DeleteUserRequest
import nonabili.nonabiliserver.user.dto.request.PatchUserNameRequest
import nonabili.nonabiliserver.user.dto.request.PatchUserPasswordRequest
import nonabili.nonabiliserver.user.dto.request.PutUserImageRequest
import nonabili.nonabiliserver.user.dto.response.UserInfoResponse
import nonabili.nonabiliserver.user.dto.response.UserIdxResponse
import nonabili.nonabiliserver.user.dto.response.UserNameAndImageResponse
import nonabili.nonabiliserver.user.service.UserService
import nonabili.nonabiliserver.common.util.ResponseFormat
import nonabili.nonabiliserver.common.util.ResponseFormatBuilder
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.security.Principal

@RestController
@RequestMapping("/user")
class UserController(val userService: UserService) {
    val log = LoggerFactory.getLogger(javaClass)
    @GetMapping()
    fun getUserInfo(principal: Principal): ResponseEntity<ResponseFormat<UserInfoResponse>> {
        val userIdx = principal.name
        val result = userService.getUserInfo(userIdx)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.build(result))
    }
    @GetMapping("/{userIdx}")
    fun getUserInfoByIdx(@PathVariable userIdx: String): ResponseEntity<ResponseFormat<UserInfoResponse>> {
        val result = userService.getUserInfo(userIdx)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.build(result))
    }


//    @GetMapping("/NameToIdx/{userName}")  ///
//    fun getUserIdxByName(@PathVariable userName: String): ResponseEntity<UserIdxResponse> {
//        val result = userService.getUserIdxByName(userName)
//        return ResponseEntity.ok(result)
//    }
//
//    @GetMapping("/IdToIdx/{userId}")  ///
//    fun getUserIdxById(@PathVariable userId: String): ResponseEntity<UserIdxResponse> {
//        val result = userService.getUserIdxById(userId)
//        return ResponseEntity.ok(result)
//    }
//
//    @GetMapping("/IdxToUserInfo/{userIdx}")  ///
//    fun getUserInfoByIdx2(@PathVariable userIdx: String): ResponseEntity<UserNameAndImageResponse> {
//        val result = userService.getUserInfoByIdx(userIdx)
//        return ResponseEntity.ok(result)
//    }

    @PatchMapping("/password")
    fun patchUserPassword(principal: Principal, @RequestBody @Valid request: PatchUserPasswordRequest): ResponseEntity<ResponseFormat<Any>> {
        val userIdx = principal.name
        userService.patchUserPassword(userIdx, request)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.noData())
    }

    @PatchMapping("/name")
    fun patchUserName(principal: Principal, @RequestBody @Valid request: PatchUserNameRequest): ResponseEntity<ResponseFormat<Any>>  {
        val userIdx = principal.name
        userService.patchUserName(userIdx, request)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.noData())
    }

    @PutMapping("/image")
    fun putUserImage(principal: Principal, image: MultipartFile): ResponseEntity<ResponseFormat<Any>>  {
        val userIdx = principal.name
        userService.putUserImage(userIdx, image)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.noData())
    }

    //ㅍㅡ로필 사진, 탈
    @PostMapping("/delete")
    fun deleteUser(principal: Principal, @RequestBody @Valid request: DeleteUserRequest): ResponseEntity<ResponseFormat<Any>> {
        val userIdx = principal.name
        userService.deleteUser(userIdx, request)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.noData())
    }

}