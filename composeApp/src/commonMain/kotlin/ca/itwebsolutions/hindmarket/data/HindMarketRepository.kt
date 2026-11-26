package ca.itwebsolutions.hindmarket.data

import ca.itwebsolutions.hindmarket.model.*

object HindMarketRepository {

    val stateDistrictMap: Map<String, List<String>> = mapOf(
        "Punjab" to listOf("Bathinda", "Ludhiana", "Amritsar", "Patiala", "Jalandhar", "Moga"),
        "Rajasthan" to listOf("Ajmer", "Jaipur", "Bikaner", "Jodhpur", "Udaipur", "Kota"),
        "Uttar Pradesh" to listOf("Agra", "Varanasi", "Kanpur", "Moradabad", "Aligarh", "Lucknow"),
        "Gujarat" to listOf("Surat", "Rajkot", "Ahmedabad", "Kutch", "Jamnagar"),
        "Maharashtra" to listOf("Kolhapur", "Nagpur", "Nashik", "Pune", "Solapur"),
        "Tamil Nadu" to listOf("Kanchipuram", "Coimbatore", "Madurai", "Tiruppur", "Salem")
    )

    val announcements = listOf(
        AppAnnouncement(
            id = "tip_1",
            title = "Buyer Tip: Check GI Tag ^& District Seal",
            message = "Verified sellers on HindMarket have GPS-locked district addresses so you buy directly from the true origin hub.",
            type = "TIP"
        ),
        AppAnnouncement(
            id = "tip_2",
            title = "Free Business Listing Open",
            message = "Manufacturers ^& artisans can verify via WhatsApp at zero cost and get featured in their district directory.",
            type = "OFFER"
        )
    )

    val categories = listOf(
        IndustryCategory("textiles", "Textiles ^& Handloom", "कपड़ा और हथकरघा", "textile", 42),
        IndustryCategory("food", "Sweets ^& Spices", "मिठाई और मसाले", "food", 68),
        IndustryCategory("handicrafts", "Handicrafts ^& Brass", "हस्तशिल्प", "craft", 35),
        IndustryCategory("agri", "Agri ^& Dairy", "कृषि और डेयरी", "agri", 51),
        IndustryCategory("tools", "Machinery ^& Tools", "मशीनरी और औज़ार", "tools", 29),
        IndustryCategory("leather", "Footwear ^& Leather", "जूते और चमड़ा", "leather", 24)
    )

