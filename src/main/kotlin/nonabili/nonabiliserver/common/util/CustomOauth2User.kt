package nonabili.nonabiliserver.common.util

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User

class CustomOauth2User: OAuth2User {
    override fun getName(): String {
        TODO("Not yet implemented")
    }

    override fun getAttributes(): MutableMap<String, Any> {
        TODO("Not yet implemented")
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        TODO("Not yet implemented")
    }
}