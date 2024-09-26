package nonabili.nonabiliserver.config

import nonabili.nonabiliserver.common.util.error.CustomError
import nonabili.nonabiliserver.common.util.error.ErrorState
import nonabili.nonabiliserver.common.util.jwt.JwtProvider
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import java.security.Principal

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig(val tokenProvider: JwtProvider) : WebSocketMessageBrokerConfigurer {
    val log = LoggerFactory.getLogger(javaClass)

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/chat/ws")
            .setAllowedOriginPatterns("*")
            .withSockJS()
    }

    override fun configureMessageBroker(config: org.springframework.messaging.simp.config.MessageBrokerRegistry) {
        config.enableSimpleBroker("/topic")
        config.setApplicationDestinationPrefixes("/app")
    }

    override fun configureClientInboundChannel(registration: org.springframework.messaging.simp.config.ChannelRegistration) {
        registration.interceptors(object : ChannelInterceptor {
            override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
                val accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java)
                if (StompCommand.SUBSCRIBE == accessor?.command) {
                    val token = accessor.getFirstNativeHeader("Authorization") ?: throw CustomError(ErrorState.NOT_FOUND_USER)
                    val userIdx = tokenProvider.getUserIdxByToken(token)
                    log.info(token)
                    log.info(userIdx)
                    val destination = accessor.destination
                    log.info(destination)
                    if (destination != null && userIdx != null) {
                        if (!destination.endsWith("/$userIdx")) {
                            throw CustomError(ErrorState.DIFFERENT_USER)
                            return null;
                        }
                    } else {
                        throw CustomError(ErrorState.WRONG_DESTINATION)
                        return null;
                    }
                }
                return message
            }
        })
    }
}