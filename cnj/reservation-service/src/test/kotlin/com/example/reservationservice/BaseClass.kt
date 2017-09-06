//package com.example.reservationservice
//
//import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc
//import org.junit.Before
//import org.junit.runner.RunWith
//import org.mockito.Mockito
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.boot.test.mock.mockito.MockBean
//import org.springframework.test.context.junit4.SpringRunner
//
///**
// * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
// */
//@SpringBootTest
//@RunWith(SpringRunner::class)
//class BaseClass {
//
//    @MockBean
//    val repository: ReservationRepository? = null
//
//    @Autowired
//    val reservationRestController: ReservationRestController? = null
//
//    @Before
//    fun before() {
//
//        RestAssuredMockMvc.standaloneSetup(this.reservationRestController)
//
//        Mockito.`when`(this.repository!!.findAll())
//                .thenReturn(arrayListOf(Reservation(id = 1, reservationName = "Foo"),
//                        Reservation(id = 2, reservationName = "Bar")))
//
//    }
//}