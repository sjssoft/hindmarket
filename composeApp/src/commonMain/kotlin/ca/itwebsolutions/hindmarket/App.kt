package ca.itwebsolutions.hindmarket

import androidx.compose.runtime.*
import ca.itwebsolutions.hindmarket.data.HindMarketRepository
import ca.itwebsolutions.hindmarket.model.*
import ca.itwebsolutions.hindmarket.platform.PlatformBridge
import ca.itwebsolutions.hindmarket.ui.*

@Composable
fun App() {
    val bridge = remember { PlatformBridge() }
    var fontScale by remember { mutableStateOf(FontScalePreset.LARGE) }
    var gridColumns by remember { mutableStateOf(2) }
    var showHindi by remember { mutableStateOf(true) }
    var showSettings by remember { mutableStateOf(false) }

    val specialties = remember { mutableStateListOf<FamousProductDetail>().apply { addAll(HindMarketRepository.initialSpecialties) } }
    var currentScreen by remember { mutableStateOf("HOME") }
    var filterMode by remember { mutableStateOf("AREA") }
    var filterQuery by remember { mutableStateOf("") }
    var filterCategory by remember { mutableStateOf<String?>(null) }
    var selectedDetail by remember { mutableStateOf<FamousProductDetail?>(null) }

    HindMarketTheme(fontScale = fontScale) {
        when (currentScreen) {
            "HOME" -> HomeScreen(
                specialties = specialties,
                categories = HindMarketRepository.categories,
                announcements = HindMarketRepository.announcements,
                gridColumns = gridColumns,
                showHindiSubtitle = showHindi,
                onOpenSearchFilter = { mode, q, cat ->
                    filterMode = mode
                    filterQuery = q
                    filterCategory = cat
                    currentScreen = "SEARCH"
                },
                onSelectSpecialty = { item ->
                    selectedDetail = item
                    currentScreen = "DETAIL"
                },
                onOpenAddBusiness = { currentScreen = "ADD_BIZ" },
                onOpenSettings = { showSettings = true }
            )

            "SEARCH" -> SearchFilterScreen(
                initialMode = filterMode,
                initialQuery = filterQuery,
                initialCategoryId = filterCategory,
                allSpecialties = specialties,
                onSelectSpecialty = { item ->
                    selectedDetail = item
                    currentScreen = "DETAIL"
                },
                onBack = { currentScreen = "HOME" }
            )

            "DETAIL" -> selectedDetail?.let { detail ->
                DetailScreen(
                    detail = detail,
                    bridge = bridge,
                    onBack = { currentScreen = "HOME" }
                )
            }

            "ADD_BIZ" -> AddBusinessScreen(
                bridge = bridge,
                onBusinessSubmitted = { newBiz ->
                    val existingIdx = specialties.indexOfFirst {
                        it.district.equals(newBiz.district, ignoreCase = true) &&
                        it.title.equals(newBiz.specialtyTitle, ignoreCase = true)
                    }
                    if (existingIdx >= 0) {
                        val old = specialties[existingIdx]
                        specialties[existingIdx] = old.copy(verifiedBusinesses = old.verifiedBusinesses + newBiz)
                    } else {
                        specialties.add(
                            0,
                            FamousProductDetail(
                                id = "spec_${newBiz.id}",
                                title = newBiz.specialtyTitle,
                                categoryId = newBiz.categoryId,
                                city = newBiz.district,
                                district = newBiz.district,
                                state = newBiz.state,
                                historicalOrigin = "Local specialty hub in ${newBiz.district}, ${newBiz.state}.",
                                whyItIsFamous = newBiz.description,
                                priceRangeInr = "Contact Seller for Direct Rates",
                                peakSeason = "All Year",
                                qualityChecklist = listOf("GPS District-Verified Seller", "WhatsApp Number Verified"),
                                verifiedBusinesses = listOf(newBiz)
                            )
                        )
                    }
                    currentScreen = "HOME"
                },
                onBack = { currentScreen = "HOME" }
            )
        }

        if (showSettings) {
            SettingsDialog(
                currentFontScale = fontScale,
                currentGridColumns = gridColumns,
                showHindiSubtitle = showHindi,
                onFontScaleChange = { fontScale = it },
                onGridColumnsChange = { gridColumns = it },
                onToggleHindi = { showHindi = it },
                onDismiss = { showSettings = false }
            )
        }
    }
}
