package ca.itwebsolutions.hindmarket.model

import kotlinx.serialization.Serializable

@Serializable
data class AppAnnouncement(
    val id: String,
    val title: String,
    val message: String,
    val type: String = "TIP"
)

@Serializable
data class IndustryCategory(
    val id: String,
    val name: String,
    val hindiName: String,
    val iconKey: String,
    val itemCount: Int
)

@Serializable
data class FamousProductDetail(
    val id: String,
    val title: String,
    val categoryId: String,
    val city: String,
    val district: String,
    val state: String,
    val hasGiTag: Boolean = false,
    val historicalOrigin: String,
    val whyItIsFamous: String,
    val priceRangeInr: String,
    val peakSeason: String,
    val qualityChecklist: List<String>,
    val verifiedBusinesses: List<BusinessListing>
)

@Serializable
data class BusinessListing(
    val id: String,
    val businessName: String,
    val ownerName: String,
    val categoryId: String,
    val specialtyTitle: String,
    val state: String,
    val district: String,
    val fullAddress: String,
    val pincode: String,
    val latitude: Double,
    val longitude: Double,
    val mobileNumber: String,
    val isMobileVerified: Boolean,
    val gstOrUdyamNumber: String = "",
    val yearsInBusiness: Int = 1,
    val description: String,
    val isWholesaleAvailable: Boolean = true
)

data class DistrictValidationResult(
    val isMatched: Boolean,
    val detectedDistrict: String,
    val detectedAddress: String,
    val latitude: Double,
    val longitude: Double,
    val errorMessage: String? = null
)

enum class FontScalePreset(val label: String, val multiplier: Float) {
    NORMAL("Standard", 1.0f),
    LARGE("Large (Seniors)", 1.22f),
    EXTRA_LARGE("Extra Large", 1.40f)
}
