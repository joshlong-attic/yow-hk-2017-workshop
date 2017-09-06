package com.example.authservice

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import java.util.stream.Stream
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@EnableDiscoveryClient
@SpringBootApplication
@EnableResourceServer
class AuthServiceApplication

fun main(args: Array<String>) {
    SpringApplication.run(AuthServiceApplication::class.java, *args)
}

@RestController
class PrincipalRestController {

    @RequestMapping("/user")
    fun principal(p: Principal) = p
}

@Configuration
@EnableAuthorizationServer
class OAuthConfiguration(val authenticationManager: AuthenticationManager) : AuthorizationServerConfigurerAdapter() {

    override fun configure(clients: ClientDetailsServiceConfigurer) {
        clients
                .inMemory()
                .withClient("html5")
                .scopes("openid")
                .secret("password")
                .authorizedGrantTypes("password", "implicit")
    }

    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
        endpoints.authenticationManager(authenticationManager)
    }
}

@Service
class AccountUserDetailsService(val ar: AccountRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails =
            ar.findByUsername(username)
                    .map({
                        User(it.username, it.password, it.active, it.active, it.active, it.active,
                                AuthorityUtils.createAuthorityList("ROLE_USER"))
                    })
                    .orElseThrow({ UsernameNotFoundException("couldn't find ${username}") })


}

@Component
class AccountInitializer(val ar: AccountRepository) : ApplicationRunner {

    override fun run(args: ApplicationArguments)
            = Stream.of("jlong,spring", "dsyer,cloud", "pwebb,boot")
            .map { it.split(",") }
            .forEach { ar.save(Account(username = it[0], password = it[1], active = true)) }

}

interface AccountRepository : JpaRepository<Account, Long> {
    fun findByUsername(username: String): java.util.Optional<Account>
}

@Entity
class Account(@Id @GeneratedValue
              var id: Long? = null, var username: String? = null, var password: String? = null, var active: Boolean = false)


