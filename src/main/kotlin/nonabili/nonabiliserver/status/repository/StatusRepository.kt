package nonabili.nonabiliserver.repository

import nonabili.nonabiliserver.entity.Status
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface StatusRepository: JpaRepository<Status, UUID> {
    fun findStatusesByArticle(articleIdx: UUID, pageable: Pageable): Page<Status>
    fun findStatusByIdx(idx: UUID): Status?
}