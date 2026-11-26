package ca.itwebsolutions.hindmarket.platform

import ca.itwebsolutions.hindmarket.model.DistrictValidationResult

expect class PlatformBridge() {
    suspend fun verifyCurrentLocationInDistrict(expectedDistrict: String): DistrictValidationResult
    fun openPhoneDialer(phoneNumber: String)
    fun openWhatsAppVerification(adminNumber: String, userMobile: String, otpCode: String)
    fun openWhatsAppChat(phoneNumber: String, prefillText: String)
    fun openMapCoordinates(lat: Double, lng: Double, label: String)
}
