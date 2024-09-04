package nonabili.nonabiliserver.auth.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import nonabili.nonabiliserver.auth.dto.request.*
import nonabili.nonabiliserver.auth.dto.response.TokenResponse
import nonabili.nonabiliserver.auth.service.SSOService
import nonabili.nonabiliserver.common.util.ResponseFormat
import nonabili.nonabiliserver.common.util.ResponseFormatBuilder
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.math.log

@RestController
@RequestMapping("/sso")
class SSOController(val ssoService: SSOService) {
    val log = LoggerFactory.getLogger(javaClass)
    @PostMapping("/sign-up")
    fun signUp(@RequestBody @Valid request: SignUpRequest, httpServletRequest: HttpServletRequest): ResponseEntity<ResponseFormat<Any>> {
        ssoService.signUp(request, httpServletRequest.getSession(true))
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.noData())
    }

    @PostMapping("/login")
    fun login(@RequestBody @Valid request: LoginRequest): ResponseEntity<ResponseFormat<TokenResponse>> {
        val result = ssoService.login(request)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.build(result))
    }

    @PostMapping("/refresh")
    fun refresh(@RequestBody @Valid request: RefreshRequest): ResponseEntity<ResponseFormat<TokenResponse>> {
        val result = ssoService.refresh(request)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.build(result))
    }
    @PostMapping("/verify/accessToken")
    fun verifyAccessToken(@RequestBody @Valid request: VerifyAccessTokenRequest): ResponseEntity<ResponseFormat<Any>> {
        ssoService.verifyAccessToken(request)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.noData())
    }

    @PostMapping("/verify/name")
    fun verifyName(@RequestBody @Valid request: VerifyNameRequest): ResponseEntity<ResponseFormat<Any>> {
        ssoService.verifyName(request)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.noData())
    }

    @PostMapping("/verify/tell")
    fun verifyTell(@RequestBody @Valid request: VerifyTellRequest, httpServletRequest: HttpServletRequest): ResponseEntity<ResponseFormat<Any>> {
        ssoService.verifyTell(request, httpServletRequest.getSession(true))
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.noData())
    }

    @PostMapping("/verify/email")
    fun verifyEmail(@RequestBody @Valid request: VerifyEmailRequest, httpServletRequest: HttpServletRequest): ResponseEntity<ResponseFormat<Any>> {
        ssoService.verifyEmail(request, httpServletRequest.getSession(true))
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.noData())
    }
}