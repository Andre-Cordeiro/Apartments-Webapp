package pt.unl.fct.di.project.boot

import jakarta.transaction.Transactional
import pt.unl.fct.di.project.data.repository.ClientRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import pt.unl.fct.di.project.data.dao.*
import pt.unl.fct.di.project.data.repository.ApartmentRepository
import pt.unl.fct.di.project.data.repository.BookingRepository
import pt.unl.fct.di.project.data.repository.ManagerRepository
import pt.unl.fct.di.project.data.repository.OwnerRepository



@Component
class Init(val managers : ManagerRepository, val owners: OwnerRepository, val apartments: ApartmentRepository, val clients : ClientRepository, val bookings : BookingRepository) : CommandLineRunner {

    fun addManagers() {
        val m1 = Manager(1, "iadi.noreply@gmail.com", "https://cdn4.iconfinder.com/data/icons/professions-1-2/151/29-512.png", 1)
        val m2 = Manager(2, "iadi.noreply@gmail.com", "https://cdn0.iconfinder.com/data/icons/business-office-and-people-in-flat/256/Businessman-2-512.png", 2)
        val m3 = Manager(3, "iadi.noreply@gmail.com", "https://cdn2.iconfinder.com/data/icons/avatars-2-7/128/3-1024.png", 3)

        managers.saveAll(listOf(m1,m2,m3))
    }

