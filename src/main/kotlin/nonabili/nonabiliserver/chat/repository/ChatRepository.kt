package nonabili.nonabiliserver.repository

import nonabili.nonabiliserver.entity.Chat
import nonabili.nonabiliserver.entity.ChatSenderAndReceiver
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface ChatRepository: JpaRepository<Chat, Long> {
    fun findChatsBySenderOrReceiver(sender: UUID, receiver: UUID, pageable: Pageable): Page<Chat>
    @Query("SELECT DISTINCT (c.sender, c.receiver) FROM Chat c WHERE c.sender = :uuid OR c.receiver = :uuid order by c.createdAt DESC")
    fun findDistinctChatByUUID(@Param("uuid") uuid: UUID, pageable: Pageable): Page<ChatSenderAndReceiver>
    @Query("SELECT c FROM Chat c WHERE (c.sender = :uuid AND c.receiver = :uuid2) OR (c.sender = :uuid2 AND c.receiver = :uuid) ORDER BY c.createdAt DESC LIMIT 1")
    fun findLatestChatBetweenUsers(@Param("uuid") uuid: UUID, @Param("uuid2") uuid2: UUID): Chat?

    fun findChatByIdx(idx: Long): Chat?
}