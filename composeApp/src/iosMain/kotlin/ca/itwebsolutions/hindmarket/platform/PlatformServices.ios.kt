package ca.itwebsolutions.hindmarket.platform

import ca.itwebsolutions.hindmarket.model.DistrictValidationResult
import platform.CoreLocation.CLGeocoder
import platform.CoreLocation.CLLocationManager
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

actual class PlatformBridge {
    private val locationManager = CLLocationManager()

    actual suspend fun verifyCurrentLocationInDistrict(expectedDistrict: String): DistrictValidationResult {
        locationManager.requestWhenInUseAuthorization()
        val loc = locationManager.location ?: return DistrictValidationResult(
            isMatched = false,
            detectedDistrict = "Unknown",
            detectedAddress = "",
            latitude = 0.0,
            longitude = 0.0,
            errorMessage = "Enable Location Services in iOS Settings."
        )

        return suspendCancellableCoroutine { cont ->
            CLGeocoder().reverseGeocodeLocation(loc) { placemarks, _ ->
                val pm = placemarks?.firstOrNull() as? platform.CoreLocation.CLPlacemark
                val subAdmin = pm?.subAdministrativeArea.orEmpty()
                val locality = pm?.locality.orEmpty()
                val matched = subAdmin.contains(expectedDistrict, ignoreCase = true) ||
                    locality.contains(expectedDistrict, ignoreCase = true)
                cont.resume(
                    DistrictValidationResult(
                        isMatched = matched,
                        detectedDistrict = subAdmin.ifBlank { locality },
                        detectedAddress = "$locality, $subAdmin",
                        latitude = 0.0,
                        longitude = 0.0,
                        errorMessage = if (matched) null else "Detected $subAdmin, expected $expectedDistrict."
                    )
                )
            }
        }
    }

    actual fun openPhoneDialer(phoneNumber: String) {
        NSURL.URLWithString("tel://+91$phoneNumber")?.let { UIApplication.sharedApplication.openURL(it) }
    }

    actual fun openWhatsAppVerification(adminNumber: String, userMobile: String, otpCode: String) {
        val url = "https://wa.me/$adminNumber?text=Verify%20$userMobile%20Code:%20$otpCode"
        NSURL.URLWithString(url)?.let { UIApplication.sharedApplication.openURL(it) }
    }

    actual fun openWhatsAppChat(phoneNumber: String, prefillText: String) {
        val url = "https://wa.me/91$phoneNumber"
        NSURL.URLWithString(url)?.let { UIApplication.sharedApplication.openURL(it) }
    }

    actual fun openMapCoordinates(lat: Double, lng: Double, label: String) {
        val url = "http://maps.apple.com/?ll=$lat,$lng"
        NSURL.URLWithString(url)?.let { UIApplication.sharedApplication.openURL(it) }
    }
}
