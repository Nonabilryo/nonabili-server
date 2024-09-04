package nonabili.nonabiliserver.auth.repository

import nonabili.nonabiliserver.auth.entity.RefreshToken
import nonabili.nonabiliserver.common.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface RefreshTokenRepository: JpaRepository<RefreshToken, String> {
    fun findRefreshTokenByUser(user: User): RefreshToken?
    fun findRefreshTokenByToken(token: String): RefreshToken?
}