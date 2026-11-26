package ca.itwebsolutions.hindmarket.platform

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import ca.itwebsolutions.hindmarket.AndroidContextHolder
import ca.itwebsolutions.hindmarket.model.DistrictValidationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resume

actual class PlatformBridge {

    @SuppressLint("MissingPermission")
    actual suspend fun verifyCurrentLocationInDistrict(expectedDistrict: String): DistrictValidationResult {
        val context = AndroidContextHolder.appContext
        val client = LocationServices.getFusedLocationProviderClient(context)

        return suspendCancellableCoroutine { cont ->
            client.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { location ->
                    if (location == null) {
                        cont.resume(
                            DistrictValidationResult(
                                isMatched = false,
                                detectedDistrict = "Unknown",
                                detectedAddress = "",
                                latitude = 0.0,
                                longitude = 0.0,
                                errorMessage = "Turn on Device GPS / Location and try again."
                            )
                        )
                        return@addOnSuccessListener
                    }

                    val isMock = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        location.isMock
                    } else {
                        @Suppress("DEPRECATION")
                        location.isFromMockProvider
                    }
                    if (isMock) {
                        cont.resume(
                            DistrictValidationResult(
                                isMatched = false,
                                detectedDistrict = "Mock GPS",
                                detectedAddress = "",
                                latitude = location.latitude,
                                longitude = location.longitude,
                                errorMessage = "Mock GPS location detected. Disable fake GPS apps."
                            )
                        )
                        return@addOnSuccessListener
                    }

                    try {
                        val geocoder = Geocoder(context, Locale.ENGLISH)
                        @Suppress("DEPRECATION")
                        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        val addr = addresses?.firstOrNull()
                        val subAdmin = addr?.subAdminArea.orEmpty()
                        val locality = addr?.locality.orEmpty()
                        val fullLine = addr?.getAddressLine(0).orEmpty()

                        val matched = subAdmin.contains(expectedDistrict, ignoreCase = true) ||
                            locality.contains(expectedDistrict, ignoreCase = true) ||
                            fullLine.contains(expectedDistrict, ignoreCase = true)

                        cont.resume(
                            DistrictValidationResult(
                                isMatched = matched,
                                detectedDistrict = subAdmin.ifBlank { locality },
                                detectedAddress = fullLine,
                                latitude = location.latitude,
                                longitude = location.longitude,
                                errorMessage = if (matched) null else "You are currently in ${subAdmin.ifBlank { locality }}, not $expectedDistrict."
                            )
                        )
                    } catch (e: Exception) {
                        cont.resume(
                            DistrictValidationResult(
                                isMatched = false,
                                detectedDistrict = "Error",
                                detectedAddress = "",
                                latitude = location.latitude,
                                longitude = location.longitude,
                                errorMessage = "Unable to verify district address: ${e.message}"
                            )
                        )
                    }
                }
                .addOnFailureListener { e ->
                    cont.resume(
                        DistrictValidationResult(
                            isMatched = false,
                            detectedDistrict = "Error",
                            detectedAddress = "",
                            latitude = 0.0,
                            longitude = 0.0,
                            errorMessage = e.message
                        )
                    )
                }
        }
    }

    actual fun openPhoneDialer(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+91$phoneNumber")).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        AndroidContextHolder.appContext.startActivity(intent)
    }

    actual fun openWhatsAppVerification(adminNumber: String, userMobile: String, otpCode: String) {
        val text = Uri.encode("HindMarket Business Verification\nMobile: +91$userMobile\nCode: $otpCode")
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/$adminNumber?text=$text")).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        AndroidContextHolder.appContext.startActivity(intent)
    }

    actual fun openWhatsAppChat(phoneNumber: String, prefillText: String) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://wa.me/91$phoneNumber?text=${Uri.encode(prefillText)}")
        ).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
        AndroidContextHolder.appContext.startActivity(intent)
    }

    actual fun openMapCoordinates(lat: Double, lng: Double, label: String) {
        val uri = Uri.parse("geo:$lat,$lng?q=$lat,$lng(${Uri.encode(label)})")
        val intent = Intent(Intent.ACTION_VIEW, uri).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
        AndroidContextHolder.appContext.startActivity(intent)
    }
}
