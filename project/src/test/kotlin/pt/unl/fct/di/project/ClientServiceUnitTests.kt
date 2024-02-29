package pt.unl.fct.di.project

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import pt.unl.fct.di.project.data.State
import pt.unl.fct.di.project.data.dao.Apartment
import pt.unl.fct.di.project.data.dao.Booking
import pt.unl.fct.di.project.data.dao.Client
import pt.unl.fct.di.project.data.dao.Owner
import pt.unl.fct.di.project.data.repository.BookingRepository
import pt.unl.fct.di.project.data.repository.ClientRepository
import pt.unl.fct.di.project.service.ClientService
import java.security.InvalidKeyException
import java.time.LocalDate
import java.util.*

@ExtendWith(MockitoExtension::class)
class ClientServiceUnitTests {

    @InjectMocks
    private lateinit var clientService: ClientService

    @Mock
    private lateinit var clientRepository: ClientRepository

    @Mock
    private lateinit var bookingRepository: BookingRepository

    @Test
    fun `should get client with bookings`(){
        val clientId = 1L
        val client = Client(clientId, "c1", "iadi.client@gmail.com", "https://cdn2.iconfinder.com/data/icons/rcons-users-color/32/manager_man-256.png", 1, mutableListOf(), mutableListOf())

        `when`(clientRepository.findClientWithBookings(clientId)).thenReturn(Optional.of(client))

        val result = clientService.getClientWithBookings(clientId)

        assertThat(result.bookings).isNotNull
    }

    @Test
    fun `should get client with reviews`() {
        val clientId = 1L
        val client = Client(clientId, "c1", "iadi.client@gmail.com", "https://cdn2.iconfinder.com/data/icons/rcons-users-color/32/manager_man-256.png", 1, mutableListOf(), mutableListOf())

        // Mock the behavior of the repository
        `when`(clientRepository.findClientWithReviews(clientId)).thenReturn(Optional.of(client))

        // Call the service method
        val result = clientService.getClientWithReviews(clientId)

        // Assertions
        assertThat(result.reviews).isNotNull
    }

    @Test
    fun `should check in client`() {
        val clientId = 1L
        val bookingId = 1L
        val ownerId = 1L
        val apartId = 1L
        val owner = Owner(ownerId, "Alex", "alex@example.com", "", 123456789, mutableListOf())
        val client = Client(clientId, "c1", "iadi.client@gmail.com", "", 1, mutableListOf(), mutableListOf())
        val apart =  Apartment(apartId, "Apartment1", "Best Apartment", "Setubal", "Good view", "", 700, owner, mutableListOf(), mutableListOf())
        val booking = Booking(bookingId,LocalDate.now(), LocalDate.now().plusDays(2),State.BOOKED,client,apart, null)


        // Mock the behavior of the repositories
        `when`(clientRepository.findById(clientId)).thenReturn(Optional.of(client))
        `when`(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking))

        // Call the service method
        clientService.checkIn(clientId, bookingId)

        // Assertions
        assertEquals(State.OCCUPIED,booking.state)
    }

    @Test
    fun `should check out client`() {
        val clientId = 1L
        val bookingId = 1L
        val ownerId = 1L
        val apartId = 1L
        val owner = Owner(ownerId, "Alex", "alex@example.com", "", 123456789, mutableListOf())
        val client = Client(clientId, "c1", "iadi.client@gmail.com", "", 1, mutableListOf(), mutableListOf())
        val apart =  Apartment(apartId, "Apartment1", "Best Apartment", "Setubal", "Good view", "", 700, owner, mutableListOf(), mutableListOf())
        val booking = Booking(bookingId,LocalDate.now(), LocalDate.now().plusDays(2),State.OCCUPIED,client,apart, null)


        // Mock the behavior of the repositories
        `when`(clientRepository.findById(clientId)).thenReturn(Optional.of(client))
        `when`(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking))

        // Call the service method
        clientService.checkOut(clientId, bookingId)

        // Assertions
        assertEquals(State.AWAITING_REVIEW,booking.state)
    }

    @Test
    fun `should throw InvalidKeyException when client not found`() {
        val clientId = 1L
        val bookingId = 1L

        // Mock the behavior of the repository
        `when`(clientRepository.findById(clientId)).thenReturn(Optional.empty())

        // Assert
        assertThrows<InvalidKeyException> {
            clientService.checkIn(clientId, bookingId)
        }
    }

    @Test
    fun `should throw InvalidKeyException when booking not found`() {
        val clientId = 1L
        val bookingId = 1L
        val client = Client(clientId, "c1", "iadi.client@gmail.com", "", 1, mutableListOf(), mutableListOf())

        // Mock the behavior of the repository
        `when`(clientRepository.findById(clientId)).thenReturn(Optional.of(client))
        `when`(clientRepository.findById(clientId)).thenReturn(Optional.empty())

        // Assert
        assertThrows<InvalidKeyException> {
            clientService.checkIn(clientId, bookingId)
        }
    }

    @Test
    fun `should throw IllegalArgumentException when booking not in the correct state (BOOKED)`() {
        val clientId = 1L
        val bookingId = 1L
        val ownerId = 1L
        val apartmentId = 1L
        val client = Client(clientId, "c1", "iadi.client@gmail.com", "", 1, mutableListOf(), mutableListOf())
        val owner = Owner(ownerId, "Alex", "alex@example.com", "", 123456789, mutableListOf())
        val apart =  Apartment(apartmentId, "Apartment1", "Best Apartment", "Setubal", "Good view", "", 700, owner, mutableListOf(), mutableListOf())
        val booking = Booking(bookingId,LocalDate.now(), LocalDate.now().plusDays(2),State.REJECTED,client,apart, null)

        // Mock the behavior of the repository
        `when`(clientRepository.findById(clientId)).thenReturn(Optional.of(client))
        `when`(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking))

        // Assert
        assertThrows<IllegalArgumentException> {
            clientService.checkIn(clientId, bookingId)
        }
    }

}