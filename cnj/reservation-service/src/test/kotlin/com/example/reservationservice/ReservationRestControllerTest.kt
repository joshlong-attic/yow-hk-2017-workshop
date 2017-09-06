package com.example.reservationservice

/*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

/**
 * @author [Josh Long](mailto:josh@joshlong.com)
 */
@SpringBootTest
@RunWith(SpringRunner::class)
@AutoConfigureMockMvc
class ReservationRestControllerTest {

    @Autowired
    var mockMvc: MockMvc? = null

    @MockBean
    val repository: ReservationRepository? = null

    @Test
    fun reservations() {

        Mockito.`when`(this.repository!!.findAll())
                .thenReturn(arrayListOf(Reservation(id = 1, reservationName = "Foo"),
                        Reservation(id = 2, reservationName = "Bar")))

        this.mockMvc!!.perform(get("/reservations"))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("@.[0].id").value(1))
                .andExpect(jsonPath("@.[0].reservationName").value("Foo"))

    }

}
*/