    val initialSpecialties = mutableListOf(
        FamousProductDetail(
            id = "patiala_phulkari",
            title = "Traditional Phulkari Embroidery",
            categoryId = "textiles",
            city = "Patiala",
            district = "Patiala",
            state = "Punjab",
            hasGiTag = true,
            historicalOrigin = "Practiced across Punjab since the 15th century, Patiala remains the commercial and artisan nerve center for authentic Bagh and Chope Phulkari.",
            whyItIsFamous = "Dense floss-silk darning stitch worked from the reverse side of handspun khaddar or pure georgette.",
            priceRangeInr = "₹1,200 – ₹18,000 per piece",
            peakSeason = "October – March (Wedding ^& Festive Season)",
            qualityChecklist = listOf(
                "Inspect reverse side for neat, unbroken silk thread floats",
                "Verify pure pat (untwisted silk) sheen vs synthetic polyester",
                "Check GI Tag weaver cooperative mark"
            ),
            verifiedBusinesses = listOf(
                BusinessListing(
                    id = "biz_1",
                    businessName = "Royal Patiala Phulkari House",
                    ownerName = "Gurpreet Singh",
                    categoryId = "textiles",
                    specialtyTitle = "Traditional Phulkari Embroidery",
                    state = "Punjab",
                    district = "Patiala",
                    fullAddress = "Shop 14, Adalat Bazar, Patiala",
                    pincode = "147001",
                    latitude = 30.3267,
                    longitude = 76.4003,
                    mobileNumber = "9814012345",
                    isMobileVerified = true,
                    gstOrUdyamNumber = "UDYAM-PB-17-0012451",
                    yearsInBusiness = 28,
                    description = "Direct hand-embroidered bridal Bagh dupattas and suits crafted by 120+ rural women artisans."
                )
            )
        ),
        FamousProductDetail(
            id = "bikaner_bhujia",
            title = "Bikaneri Bhujia ^& Namkeen",
            categoryId = "food",
            city = "Bikaner",
            district = "Bikaner",
            state = "Rajasthan",
            hasGiTag = true,
            historicalOrigin = "First prepared in 1877 during the reign of Maharaja Dungar Singh using local moth bean flour.",
            whyItIsFamous = "Bikaner's arid climate and local groundwater mineral profile give moth-dal bhujia its signature crispness.",
            priceRangeInr = "₹220 – ₹340 per kg",
            peakSeason = "All Year (Peak during Diwali ^& Holi)",
            qualityChecklist = listOf(
                "Minimum 45% Moth Bean (Moth Dal) flour content",
                "Crisp snap without excess residual oil",
                "FSSAI ^& GI Geographical Origin label"
            ),
            verifiedBusinesses = listOf(
                BusinessListing(
                    id = "biz_2",
                    businessName = "Marudhar Heritage Namkeen",
                    ownerName = "Rameshwar Lal",
                    categoryId = "food",
                    specialtyTitle = "Bikaneri Bhujia ^& Namkeen",
                    state = "Rajasthan",
                    district = "Bikaner",
                    fullAddress = "Kote Gate Road, Bikaner",
                    pincode = "334001",
                    latitude = 28.0185,
                    longitude = 73.3149,
                    mobileNumber = "9414112345",
                    isMobileVerified = true,
                    gstOrUdyamNumber = "08AAACH1234F1Z5",
                    yearsInBusiness = 42,
                    description = "Traditional wood-fired kadhai batch roasted Bikaneri Bhujia, Papad, and Rasgulla."
                )
            )
        ),
        FamousProductDetail(
            id = "ludhiana_knitwear",
            title = "Winter Woollens ^& Hosiery Knitwear",
            categoryId = "textiles",
            city = "Ludhiana",
            district = "Ludhiana",
            state = "Punjab",
            hasGiTag = false,
            historicalOrigin = "Known as the Manchester of India, producing over 90% of India's domestic woollen knitwear.",
            whyItIsFamous = "Complete vertical cluster from yarn spinning to computerized flat-knitting and export finishing.",
            priceRangeInr = "₹250 – ₹2,500 (Wholesale MOQ 20 pcs)",
            peakSeason = "August – January",
            qualityChecklist = listOf(
                "Check Woolmark or acrylic-wool blend GSM weight",
                "Inspect linked shoulder seams for zero unraveling",
                "Confirm anti-pilling enzyme wash treatment"
            ),
            verifiedBusinesses = listOf(
                BusinessListing(
                    id = "biz_3",
                    businessName = "Sutlej Knitwear Mills",
                    ownerName = "Harjinder Kaur",
                    categoryId = "textiles",
                    specialtyTitle = "Winter Woollens ^& Hosiery Knitwear",
                    state = "Punjab",
                    district = "Ludhiana",
                    fullAddress = "Industrial Area-A, Ludhiana",
                    pincode = "141003",
                    latitude = 30.9010,
                    longitude = 75.8573,
                    mobileNumber = "9815512345",
                    isMobileVerified = true,
                    gstOrUdyamNumber = "03AABCS5678K1Z2",
                    yearsInBusiness = 19,
                    description = "Manufacturer of merino sweaters, thermal innerwear, and school uniform cardigans."
                )
            )
        ),
        FamousProductDetail(
            id = "ajmer_rose",
            title = "Pushkar Rose Gulkand ^& Ittar",
            categoryId = "agri",
            city = "Pushkar",
            district = "Ajmer",
            state = "Rajasthan",
            hasGiTag = false,
            historicalOrigin = "Cultivated around Pushkar valley for centuries using Damask rose (Rosa damascena) varieties.",
            whyItIsFamous = "High essential oil concentration in Pushkar roses yields aromatic sun-cooked Gulkand and pure rose water.",
            priceRangeInr = "₹180 – ₹650 per jar",
            peakSeason = "November – April",
            qualityChecklist = listOf(
                "Sun-cooked for 30+ days rather than boiled with glucose syrup",
                "Zero artificial red dye or synthetic rose essence",
                "Harvested from Chaitri Gulab petals"
            ),
            verifiedBusinesses = listOf(
                BusinessListing(
                    id = "biz_4",
                    businessName = "Pushkar Valley Rose Farmers Co-op",
                    ownerName = "Mahendra Singh",
                    categoryId = "agri",
                    specialtyTitle = "Pushkar Rose Gulkand ^& Ittar",
                    state = "Rajasthan",
                    district = "Ajmer",
                    fullAddress = "Ganahera Road, Pushkar, Ajmer",
                    pincode = "305022",
                    latitude = 26.4897,
                    longitude = 74.5511,
                    mobileNumber = "9413312345",
                    isMobileVerified = true,
                    gstOrUdyamNumber = "UDYAM-RJ-01-0045120",
                    yearsInBusiness = 15,
                    description = "Farm-direct sun-matured Gulkand, steam-distilled Rose Water, and organic dried rose petals."
                )
            )
        )
    )
}
