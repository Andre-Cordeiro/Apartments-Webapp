package pt.unl.fct.di.project.config

import org.intellij.lang.annotations.Language
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component("securityRules")
annotation class SecurityRules {

    /*
    ################ APARTMENT RELATED #####################
     */

    @PreAuthorize(CanListApartments.condition)
    annotation class CanListApartments {
        companion object {
            @Language("SpEL")
            const val condition = "true"
        }
    }

    @PreAuthorize(CanCreateApartmentPeriod.condition)
    annotation class CanCreateApartmentPeriod {
        companion object {
            @Language("SpEL")
            const val condition = "hasAuthority('OWNER') AND @securityService.isApartmentOwner(principal, #apartmentId)"
        }
    }

    @PreAuthorize(CanBookApartment.condition)
    annotation class CanBookApartment {
        companion object {
            @Language("SpEL")
            const val condition = "hasAuthority('CLIENT') AND @securityService.isTheOneRequesting(principal, #username)"
        }
    }

    @PreAuthorize(CanGetApartmentPeriods.condition)
    annotation class CanGetApartmentPeriods {
        companion object {
            @Language("SpEL")
            const val condition = "true"
        }
    }

    @PreAuthorize(CanListApartmentAvailablePeriods.condition)
    annotation class CanListApartmentAvailablePeriods {

        companion object {
            @Language("SpEL")
            const val condition = "true"
        }
    }

    @PreAuthorize(CanListApartmentReviews.condition)
    annotation class CanListApartmentReviews {

        companion object {
            @Language("SpEL")
            const val condition = "(hasAuthority('OWNER') AND @securityService.isApartmentOwner(principal, #apartmentId)) OR hasAuthority('CLIENT')"
        }
    }

    /*
    ################ OWNER RELATED #####################
     */

    @PreAuthorize(CanChangeBookingStatus.condition)
    annotation class CanChangeBookingStatus {
        companion object {
            @Language("SpEL")
            const val condition = "(hasAuthority('OWNER') AND @securityService.isTheOneRequesting(principal, #ownerId) AND @securityService.isApartmentOwnerOfBooking(principal, #bookingId)) OR hasAuthority('MANAGER')"
        }
    }

    @PreAuthorize(CanGetOwnerApartments.condition)
    annotation class CanGetOwnerApartments {

        companion object {
            @Language("SpEL")
            const val condition = "hasAuthority('OWNER') AND @securityService.isTheOneRequesting(principal, #ownerId)"
        }
    }

    /*
    ################ CLIENT RELATED #####################
     */

    @PreAuthorize(CanCheckIn.condition)
    annotation class CanCheckIn {
        companion object {
            @Language("SpEL")
            const val condition = "(hasAuthority('CLIENT') AND @securityService.isTheOneRequesting(principal, #username) AND @securityService.isBookingClient(principal, #bookingId)) OR hasAuthority('MANAGER')"
        }
    }

    @PreAuthorize(CanCheckOut.condition)
    annotation class CanCheckOut {
        companion object {
            @Language("SpEL")
            const val condition = "(hasAuthority('CLIENT') AND @securityService.isTheOneRequesting(principal, #username) AND @securityService.isBookingClient(principal, #bookingId)) OR hasAuthority('MANAGER')"
        }
    }

    @PreAuthorize(CanCancel.condition)
    annotation class CanCancel {
        companion object {
            @Language("SpEL")
            const val condition = "(hasAuthority('CLIENT') AND @securityService.isTheOneRequesting(principal, #username) AND @securityService.isBookingClient(principal, #bookingId)) OR hasAuthority('MANAGER')"
        }
    }

    @PreAuthorize(CanGetClientBookings.condition)
    annotation class CanGetClientBookings {
        companion object {
            @Language("SpEL")
            const val condition = "hasAuthority('CLIENT') AND @securityService.isTheOneRequesting(principal, #username)"
        }
    }

    @PreAuthorize(CanGetClientReviews.condition)
    annotation class CanGetClientReviews {
        companion object {
            @Language("SpEL")
            const val condition = "hasAuthority('CLIENT') AND @securityService.isTheOneRequesting(principal, #username)"
        }
    }

    /*
    ################ REVIEW RELATED #####################
     */

    @PreAuthorize(CanCreateReview.condition)
    annotation class CanCreateReview {
        companion object {
            @Language("SpEL")
            const val condition = "hasAuthority('CLIENT') AND @securityService.isTheOneRequesting(principal, #username) AND @securityService.isBookingClient(principal, #bookingId)"
        }
    }

    @PreAuthorize(CanGetAllReviews.condition)
    annotation class CanGetAllReviews {
        companion object {
            @Language("SpEL")
            const val condition = "true"
        }
    }

    @PreAuthorize(CanGetReview.condition)
    annotation class CanGetReview {
        companion object {
            @Language ("SpEL")
            const val condition = "true"
        }
    }

    /*
    ################ BOOKING RELATED #####################
     */

    @PreAuthorize(CanGetBooking.condition)
    annotation class CanGetBooking {
        companion object {
            @Language("SpEL")
            const val condition = "(hasAuthority('OWNER') AND @securityService.isApartmentOwnerOfBooking(principal, #bookingID)) OR (hasAuthority('CLIENT') AND @securityService.isBookingClient(principal, #bookingID))"
        }
    }

    @PreAuthorize(CanGetBookingReview.condition)
    annotation class CanGetBookingReview {
        companion object {
            @Language("SpEL")
            const val condition = "(hasAuthority('OWNER') AND @securityService.isApartmentOwnerOfBooking(principal, #bookingID)) OR (hasAuthority('CLIENT') AND @securityService.isBookingClient(principal, #bookingID))"
        }
    }
}