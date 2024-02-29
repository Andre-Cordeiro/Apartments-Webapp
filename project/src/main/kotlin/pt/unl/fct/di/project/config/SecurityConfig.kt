package pt.unl.fct.di.project.config


import pt.unl.fct.di.project.data.repository.PersonRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.stereotype.Service

@EnableWebSecurity
@Configuration
@EnableMethodSecurity(prePostEnabled=true)
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity, authenticationManager: AuthenticationManager): SecurityFilterChain {
        http {
            csrf { disable() }
            sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }
            authorizeHttpRequests {
                authorize("/owners", hasAnyAuthority("OWNER", "MANAGER"))
                authorize("/clients", hasAnyAuthority("CLIENT", "MANAGER"))
                authorize("/periods", permitAll)
                authorize("/apartments", permitAll)
                authorize("/bookings", hasAnyAuthority("OWNER", "CLIENT"))
                authorize("/reviews", permitAll)
                authorize("/v3/api-docs", permitAll)
                authorize("/swagger-ui/**", permitAll)
                authorize("/console/**", permitAll)
                authorize("/login", permitAll)
                authorize(anyRequest, permitAll)
            }

            addFilterBefore<BasicAuthenticationFilter>(
                UserPasswordAuthenticationFilterToJWT(
                    "/login",
                    authenticationManager
                )
            )
            addFilterBefore<BasicAuthenticationFilter>(JWTAuthenticationFilter())
        }
        return http.build()
    }

    @Bean
    fun authenticationManager(authConfiguration: AuthenticationConfiguration): AuthenticationManager? {
        return authConfiguration.authenticationManager
    }

    @Bean
    fun encoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Service
    class UserDetailsServiceImpl(val person: PersonRepository, val bcrypt:PasswordEncoder) : UserDetailsService {
        override fun loadUserByUsername(username: String): UserDetails {
            val person = person.findById(username.toLong()).get()
            println("------------------------------")
            println("$$$$$$$$$$ ID USER: " + person.username)
            println("$$$$$$$$$$ ROLE: " + person.role.toString())
            println("------------------------------")
            println("$$$$$$$$$$ PASSWORD: " + person.password)
            println("------------------------------")
            println("$$$$$$$$$$ ENCODED PASSWORD: " + bcrypt.encode(person.password))

            return User.builder()
                .username(person.username.toString())
                .password(bcrypt.encode(person.password))
                .roles(person.role.toString())
                .build()
        }
    }
}