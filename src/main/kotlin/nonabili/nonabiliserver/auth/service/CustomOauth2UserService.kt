package nonabili.nonabiliserver.auth.service

import nonabili.nonabiliserver.common.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

@Service
class CustomOauth2UserService(): DefaultOAuth2UserService() {
    val log = LoggerFactory.getLogger(javaClass)
    override fun loadUser(userRequest: OAuth2UserRequest?): OAuth2User {
        val oAuth2User = super.loadUser(userRequest)
        return oAuth2User
    }
}