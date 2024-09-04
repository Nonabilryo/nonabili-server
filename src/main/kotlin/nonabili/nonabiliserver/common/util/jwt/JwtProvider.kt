package nonabili.nonabiliserver.common.util.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import kotlin.io.encoding.Base64

@Component
class JwtProvider(
    @Value("\${jwt.secret}") val secret: String,
    @Value("\${jwt.refresh-token-expires-time}") val refreshTokenExpiresTime: Long,
    @Value("\${jwt.access-token-expires-time}") val accessTokenExpiresTime: Long
): InitializingBean {
    lateinit var key: Key
    final val ROLE_KEY = "role" // todo 수정
    @OptIn(kotlin.io.encoding.ExperimentalEncodingApi::class)
    override fun afterPropertiesSet() {
        val keyByte = Base64.decode(secret)
        this.key = Keys.hmacShaKeyFor(keyByte)
    }
    val log = LoggerFactory.getLogger(javaClass)

//    fun createAccessToken(auth: Authentication): String {
//        val role = auth.authorities.joinToString(transform =  GrantedAuthority::getAuthority)
//        val date = Date(System.currentTimeMillis() + accessTokenExpiresTime)
//        return Jwts.builder()
//            .setSubject(auth.name)
//            .claim(ROLE_KEY, role)
//            .signWith(key, SignatureAlgorithm.HS512)
//            .setExpiration(date)
//            .compact()
//    }

    fun createAccessToken(idx: UUID, role: String): String {
        val date = Date(System.currentTimeMillis() + accessTokenExpiresTime)
        return Jwts.builder()
            .setSubject(idx.toString())
            .claim(ROLE_KEY, role)
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(date)
            .compact()
    }
    fun createRefreshToken(userUUID: UUID): String {
        val date = Date(System.currentTimeMillis() + refreshTokenExpiresTime)
        return Jwts.builder()
            .setSubject(userUUID.toString())
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(date)
            .compact()
    }

    fun getAuthenticationByToken(token: String): Authentication {
        val claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
        val role = setOf(SimpleGrantedAuthority(claims[ROLE_KEY].toString()))
        val principal = User(claims.subject, "", role)
        return UsernamePasswordAuthenticationToken(principal, token, role)
    }

    fun validateToken(token: String): Jws<Claims> {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
    }
}