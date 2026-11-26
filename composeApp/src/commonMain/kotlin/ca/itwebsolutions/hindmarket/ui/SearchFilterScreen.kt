package ca.itwebsolutions.hindmarket.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ca.itwebsolutions.hindmarket.data.HindMarketRepository
import ca.itwebsolutions.hindmarket.model.FamousProductDetail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFilterScreen(
    initialMode: String,
    initialQuery: String,
    initialCategoryId: String?,
    allSpecialties: List<FamousProductDetail>,
    onSelectSpecialty: (FamousProductDetail) -> Unit,
    onBack: () -> Unit
) {
    var mode by remember { mutableStateOf(initialMode) }
    var query by remember { mutableStateOf(initialQuery) }
    var selectedState by remember { mutableStateOf<String?>(null) }
    var selectedDistrict by remember { mutableStateOf<String?>(null) }
    var selectedCategory by remember { mutableStateOf(initialCategoryId) }

    val filtered = allSpecialties.filter { item ->
        val matchesQuery = query.isBlank() ||
            item.title.contains(query, ignoreCase = true) ||
            item.city.contains(query, ignoreCase = true) ||
            item.district.contains(query, ignoreCase = true) ||
            item.state.contains(query, ignoreCase = true)
        val matchesState = selectedState == null || item.state == selectedState
        val matchesDistrict = selectedDistrict == null || item.district == selectedDistrict
        val matchesCat = selectedCategory == null || item.categoryId == selectedCategory
        matchesQuery && matchesState && matchesDistrict && matchesCat
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (mode == "AREA") "Find by Area / District" else "Find by Product / Service") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SaffronPrimary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                FilterChip(
                    selected = mode == "AREA",
                    onClick = { mode = "AREA" },
                    label = { Text("By State ^& District") }
                )
                FilterChip(
                    selected = mode == "PRODUCT",
                    onClick = { mode = "PRODUCT" },
                    label = { Text("By Product ^& Category") }
                )
            }

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(if (mode == "AREA") "Type City or District name..." else "Type Product or Industry...") },
                singleLine = true
            )

            if (mode == "AREA") {
                Text("Select State:", style = MaterialTheme.typography.titleMedium)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FilterChip(
                            selected = selectedState == null,
                            onClick = { selectedState = null; selectedDistrict = null },
                            label = { Text("All States") }
                        )
                    }
                    items(HindMarketRepository.stateDistrictMap.keys.toList()) { st ->
                        FilterChip(
                            selected = selectedState == st,
                            onClick = { selectedState = st; selectedDistrict = null },
                            label = { Text(st) }
                        )
                    }
                }
                selectedState?.let { st ->
                    val districts = HindMarketRepository.stateDistrictMap[st].orEmpty()
                    Text("Select District in $st:", style = MaterialTheme.typography.titleMedium)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(districts) { dist ->
                            FilterChip(
                                selected = selectedDistrict == dist,
                                onClick = { selectedDistrict = if (selectedDistrict == dist) null else dist },
                                label = { Text(dist) }
                            )
                        }
                    }
                }
            } else {
                Text("Filter by Category:", style = MaterialTheme.typography.titleMedium)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FilterChip(
                            selected = selectedCategory == null,
                            onClick = { selectedCategory = null },
                            label = { Text("All Categories") }
                        )
                    }
                    items(HindMarketRepository.categories) { cat ->
                        FilterChip(
                            selected = selectedCategory == cat.id,
                            onClick = { selectedCategory = cat.id },
                            label = { Text(cat.name) }
                        )
                    }
                }
            }

            Text("${filtered.size} Results Found", style = MaterialTheme.typography.titleMedium)

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(filtered) { item ->
                    SpecialtySummaryCard(item = item, onClick = { onSelectSpecialty(item) })
                }
            }
        }
    }
}
