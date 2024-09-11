package nonabili.nonabiliserver.auth.handler

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import nonabili.nonabiliserver.auth.entity.RefreshToken
import nonabili.nonabiliserver.auth.entity.Role
import nonabili.nonabiliserver.user.entity.User
import nonabili.nonabiliserver.auth.repository.RefreshTokenRepository
import nonabili.nonabiliserver.common.repository.UserRepository
import nonabili.nonabiliserver.common.util.jwt.JwtProvider
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID

@Component
class OauthSuccessHandler(val userRepository: UserRepository, val jwtProvider: JwtProvider, val refreshTokenRepository: RefreshTokenRepository): SimpleUrlAuthenticationSuccessHandler() {
    val log = LoggerFactory.getLogger(javaClass)
    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {
        val oAuth2User = authentication?.principal as OAuth2User
        log.info("httpSerletRequest - " + request)
        log.info("oauth login - " + oAuth2User.attributes.toString())

        val user = userRepository.findUserByEmail(oAuth2User.attributes.get("email").toString())
        val session = request?.getSession(true)

        if (user == null) {
            userRepository.save(
                User(
                    name = "유저${System.currentTimeMillis()/10}",
                    id = "GG_${oAuth2User.attributes.get("sub")}",
                    password = "${UUID.randomUUID().toString().slice(IntRange(0,24))}",
                    email = oAuth2User.attributes.get("email").toString(),
                    role = Role.READY
                )
            )
            val randomCode = (1..8).map { "0123456789".random() }.joinToString("")
            session?.setAttribute("email", oAuth2User.attributes.get("email"))
            session?.setAttribute("emailVerifyCode", randomCode)
            session?.setAttribute("emailVerifyCodeExp", LocalDateTime.now().plus(3, ChronoUnit.MINUTES))

            response?.sendRedirect("http://localhost:8080?emailVerifyCode=${randomCode}&email=${oAuth2User.attributes.get("email")}")  // front sign-up
        } else {
            val accessToken = jwtProvider.createAccessToken(user.idx, user.role.name)
            val refreshToken = jwtProvider.createRefreshToken(user.idx)
            refreshTokenRepository.save(RefreshToken(
                token = refreshToken,
                user = user
            ))

            response?.addCookie(createCookie("AccessToken", accessToken))
            response?.addCookie(createCookie("RefreshToken", refreshToken))

            if (user.role == Role.READY) {
                val randomCode = (1..8).map { "0123456789".random() }.joinToString("")
                session?.setAttribute("email", oAuth2User.attributes.get("email"))
                session?.setAttribute("emailVerifyCode", randomCode)
                session?.setAttribute("emailVerifyCodeExp", LocalDateTime.now().plus(3, ChronoUnit.MINUTES))
//                response?.addCookie(createCookie("emailVerifyCode", "randomCode"))
                response?.sendRedirect("http://localhost:8080?emailVerifyCode=${randomCode}&email=${oAuth2User.attributes.get("email")}")  // todo: cookie or param ?
            } else {
                response?.sendRedirect("http://localhost:8080")  // front address
            }

        }



    }
    private fun createCookie(key: String, value: String): Cookie? {
        val cookie = Cookie(key, value)
        cookie.setMaxAge(60 * 60 * 60)
//        cookie.setSecure(true);
        cookie.setPath("/")
        cookie.setHttpOnly(true)
        return cookie
    }
}