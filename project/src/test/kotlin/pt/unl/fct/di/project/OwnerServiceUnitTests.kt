package pt.unl.fct.di.project

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
import pt.unl.fct.di.project.data.repository.OwnerRepository
import pt.unl.fct.di.project.service.OwnerService
import java.security.InvalidKeyException
import java.security.InvalidParameterException
import java.time.LocalDate
import java.util.*

@ExtendWith(MockitoExtension::class)
class OwnerServiceUnitTests {

    @Mock
    private lateinit var bookings: BookingRepository

    @Mock
    private lateinit var owners: OwnerRepository

    @InjectMocks
    private lateinit var ownerService: OwnerService

    @Test
    fun `should get owner with apartments`() {

        val ownerId = 1L
        val owner = Owner(ownerId, "Alex", "iadi.owner1@gmail.com", "", 1, mutableListOf())
        `when`(owners.findOwnerWithAparts(ownerId)).thenReturn(Optional.of(owner))

        val result = ownerService.getOwnerWithApartments(ownerId)
        assertEquals(owner, result)
    }

    @Test
    fun `should throw InvalidKeyException when owner not found`() {
        // Arrange
        val ownerId = 1L
        `when`(owners.findOwnerWithAparts(ownerId)).thenReturn(Optional.empty())

        // Assert
        assertThrows<InvalidKeyException> {
            ownerService.getOwnerWithApartments(ownerId)
        }
    }

    @Test
    fun `should change booking status to ACCEPT`() {
        // Arrange
        val ownerId = 1L
        val bookingId = 1L
        val decision = "ACCEPT"
        val owner = Owner(ownerId, "Alex", "alex@example.com", "", 123456789, mutableListOf())
        val client = Client(10L, "jose", "mail", "url", 112, mutableListOf(), mutableListOf())
        val apartment = Apartment(1L, "Apartment1", "Best Apartment", "Setubal", "Good view", "pic", 700, owner, mutableListOf(), mutableListOf())
        val booking = Booking(bookingId, LocalDate.now(), LocalDate.now().plusDays(2), State.UNDER_CONSIDERATION, client, apartment,null)

        `when`(owners.findById(ownerId)).thenReturn(Optional.of(owner))
        `when`(bookings.findById(bookingId)).thenReturn(Optional.of(booking))

        // Act
        ownerService.changeBookingStatus(ownerId, bookingId, decision)

        // Assert
        assertEquals(State.BOOKED, booking.state)
    }

    @Test
    fun `should change booking status to REJECT`() {
        // Arrange
        val ownerId = 1L
        val bookingId = 1L
        val decision = "REJECT"
        val owner = Owner(ownerId, "Alex", "alex@example.com", "", 123456789, mutableListOf())
        val client = Client(10L, "jose", "mail", "url", 112, mutableListOf(), mutableListOf())
        val apartment = Apartment(1L, "Apartment1", "Best Apartment", "Setubal", "Good view", "pic", 700, owner, mutableListOf(), mutableListOf())
        val booking = Booking(bookingId, LocalDate.now(), LocalDate.now().plusDays(2), State.UNDER_CONSIDERATION, client, apartment,null)

        `when`(owners.findById(ownerId)).thenReturn(Optional.of(owner))
        `when`(bookings.findById(bookingId)).thenReturn(Optional.of(booking))

        // Act
        ownerService.changeBookingStatus(ownerId, bookingId, decision)

        // Assert
        assertEquals(State.REJECTED, booking.state)
    }

    @Test
    fun `should throw InvalidParameterException for unknown decision`() {
        // Arrange
        val ownerId = 1L
        val bookingId = 1L
        val decision = "INVALID"
        val owner = Owner(ownerId, "Alex", "alex@example.com", "", 123456789, mutableListOf())
        val client = Client(10L, "jose", "mail", "url", 112, mutableListOf(), mutableListOf())
        val apartment = Apartment(1L, "Apartment1", "Best Apartment", "Setubal", "Good view", "pic", 700, owner, mutableListOf(), mutableListOf())
        val booking = Booking(bookingId, LocalDate.now(), LocalDate.now().plusDays(2), State.UNDER_CONSIDERATION, client, apartment,null)

        `when`(owners.findById(ownerId)).thenReturn(Optional.of(owner))
        `when`(bookings.findById(bookingId)).thenReturn(Optional.of(booking))

        // Assert
        assertThrows<InvalidParameterException> {
            ownerService.changeBookingStatus(ownerId, bookingId, decision)
        }
    }

    @Test
    fun `should throw InvalidKeyException when owner not found in changeBookingStatus`() {
        // Arrange
        val ownerId = 1L
        val bookingId = 1L
        val decision = "ACCEPT"

        `when`(owners.findById(ownerId)).thenReturn(Optional.empty())

        // Assert
        assertThrows<InvalidKeyException> {
            ownerService.changeBookingStatus(ownerId, bookingId, decision)
        }
    }

    @Test
    fun `should throw InvalidKeyException when booking not found in changeBookingStatus`() {
        // Arrange
        val ownerId = 1L
        val bookingId = 1L
        val decision = "ACCEPT"
        val owner = Owner(ownerId, "Alex", "alex@example.com", "", 123456789, mutableListOf())

        `when`(owners.findById(ownerId)).thenReturn(Optional.of(owner))
        `when`(bookings.findById(bookingId)).thenReturn(Optional.empty())

        // Assert
        assertThrows<InvalidKeyException> {
            ownerService.changeBookingStatus(ownerId, bookingId, decision)
        }
    }
}