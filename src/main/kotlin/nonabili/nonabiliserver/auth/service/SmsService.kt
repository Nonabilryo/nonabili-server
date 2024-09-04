package nonabili.nonabiliserver.auth.service

import net.nurigo.sdk.message.model.Message
import net.nurigo.sdk.message.request.SingleMessageSendingRequest
import net.nurigo.sdk.message.response.SingleMessageSentResponse
import net.nurigo.sdk.message.service.DefaultMessageService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class SmsService(
    @Value("\${coolsms.api.key}") val coolsmsApiKey: String,
    @Value("\${coolsms.api.secret}") val coolsmsApiSecret: String,
    @Value("\${coolsms.from}") val coolsmsFrom: String
) {
    val messageService = DefaultMessageService(coolsmsApiKey, coolsmsApiSecret, "https://api.coolsms.co.kr")
    fun sendOne(to: String, message: String): SingleMessageSentResponse? {
        val message = Message().apply {
            from = coolsmsFrom
            this.to = to
            text = message
        }
        return messageService.sendOne(SingleMessageSendingRequest(message))
    }
}