    fun addOwnersAndApartments() {

        val o1 = Owner(1, "Alex", "iadi.owner1@gmail.com", "https://icon-library.com/images/business-owner-icon/business-owner-icon-10.jpg", 1, mutableListOf())
        val o2 = Owner(2, "Bernardo","iadi.owner2@gmail.com", "https://kyprianouarchitects.com/wp-content/uploads/2019/05/businessman-icon-png-12.png", 2,mutableListOf())
        val o3 = Owner(3, "Andre","iadi.owner3@gmail.com", "https://vectorified.com/images/product-owner-icon-23.png", 3, mutableListOf())

        owners.saveAll(listOf(o1,o2,o3))


        val a1 =  Apartment(1, "Oceanfront Paradise", "Luxurious Apartment with Stunning Ocean View", "Setubal", "Private Beach Access", "https://www.livehome3d.com/assets/img/articles/design-house/how-to-design-a-house.jpg", 700, o1, mutableListOf(), mutableListOf())
        val a2 =  Apartment(2, "Downtown Luxury Loft", "Modern Loft in the Heart of Almada", "Almada", "City Center Convenience", "https://thumbor.forbes.com/thumbor/fit-in/900x510/https://www.forbes.com/home-improvement/wp-content/uploads/2022/07/download-23.jpg",500, o1, mutableListOf(), mutableListOf())
        val a3 =  Apartment(3, "Charming Lisbon Studio", "Cozy Studio near Lisbon Center", "Lisboa", "Cultural District Ambiance", "https://images.coolhouseplans.com/plans/80523/80523-b440.jpg",600, o2, mutableListOf(), mutableListOf())
        val a4 =  Apartment(4, "Urban Oasis", "Contemporary Living in Setubal", "Setubal", "Access to Urban Amenities", "https://www.thehousedesigners.com/images/uploads/SiteImage-Landing-contemporary-house-plans-1.webp",850, o2, mutableListOf(), mutableListOf())
        val a5 =  Apartment(5, "Seaside Retreat", "Oceanfront Paradise in Seixal", "Seixal", "Panoramic Ocean Views", "https://cdn.houseplansservices.com/product/mu8beogs519ppvg9sreus54h88/w1024.png?v=12",900, o3, mutableListOf(), mutableListOf())
        val a6 = Apartment(6, "Luxury Penthouse", "Elegant Penthouse with City Skyline", "Porto", "Breathtaking City Views", "https://wallpapers.com/images/hd/beautiful-house-pictures-gi9u23e95gi8iu2e.jpg", 2500, o3, mutableListOf(), mutableListOf())
        val a7 = Apartment(7, "Beachfront Villa", "Spacious Villa by the Ocean in Faro", "Faro", "Private Beach Access", "https://cdn-5.urmy.net/images/plans/HWD/bulk/7526/001-11.jpg", 1800, o1, mutableListOf(), mutableListOf())
        val a8 = Apartment(8, "Riverside Oasis", "Comfortable Riverside Apartment in Coimbra", "Coimbra", "Scenic Riverbank Setting", "https://w0.peakpx.com/wallpaper/665/47/HD-wallpaper-beautiful-house-architecture-beautiful-houses-house-houses.jpg", 1600, o1, mutableListOf(), mutableListOf())
        val a9 = Apartment(9, "Mountain Retreat", "Mountain View Apartment in Aveiro", "Aveiro", "Serene Mountain Scenery", "https://www.newshub.co.nz/home/travel/2018/07/global-competition-reveals-world-s-most-beautiful-houses/_jcr_content/par/image_285388803.dynimg.full.q75.jpg/v1532489183533/18_CATERS_HGTV_ULTIMATE_HOUSE_HUNT_FINALS_20-Windermere+Real+Estate-1120.jpg", 1900, o2, mutableListOf(), mutableListOf())
        val a10 = Apartment(10, "Historic Charm", "Historical Apartment with Classic Architecture", "Lisbon", "Rich Cultural Surroundings", "https://thumbor.forbes.com/thumbor/fit-in/900x510/https://www.forbes.com/home-improvement/wp-content/uploads/2022/07/download-23.jpg", 2200, o1, mutableListOf(), mutableListOf())
        val a11 = Apartment(11, "Modern Urban Living", "Contemporary Apartment in Central Setubal", "Setubal", "Convenient Urban Lifestyle", "https://images.coolhouseplans.com/plans/80523/80523-b440.jpg", 1300, o2, mutableListOf(), mutableListOf())
        val a12 = Apartment(12, "Quaint Coastal Cottage", "Cozy Cottage near Almada Beaches", "Almada", "Proximity to Coastal Attractions", "https://www.thehousedesigners.com/images/uploads/SiteImage-Landing-contemporary-house-plans-1.webp", 1700, o2, mutableListOf(), mutableListOf())
        val a13 = Apartment(13, "Tranquil Garden Hideaway", "Peaceful Apartment with Garden Views in Lisbon", "Lisbon", "Relaxing Garden Retreat", "https://cdn-5.urmy.net/images/plans/HWD/bulk/7526/001-11.jpg", 1400, o3, mutableListOf(), mutableListOf())
        val a14 = Apartment(14, "Sunny Seixal Loft", "Bright Loft with Sunlit Interiors in Seixal", "Seixal", "Sunshine-filled Living Spaces", "https://images.coolhouseplans.com/plans/80523/80523-b440.jpg", 2000, o3, mutableListOf(), mutableListOf())
        val a15 = Apartment(15, "Artistic Loft Space", "Creative Loft in the Heart of Lisbon", "Lisbon", "Inspiring Artistic Ambiance", "https://images.coolhouseplans.com/plans/80523/80523-b440.jpg", 1800, o2, mutableListOf(), mutableListOf())


        o1.apartments.addAll(listOf(a1, a2, a7, a8, a10))
        o2.apartments.addAll(listOf(a3, a4, a9, a11, a12, a15))
        o3.apartments.addAll(listOf(a5, a6, a13, a14))

        apartments.saveAll(listOf(a1, a2, a3, a4, a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15))

    }

    fun addClients() {
        val c1 = Client(1, "Rui", "iadi.client@gmail.com", "https://cdn2.iconfinder.com/data/icons/rcons-users-color/32/manager_man-256.png", 1, mutableListOf(), mutableListOf())
        val c2 = Client(2, "Bruno", "iadi.client2@gmail.com", "https://cdn.iconscout.com/icon/free/png-256/manager-2506834-2130095.png", 2, mutableListOf(), mutableListOf())
        val c3 = Client(3, "Maria", "iadi.client3@gmail.com", "https://www.pngrepo.com/png/122868/180/manager.png", 3, mutableListOf(), mutableListOf())

        clients.saveAll(listOf(c1,c2,c3))
    }


    @Transactional
    override fun run(vararg args: String?) {

        addManagers()
        addOwnersAndApartments()
        addClients()

        println("===Clients===")
        clients.findAll().forEach {
            println(it)
        }

        println("===Owners===")
        owners.findAll().forEach {
            println(it)
        }

        println("===Apartments===")
        apartments.findAll().forEach {
            println(it)
        }
    }
}