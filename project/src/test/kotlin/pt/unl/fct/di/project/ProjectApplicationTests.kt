package pt.unl.fct.di.project

import com.fasterxml.jackson.databind.ObjectMapper
import pt.unl.fct.di.project.presentation.dto.ClientInfoDTO
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import pt.unl.fct.di.project.presentation.dto.*
import java.time.LocalDate
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.test.web.servlet.*


@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ContextConfiguration
class ProjectApplicationTests {

	@Autowired
	lateinit var mvc: MockMvc

	@Autowired
	private lateinit var mapper: ObjectMapper

	@Autowired
	private lateinit var context: WebApplicationContext


	private val apartmentsAPI = "/apartments"
	private val clientsAPI = "/clients"
	private val bookingsAPI = "/bookings"
	private val ownersAPI = "/owners"
	private val reviewsAPI = "/reviews"


	@BeforeEach
	fun setup() {
		mvc = MockMvcBuilders
			.webAppContextSetup(context)
			.build()
	}



	@Nested
	@TestMethodOrder(OrderAnnotation::class)
	inner class NormalWorkflow {

		@Test
		@Order(1)
		@WithMockUser(username="4",roles= ["OWNER"])
		fun `should get owner apartments` () {
			val mockUser = SecurityContextHolder.getContext().authentication.principal as User

			val ap1 = ApartmentInfoDTO(1,"Apartment1", "Best Apartment", "Setubal", "Good view", "https://www.livehome3d.com/assets/img/articles/design-house/how-to-design-a-house.jpg", 700)
			val ap2 = ApartmentInfoDTO(2,"Apartment2", "Best Apartment2", "Almada", "Good view", "https://thumbor.forbes.com/thumbor/fit-in/900x510/https://www.forbes.com/home-improvement/wp-content/uploads/2022/07/download-23.jpg",500)
			val res = listOf(ap1, ap2)

			mvc.get("$ownersAPI/${mockUser.username}/apartments") {
			}.andExpect {
				status { isOk() }
				content { contentType(MediaType.APPLICATION_JSON) }
				content { json(mapper.writeValueAsString(res)) }
			}
		}

		@Test
		@Order(2)
		@WithMockUser(username="7",roles= ["CLIENT"])
		fun `should get apartments` () {

			val a1 =  ApartmentInfoDTO(1, "Apartment1", "Best Apartment", "Setubal", "Good view", "https://www.livehome3d.com/assets/img/articles/design-house/how-to-design-a-house.jpg", 700)
			val a2 =  ApartmentInfoDTO(2, "Apartment2", "Best Apartment2", "Almada", "Good view", "https://thumbor.forbes.com/thumbor/fit-in/900x510/https://www.forbes.com/home-improvement/wp-content/uploads/2022/07/download-23.jpg",500)
			val a3 =  ApartmentInfoDTO(3, "Apartment3", "Best Apartment3", "Lisboa", "Good view", "https://images.coolhouseplans.com/plans/80523/80523-b440.jpg",600)
			val a4 =  ApartmentInfoDTO(4, "Apartment4", "Best Apartment4", "Setubal", "Good view", "https://www.thehousedesigners.com/images/uploads/SiteImage-Landing-contemporary-house-plans-1.webp",850)
			val a5 =  ApartmentInfoDTO(5, "Apartment5", "Best Apartment5", "Seixal", "Good view", "https://cdn.houseplansservices.com/product/mu8beogs519ppvg9sreus54h88/w1024.png?v=12",900)

			val res = listOf(a1, a2, a3, a4, a5)

			mvc.get("$apartmentsAPI/") {

			}.andExpect {
				status { isOk() }
				content { contentType(MediaType.APPLICATION_JSON) }
				content { json(mapper.writeValueAsString(res)) }
			}
		}

		@Test
		@Order(3)
		@WithMockUser(username="4",roles= ["OWNER"])
		fun `should create a period` () {

			val startDate = LocalDate.now()
			val period = PeriodCreateDTO(startDate, startDate.plusDays(2))

			mvc.post("$apartmentsAPI/1/period") {
				accept = MediaType.APPLICATION_JSON
				contentType = MediaType.APPLICATION_JSON
				content = mapper.writeValueAsString(period)
			}.andExpect {
				status { isOk() }
			}
		}

		@Test
		@Order(4)
		@WithMockUser(username="7",roles= ["CLIENT"])
		fun `should get apartment available periods` () {

			val startDate = LocalDate.now()
			val p1 = ApartAvailablePeriodsDTO(ApartmentInfoDTO(1, "Apartment1", "Best Apartment", "Setubal", "Good view","https://www.livehome3d.com/assets/img/articles/design-house/how-to-design-a-house.jpg", 700), startDate)
			val p2 = ApartAvailablePeriodsDTO(ApartmentInfoDTO(1, "Apartment1", "Best Apartment", "Setubal", "Good view","https://www.livehome3d.com/assets/img/articles/design-house/how-to-design-a-house.jpg", 700), startDate.plusDays(1))
			val p3 = ApartAvailablePeriodsDTO(ApartmentInfoDTO(1, "Apartment1", "Best Apartment", "Setubal", "Good view","https://www.livehome3d.com/assets/img/articles/design-house/how-to-design-a-house.jpg", 700), startDate.plusDays(2))

			val res = listOf(p1,p2,p3)

			mvc.get("$apartmentsAPI/1/availablePeriods") {

			}.andExpect {
				status { isOk() }
				content { contentType(MediaType.APPLICATION_JSON) }
				content { json(mapper.writeValueAsString(res)) }
			}
		}


		@Test
		@Order(5)
		@WithMockUser(username="7",roles= ["CLIENT"])
		fun `should book apartment` () {
			val mockUser = SecurityContextHolder.getContext().authentication.principal as User

			val checkIn = LocalDate.now()
			val checkOut = checkIn.plusDays(2)
			val booking = BookApartmentDTO(checkIn, checkOut, 3)
			val bookingResult = BookingInfoDTO(1, checkIn, checkOut, 3, "UNDER_CONSIDERATION", ApartmentInfoDTO(1, "Apartment1", "Best Apartment", "Setubal", "Good view", "https://www.livehome3d.com/assets/img/articles/design-house/how-to-design-a-house.jpg", 700))

			mvc.post("$apartmentsAPI/1/booking/${mockUser.username}") {
				accept = MediaType.APPLICATION_JSON
				contentType = MediaType.APPLICATION_JSON
				content = mapper.writeValueAsString(booking)
			}.andExpect {
				status { isOk() }
				content { contentType(MediaType.APPLICATION_JSON) }
				content { json(mapper.writeValueAsString(bookingResult)) }
			}
		}


		@Test
		@Order(6)
		@WithMockUser(username="7",roles= ["CLIENT"])
		fun `should get Client Bookings` () {
			val mockUser = SecurityContextHolder.getContext().authentication.principal as User

			val checkIn = LocalDate.now()
			val checkOut = checkIn.plusDays(2)
			val b1 = ClientBookingsDTO("c1", "iadi.client@gmail.com", 1, BookingInfoDTO(checkIn, checkOut, ApartmentInfoDTO("Apartment1", "Best Apartment", "Setubal", "Good view", "https://www.livehome3d.com/assets/img/articles/design-house/how-to-design-a-house.jpg", 700)))

			val res = listOf(b1)

			mvc.get("$clientsAPI/${mockUser.username}/bookings") {

			}.andExpect {
				status { isOk() }
				content { contentType(MediaType.APPLICATION_JSON) }
				content { json(mapper.writeValueAsString(res)) }
			}
		}

		@Test
		@Order(7)
		@WithMockUser(username="7",roles= ["CLIENT"])
		fun `should get booking` () {

			val checkIn = LocalDate.now()
			val checkOut = checkIn.plusDays(2)
			val bookingResult = BookingInfoDTO(checkIn, checkOut, ApartmentInfoDTO("Apartment1", "Best Apartment", "Setubal", "Good view", "https://www.livehome3d.com/assets/img/articles/design-house/how-to-design-a-house.jpg", 700))

			mvc.get("$bookingsAPI/1") {
			}.andExpect {
				status { isOk() }
				content { contentType(MediaType.APPLICATION_JSON) }
				content { json(mapper.writeValueAsString(bookingResult)) }
			}
		}


		@Test
		@Order(8)
		@WithMockUser(username="4",roles= ["OWNER"])
		fun `test accept booking` () {
			val mockUser = SecurityContextHolder.getContext().authentication.principal as User

			mvc.put("$ownersAPI/${mockUser.username}/bookings/1") {
				accept = MediaType.APPLICATION_JSON
				contentType = MediaType.APPLICATION_JSON
				content = "Accept"
			}.andExpect {
				status { isOk() }
			}
		}

		@Test
		@Order(9)
		@WithMockUser(username="7",roles= ["CLIENT"])
		fun `should checkIn` () {
			val mockUser = SecurityContextHolder.getContext().authentication.principal as User
			mvc.put("$clientsAPI/${mockUser.username}/checkIn/1") {
			}.andExpect {
				status { isOk() }
			}
		}

		@Test
		@Order(10)
		@WithMockUser(username="7",roles= ["CLIENT"])
		fun `should checkOut` () {
			val mockUser = SecurityContextHolder.getContext().authentication.principal as User
			mvc.put("$clientsAPI/${mockUser.username}/checkOut/1") {
			}.andExpect {
				status { isOk() }
			}
		}

		@Test
		@Order(11)
		@WithMockUser(username="7",roles= ["CLIENT"])
		fun `should create review` () {
			val mockUser = SecurityContextHolder.getContext().authentication.principal as User
			val review = ReviewInfoDTO(mockUser.username, "good", 4)
			mvc.post("$reviewsAPI/${mockUser.username}/review/1") {
				accept = MediaType.APPLICATION_JSON
				contentType = MediaType.APPLICATION_JSON
				content = mapper.writeValueAsString(review)
			}.andExpect {
				status { isOk() }
			}
		}

		@Test
		@Order(12)
		@WithMockUser(username="7",roles= ["CLIENT"])
		fun `should get review` () {

			val reviewResult = ReviewInfoDTO("good", 4, ClientInfoDTO("c1", "iadi.client@gmail.com", 1))

			mvc.get("$reviewsAPI/1") {
				}.andExpect {
					status { isOk() }
					content { contentType(MediaType.APPLICATION_JSON) }
					content { json(mapper.writeValueAsString(reviewResult)) }
				}
		}

		@Test
		@Order(13)
		@WithMockUser(username="7",roles= ["CLIENT"])
		fun `should get all review` () {

			val reviewResult1 = ReviewInfoDTO("mockuser","good", 4)
			val res = listOf(reviewResult1)

			mvc.get("$reviewsAPI/") {
				}.andExpect {
					status { isOk() }
					content { contentType(MediaType.APPLICATION_JSON) }
					content { json(mapper.writeValueAsString(res)) }
				}
		}

		@Test
		@Order(14)
		@WithMockUser(username="7",roles= ["CLIENT"])
		fun `should get Client reviews` () {
			val mockUser = SecurityContextHolder.getContext().authentication.principal as User
			val reviewResult1 = ReviewInfoDTO(mockUser.username, "good", 4)
			val res = listOf(reviewResult1)

			mvc.get("$clientsAPI/${mockUser.username}/reviews") {
			}.andExpect {
				status { isOk() }
				content { contentType(MediaType.APPLICATION_JSON) }
				content { json(mapper.writeValueAsString(res)) }
			}
		}
	}
}

