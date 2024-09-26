package nonabili.nonabiliserver.auth.config

import nonabili.nonabiliserver.auth.filter.TokenFilter
import nonabili.nonabiliserver.auth.service.CustomOauth2UserService
import nonabili.nonabiliserver.auth.handler.OauthSuccessHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(val customOauth2UserService: CustomOauth2UserService, val oauthSuccessHandler: OauthSuccessHandler, val tokenFilter: TokenFilter) {
    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        return http
            .formLogin { it.disable() }
            .csrf { it.disable() }
            .cors { it.disable() }
            .authorizeHttpRequests {
                it
                        .requestMatchers("/sso/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/article/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/follow/**").permitAll()
                        .requestMatchers("/chat/**").permitAll()
                        .requestMatchers("/swagger-ui.html", "/api-docs/**", "/swagger-ui/**", "/v1/**", "/v2/**", "/v3/**").permitAll()
                        .anyRequest().authenticated()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .oauth2Login {
                it
                    .successHandler(oauthSuccessHandler)
                    .authorizationEndpoint { it.baseUri("/sso/login") }
                    .userInfoEndpoint { it.userService(customOauth2UserService) }

            }
            .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }
}