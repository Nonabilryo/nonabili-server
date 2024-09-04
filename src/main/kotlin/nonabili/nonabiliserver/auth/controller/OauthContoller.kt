package nonabili.nonabiliserver.auth.controller

import nonabili.nonabiliserver.common.util.ResponseFormat
import nonabili.nonabiliserver.common.util.ResponseFormatBuilder
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import java.security.Principal

@Controller
class OauthContoller {
    @GetMapping("/favicon.ico")
    @ResponseBody
    fun returnNoFavicon(): String {
        return ""
    }
}