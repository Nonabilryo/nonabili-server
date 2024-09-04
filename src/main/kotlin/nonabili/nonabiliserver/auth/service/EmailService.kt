package nonabili.nonabiliserver.auth.service

import jakarta.mail.internet.MimeMessage
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

@Service
class EmailService(
    val javaMailSender: JavaMailSender,
    @Value("\${spring.mail.username}")
    val fromEmail: String,
    val templateEngine: TemplateEngine
) {
    val log = LoggerFactory.getLogger(javaClass)

    fun sendVerify(receiver: String, verifyCode: String) {
        val message = javaMailSender.createMimeMessage()
        message.setFrom(fromEmail)
        message.subject = "노나빌려에서 이메일 인증"
        message.setRecipients(MimeMessage.RecipientType.TO, receiver)
        val context = Context()
        context.setVariable("verifyCode", verifyCode)
        val body = templateEngine.process("EmailVerify", context) // todo 이름 추가
        message.setText(body,"UTF-8","html")
        javaMailSender.send(message)
    }

}