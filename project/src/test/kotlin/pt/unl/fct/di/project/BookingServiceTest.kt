package pt.unl.fct.di.project

import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows

import java.util.Optional
import org.junit.jupiter.api.Test
import org.mockito.junit.jupiter.MockitoExtension
import pt.unl.fct.di.project.data.State
import pt.unl.fct.di.project.data.dao.*
import pt.unl.fct.di.project.data.repository.BookingRepository
import pt.unl.fct.di.project.service.BookingService
import java.security.InvalidKeyException
import java.time.LocalDate


@ExtendWith(MockitoExtension::class)
class BookingServiceTest {

    @Mock
    private lateinit var bookings: BookingRepository

    @InjectMocks
    private lateinit var bookingService: BookingService

    @Test
    fun testGetBookingById() {
        // Arrange
        val bookingId = 1L
        val checkIn = LocalDate.now()
        val checkOut = checkIn.plusDays(2)
        val state = State.UNDER_CONSIDERATION
        val owner = Owner(1L, "Alexaaaa", "iadi.owner1@gmail.com", "jpg", 1, mutableListOf())
        val apartment = Apartment(8L, "Apartment1", "Best Apartment", "Setubal", "Good view", "pic", 700, owner, mutableListOf(), mutableListOf())
        val client = Client(10L, "jose", "mail", "url", 112, mutableListOf(), mutableListOf())
        val booking = Booking(bookingId,checkIn, checkOut, state, client, apartment,null)

        Mockito.`when`(bookings.findById(bookingId))
                .thenReturn(Optional.of(booking))

        // Act
        val result = bookingService.getBooking(bookingId)

        // Assert
        assertEquals(booking, result)
    }

    @Test
    fun testGetBookingByIdNotFound() {
        // Arrange
        val bookingId = 2L
        val checkIn = LocalDate.now()
        val checkOut = checkIn.plusDays(2)
        val state = State.UNDER_CONSIDERATION
        val owner = Owner(1L, "Alexaaaa", "iadi.owner1@gmail.com", "jpg", 1, mutableListOf())
        val apartment = Apartment(8L, "Apartment1", "Best Apartment", "Setubal", "Good view", "pic", 700, owner, mutableListOf(), mutableListOf())
        val client = Client(10L, "jose", "mail", "url", 112, mutableListOf(), mutableListOf())
        val booking = Booking(bookingId,checkIn, checkOut, state, client, apartment,null)

        Mockito.`when`(bookings.findById(bookingId))
                .thenReturn(Optional.empty())

        // Act and Assert
        assertThrows(InvalidKeyException::class.java) {
            bookingService.getBooking(bookingId)
        }
    }
}