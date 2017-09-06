package com.example.reservationclient

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.netflix.feign.EnableFeignClients
import org.springframework.cloud.netflix.feign.FeignClient
import org.springframework.cloud.netflix.zuul.EnableZuulProxy
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.messaging.Source
import org.springframework.context.annotation.Bean
import org.springframework.messaging.support.MessageBuilder
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.web.bind.annotation.*

@EnableResourceServer
@EnableBinding(Source::class)
@EnableCircuitBreaker
@EnableFeignClients
@EnableDiscoveryClient
@EnableZuulProxy
@SpringBootApplication
class ReservationClientApplication {

    @Bean
    fun init(dc: DiscoveryClient) = ApplicationRunner {
        dc.getInstances("reservation-service")
                .forEach { println("${it.serviceId}:${it.host}:${it.port}") }
    }
/*
    @Bean
    @LoadBalanced
    fun rt() = RestTemplate()
    */
}

@RestController
@RequestMapping("/reservations")
class ReservationApiAdapterRestController(val rc: ReservationClient,
                                          val src: Source) {

    val channel = src.output()

    fun fallback(): List<String?> = ArrayList<String?>()

    @HystrixCommand(fallbackMethod = "fallback")
    @GetMapping("/names")
    fun names(): List<String?> = rc.read().map { it.reservationName }

    @PostMapping
    fun write(@RequestBody r: Reservation) = this.channel.send(MessageBuilder.withPayload(r.reservationName).build())
}

/*
@Component
class ReservationClient(@LoadBalanced val rt: RestTemplate) {

    fun read(): Collection<Reservation> =
            rt.exchange("http://reservation-service/reservations", HttpMethod.GET, null,
                    object : ParameterizedTypeReference<Collection<Reservation>>() {})
                    .body

}
*/

@FeignClient("reservation-service")
interface ReservationClient {

    @GetMapping("/reservations")
    fun read(): Collection<Reservation>
}

class Reservation(var id: Long? = null, var reservationName: String? = null)

/*
@Component
class CustomZuulFilter : ZuulFilter() {

    override fun run(): Any? = {
        val ctx = RequestContext.getCurrentContext()
        println("proxying a request to ${ctx.request.requestURI}..")
        null
    }

    override fun shouldFilter(): Boolean = true
    override fun filterType(): String = "pre"
    override fun filterOrder(): Int = -100
}
*/

fun main(args: Array<String>) {
    SpringApplication.run(ReservationClientApplication::class.java, *args)
}
