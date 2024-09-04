package nonabili.nonabiliserver.config

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

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/chat/ws")
            .setAllowedOrigins("http://localhost:3020")
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
                    val idxHeader = accessor.getFirstNativeHeader("userIdx")
                    val destination = accessor.destination
                    if (destination != null && idxHeader != null) {
                        if (!destination.endsWith("/$idxHeader")) {
                            throw IllegalArgumentException("topic != userIdx")
                        }
                    }
                }
                return message
            }
        })
    }
}