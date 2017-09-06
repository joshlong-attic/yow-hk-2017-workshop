package com.example.reservationclient

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.BDDAssertions
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner
import org.springframework.test.context.junit4.SpringRunner

/**
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
@SpringBootTest
@RunWith(SpringRunner::class)
@AutoConfigureStubRunner(workOffline = true, ids = arrayOf("com.example:reservation-service:+:8000"))
//@AutoConfigureWireMock(port = 8000)
//@AutoConfigureJson
class ReservationClientTest {

    @Autowired
    val om: ObjectMapper? = null

    @Autowired
    val reader: ReservationClient? = null

    @Test
    fun testReader() {

/*        val json = om!!.writeValueAsString(
                arrayListOf(Reservation(reservationName = "Foo", id = 1L),
                        Reservation(reservationName = "Bar", id = 2L)))

        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/reservations"))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .withBody(json)))*/

        val reservations = this.reader!!.read()
        val first: Reservation = reservations.iterator().next()
        BDDAssertions.then(reservations.size).isEqualTo(2)
        BDDAssertions.then(first.reservationName).isEqualTo("Foo")
        BDDAssertions.then(first.id).isEqualTo(1L)
    }

}