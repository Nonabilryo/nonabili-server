package nonabili.nonabiliserver.auth.filter

import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import nonabili.nonabiliserver.common.util.jwt.JwtProvider
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean

@Component
class TokenFilter(val tokenProvider: JwtProvider, @Value("\${jwt.haeder}") val header: String): GenericFilterBean() {
    val log = LoggerFactory.getLogger(javaClass)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        request as HttpServletRequest

        log.info(request.requestURI)
        try {
            request.getHeader(header)?.let {
                log.info(it)
                tokenProvider.validateToken(it)
                val auth = tokenProvider.getAuthenticationByToken(it)
                SecurityContextHolder.getContext().authentication = auth
            }

            chain.doFilter(request, response)
        } catch (e: ExpiredJwtException) {
            handleException(response as HttpServletResponse, "Jwt was expired", 401)
        }

    }
    private fun handleException(response: HttpServletResponse, message: String, status: Int) {
        log.error(message)
        response.status = 401
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.writer.write(
                """
            {
                "state": $status,
                "message": "$message"
            }
            """.trimIndent()
        )
    }
}