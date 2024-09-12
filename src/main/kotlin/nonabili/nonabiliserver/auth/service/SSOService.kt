package nonabili.nonabiliserver.auth.service

import jakarta.servlet.http.HttpSession
import nonabili.nonabiliserver.auth.dto.request.*
import nonabili.nonabiliserver.auth.dto.response.TokenResponse
import nonabili.nonabiliserver.auth.entity.RefreshToken
import nonabili.nonabiliserver.auth.entity.Role
import nonabili.nonabiliserver.user.entity.User
import nonabili.nonabiliserver.auth.repository.RefreshTokenRepository
import nonabili.nonabiliserver.common.repository.UserRepository
import nonabili.nonabiliserver.common.util.error.CustomError
import nonabili.nonabiliserver.common.util.error.ErrorState
import nonabili.nonabiliserver.common.util.jwt.JwtProvider
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Service
class SSOService(
        val userRepository: UserRepository,
        val passwordEncoder: BCryptPasswordEncoder,
        val smsService: SmsService,
        val jwtProvider: JwtProvider,
        val refreshTokenRepository: RefreshTokenRepository,
        val emailService: EmailService
) {
    val log = LoggerFactory.getLogger(javaClass)
    fun signUp(request: SignUpRequest, session: HttpSession) {
        val encodedPassword = passwordEncoder.encode(request.password)
        userRepository.findUserById(request.id)?.let { throw CustomError(ErrorState.ID_IS_ALREADY_USED) }
        userRepository.findUserByName(request.name)?.let { throw CustomError(ErrorState.NAME_IS_ALREADY_USED) }
        userRepository.findUserByTell(request.tell)?.let { throw CustomError(ErrorState.TELL_IS_ALREADY_USED) }
        val readyUser = userRepository.findUserByEmail(request.email)
        if (readyUser != null) {  // todo fix later
            if (readyUser.role == Role.READY) {
                userRepository.save(
                    readyUser.copy(
                        name = request.name,
                        id = request.id,
                        password = encodedPassword,
                        tell = request.tell,
                        role = Role.USER
                    )
                )
            } else {
                throw CustomError(ErrorState.EMAIL_IS_ALREADY_USED)
            }
        } else {
            if (session.getAttribute("email") != request.email
                || session.getAttribute("emailVerifed") == false
//                || session.getAttribute("emailVerifyCode") != request.emailVerifyCode
                || (session.getAttribute("emailVerifyCodeExp") as LocalDateTime?)?.isBefore(LocalDateTime.now()) == true
            ) {
                throw CustomError(ErrorState.NOT_VERIFED_EMAIL)
            }
            if (session.getAttribute("tell") != request.tell
                || session.getAttribute("tellVerifed") == false
//                || session.getAttribute("tellVerifyCode") != request.tellVerifyCode
                || (session.getAttribute("tellVerifyCodeExp") as LocalDateTime?)?.isBefore(LocalDateTime.now()) == true
            ) {
                throw CustomError(ErrorState.NOT_VERIFED_TELL)
            }

            val user = User(
                name = request.name,
                id = request.id,
                password = encodedPassword,
                email = request.email,
                tell = request.tell,
                adress = request.adress ?: "전국",
            )
            userRepository.save(user)
        }
    }

    fun login(request: LoginRequest): TokenResponse {
        val user = userRepository.findUserById(request.id) ?: throw CustomError(ErrorState.NOT_FOUND_ID)
        if (!passwordEncoder.matches(request.password, user.password)) throw CustomError(ErrorState.WRONG_PASSWORD)
        val accessToken = jwtProvider.createAccessToken(user.idx, user.role.name)
        val refreshToken = jwtProvider.createRefreshToken(user.idx)
        refreshTokenRepository.findRefreshTokenByUser(user)?.let {
            refreshTokenRepository.delete(it)
        }
        refreshTokenRepository.save(
            RefreshToken(
                token = refreshToken,
                user = user
            )
        )
        return TokenResponse(accessToken, refreshToken)
    }

    fun refresh(request: RefreshRequest): TokenResponse {
        jwtProvider.validateToken(request.refreshToken)
        val refreshToken = refreshTokenRepository.findRefreshTokenByToken(request.refreshToken)
        if (refreshToken == null) CustomError(ErrorState.NOT_FOUND_REFRESHTOKEN)
        val accessToken = jwtProvider.createAccessToken(refreshToken!!.user.idx, refreshToken!!.user.role.name)
        return TokenResponse(
            accessToken = accessToken,
            refreshToken = refreshToken.token
        )
    }
    fun verifyAccessToken(request: VerifyAccessTokenRequest) {
        try {
            jwtProvider.validateToken(request.accessToken)
        } catch (e: Exception) {
            throw CustomError(ErrorState.ACCESSTOKEN_HAS_EXPIRED)
        }
    }
    fun verifyName(request: VerifyNameRequest) {
        userRepository.findUserByName(request.name)?.let { throw CustomError(ErrorState.NAME_IS_ALREADY_USED) }
    }

    fun verifyTell(request: VerifyTellRequest, session: HttpSession): String {
        userRepository.findUserByTell(request.tell)?.let { throw CustomError(ErrorState.TELL_IS_ALREADY_USED) }
        val randomCode = (1..8).map { "0123456789".random() }.joinToString("")
        val message = "[logo]\n ${randomCode} <- 당신의 인증코드"
        smsService.sendOne(request.tell, message)
        session.setAttribute("tell", request.tell)
        session.setAttribute("tellVerifyCode", randomCode)
        session.setAttribute("tellVerifyCodeExp", LocalDateTime.now().plus(3, ChronoUnit.MINUTES))
        session.setAttribute("tellVerifed", false)
        return randomCode
    }

    fun verifyEmail(request: VerifyEmailRequest, session: HttpSession): String {
        userRepository.findUserByEmail(request.email)?.let { throw CustomError(ErrorState.EMAIL_IS_ALREADY_USED) }
        val randomCode = (1..8).map { "0123456789".random() }.joinToString("")
        emailService.sendVerify(request.email, randomCode)
        session.setAttribute("email", request.email)
        session.setAttribute("emailVerifyCode", randomCode)
        session.setAttribute("emailVerifyCodeExp", LocalDateTime.now().plus(3, ChronoUnit.MINUTES))
        session.setAttribute("emailVerifed", false)
        return randomCode
    }

    fun verifyEmailCode(request: VerifyEmailCodeRequest, session: HttpSession) {
        if (session.getAttribute("emailVerifyCode") != request.emailVerifyCode
                || (session.getAttribute("emailVerifyCodeExp") as LocalDateTime?)?.isBefore(LocalDateTime.now()) == true
        ) throw CustomError(ErrorState.WRONG_EMAILVERIFYCODE)
        session.setAttribute("emailVerifyCodeExp", LocalDateTime.now().plus(10, ChronoUnit.MINUTES))
        session.setAttribute("emailVerifed", true)
    }
    fun verifyTellCode(request: VerifyTellCodeRequest, session: HttpSession) {
        if (session.getAttribute("tellVerifyCode") != request.tellVerifyCode
                || (session.getAttribute("tellVerifyCodeExp") as LocalDateTime?)?.isBefore(LocalDateTime.now()) == true
        ) throw CustomError(ErrorState.WRONG_EMAILVERIFYCODE)
        session.setAttribute("tellVerifyCodeExp", LocalDateTime.now().plus(10, ChronoUnit.MINUTES))
        session.setAttribute("tellVerifed", true)
    }
}