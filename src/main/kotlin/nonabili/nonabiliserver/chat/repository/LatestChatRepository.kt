package nonabili.nonabiliserver.chat.repository

import nonabili.nonabiliserver.chat.entity.LatestChat
import nonabili.nonabiliserver.entity.Chat
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface LatestChatRepository: JpaRepository<LatestChat, UUID> {
    @Query("SELECT c FROM LatestChat c WHERE (c.sender = :uuid AND c.receiver = :uuid2) OR (c.sender = :uuid2 AND c.receiver = :uuid) ORDER BY c.updatedAt DESC LIMIT 1")
    fun findLatestChatBetweenUsers(@Param("uuid") uuid: UUID, @Param("uuid2") uuid2: UUID): LatestChat?
    @Query("SELECT c FROM LatestChat c WHERE c.sender = :uuid OR c.receiver = :uuid ORDER BY c.updatedAt DESC")
    fun findLatestChatsByUuid(@Param("uuid") uuid: UUID, pageable: Pageable): Page<LatestChat>
}