package nonabili.nonabiliserver.service


import nonabili.nonabiliserver.dto.request.ChatMessageRequest
import nonabili.nonabiliserver.entity.Chat
import nonabili.nonabiliserver.repository.ChatRepository
import nonabili.nonabiliserver.common.util.error.CustomError
import nonabili.nonabiliserver.common.util.error.ErrorState
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.*

@Service
class ChatService(val chatRepository: ChatRepository) {
    val log = LoggerFactory.getLogger(javaClass)
    fun saveChat(chatMessage: ChatMessageRequest): Chat{
        log.info(chatMessage.toString())
        return chatRepository.save(Chat(
            sender = UUID.fromString(chatMessage.sender),
            receiver = UUID.fromString(chatMessage.receiver),
            contentType = chatMessage.contentType,
            content = chatMessage.content
        )
        )
    }
    fun getLatestChat(userIdx: String, page: Int): Page<Chat> {
        val relatedUserIdx = chatRepository.findDistinctChatByUUID(UUID.fromString(userIdx), PageRequest.of(page, 25)).map { if(it.receiver.toString() == userIdx) it.sender else it.receiver }
        val chats = relatedUserIdx.map { chatRepository.findLatestChatBetweenUsers(UUID.fromString(userIdx), it) ?: Chat(sender = UUID.fromString(userIdx), receiver = it, content = "--오류--") }
//        return chatRepository.findChatsBySenderOrReceiver(UUID.fromString(userIdx), UUID.fromString(userIdx), PageRequest.of(page, 25))
        return chats
    }
    fun deleteChat(userIdx: String, chatIdx: Long) {
        val chat = chatRepository.findChatByIdx(chatIdx) ?: throw CustomError(ErrorState.NOT_FOUDN_CHAT)
        if (chat.sender.toString() != userIdx) throw CustomError(ErrorState.DIFFERENT_USER)
        chatRepository.delete(chat)
    }
}