package com.example.reservationservice

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.messaging.Sink
import org.springframework.context.annotation.Bean
import org.springframework.context.event.EventListener
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.integration.dsl.IntegrationFlows
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.stream.Stream
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@EnableBinding(Sink::class)
@EnableDiscoveryClient
@SpringBootApplication
@EnableConfigurationProperties(FooProperties::class)
class ReservationServiceApplication {

    @Bean
    fun incomingFlow(sink: Sink, rr: ReservationRepository) =
            IntegrationFlows
                    .from(sink.input())
                    .handle({ payload: String, headers: Map<String, Any> ->
                        rr.save(Reservation(reservationName = payload))
                    })
                    .get()

    @Bean
    fun init(rr: ReservationRepository) = ApplicationRunner {
        Stream.of("Josh", "Will", "Kan", "Fabien", "Sebastien", "Clarens", "Arnaud")
                .map { rr.save(Reservation(reservationName = it)) }
                .forEach { println(it) }
    }
}

@Component
class YowHealthIndicator : HealthIndicator {

    override fun health(): Health = Health.status("I <3 YOW!!").build()
}

@Component
class CustomListener(val fooProperties: FooProperties) {

    init {
        println("name is ${fooProperties.name} on startup.")
    }

    @EventListener(RefreshScopeRefreshedEvent::class)
    fun onRefresh(rsre: RefreshScopeRefreshedEvent) = println("saw: ${fooProperties.name}")
}

@RefreshScope
@ConfigurationProperties("foo")
class FooProperties(var name: String? = null)

@RestController
@RefreshScope
class MessageRestController {

    var msg: String? = null

    constructor(@Value("\${message}") msg: String) {
        this.msg = msg
    }

    @GetMapping("/message")
    fun message() = this.msg
}

@RestController
class ReservationRestController(val reservationRepository: ReservationRepository) {

    @GetMapping("/reservations")
    fun reservations() = reservationRepository.findAll()
}

fun main(args: Array<String>) {
    SpringApplication.run(ReservationServiceApplication::class.java, *args)
}


interface ReservationRepository : JpaRepository<Reservation, Long>

@Entity()
class Reservation(@Id @GeneratedValue var id: Long? = null, var reservationName: String? = null) {
    constructor() : this(null, null)
}