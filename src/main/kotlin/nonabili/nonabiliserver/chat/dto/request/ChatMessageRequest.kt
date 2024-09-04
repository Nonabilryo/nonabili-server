package nonabili.nonabiliserver.dto.request

import nonabili.nonabiliserver.entity.ContentType


data class ChatMessageRequest(
    val sender: String,
    val receiver: String,
    val contentType: ContentType,
    val content: String
) {
    override fun toString(): String {
        return "ChatMessageRequest(sender='$sender', receiver='$receiver', contentType=$contentType, content='$content')"
    }
}