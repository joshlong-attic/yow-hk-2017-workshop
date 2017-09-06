package com.example.hicf

import org.springframework.boot.SpringApplication
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class HicfApplication

fun main(args: Array<String>) {
    SpringApplication.run(HicfApplication::class.java, *args)
}

@Component
class CustomHealthIndicator : HealthIndicator {

    override fun health(): Health  = Health.status("I <3 Production!!").build()
}

@RestController
class GreetingsRestController {

    @GetMapping("/hi/{name}")
    fun hi(@PathVariable name: String) = "Hello, ${name}"
}