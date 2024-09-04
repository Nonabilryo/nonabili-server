package nonabili.nonabiliserver.common.repository

import nonabili.nonabiliserver.common.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserRepository: JpaRepository<User, UUID> {
    fun findUserByName(name: String): User?
    fun findUserById(id: String): User?
    fun findUserByTell(tell: String): User?
    fun findUserByEmail(email: String): User?

    fun findUserByIdx(idx: UUID): User?
}