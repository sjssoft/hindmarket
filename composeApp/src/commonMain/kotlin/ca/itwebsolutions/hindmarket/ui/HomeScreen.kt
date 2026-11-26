package ca.itwebsolutions.hindmarket.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ca.itwebsolutions.hindmarket.model.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    specialties: List<FamousProductDetail>,
    categories: List<IndustryCategory>,
    announcements: List<AppAnnouncement>,
    gridColumns: Int,
    showHindiSubtitle: Boolean,
    onOpenSearchFilter: (initialMode: String, initialQuery: String, categoryId: String?) -> Unit,
    onSelectSpecialty: (FamousProductDetail) -> Unit,
    onOpenAddBusiness: () -> Unit,
    onOpenSettings: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var showTipBanner by remember { mutableStateOf(true) }
    var tipIndex by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SaffronPrimary,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                title = {
                    Column {
                        Text("HindMarket", style = MaterialTheme.typography.titleLarge, color = Color.White)
                        Text(
                            "India's City ^& District Specialty Directory",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showTipBanner = showTipBanner }) {
                        BadgedBox(badge = { Badge { Text("${announcements.size}") } }) {
                            Icon(Icons.Default.Notifications, contentDescription = "Tips ^& Alerts")
                        }
                    }
                    IconButton(onClick = onOpenSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(WarmBackground)
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                AnimatedVisibility(visible = showTipBanner && announcements.isNotEmpty()) {
                    val currentTip = announcements[tipIndex % announcements.size]
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                        border = BorderStroke(1.5.dp, SaffronPrimary),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Lightbulb, contentDescription = null, tint = SaffronPrimary)
                                    Spacer(Modifier.width(8.dp))
                                    Text(currentTip.title, style = MaterialTheme.typography.titleMedium, color = SaffronPrimary)
                                }
                                IconButton(
                                    onClick = { showTipBanner = false },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.Close, contentDescription = "Hide Tip")
                                }
                            }
                            Spacer(Modifier.height(4.dp))
                            Text(currentTip.message, style = MaterialTheme.typography.bodyMedium)
                            if (announcements.size > 1) {
                                TextButton(
                                    onClick = { tipIndex++ },
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text("Next Tip →", fontWeight = FontWeight.Bold, color = SaffronPrimary)
                                }
                            }
                        }
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    placeholder = { Text("Search city, district, product, or service...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchText.isNotEmpty()) {
                            IconButton(onClick = { onOpenSearchFilter("ALL", searchText, null) }) {
                                Icon(Icons.Default.ArrowForward, contentDescription = "Go")
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { onOpenSearchFilter("AREA", searchText, null) },
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 64.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.LocationOn, contentDescription = null)
                            Spacer(Modifier.height(4.dp))
                            Text("Find by Area", style = MaterialTheme.typography.labelLarge)
                        }
                    }

                    Button(
                        onClick = { onOpenSearchFilter("PRODUCT", searchText, null) },
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 64.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldSecondary)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Storefront, contentDescription = null)
                            Spacer(Modifier.height(4.dp))
                            Text("Find by Product", style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }

            item {
                Text("Top Industries ^& Categories", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(10.dp))
                val rows = categories.chunked(gridColumns)
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    rows.forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            rowItems.forEach { cat ->
                                CategoryGridCard(
                                    category = cat,
                                    showHindi = showHindiSubtitle,
                                    modifier = Modifier.weight(1f),
                                    onClick = { onOpenSearchFilter("PRODUCT", "", cat.id) }
                                )
                            }
                            repeat(gridColumns - rowItems.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    border = BorderStroke(2.dp, EmeraldSecondary),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Own a Factory, Workshop, or Famous Local Business?",
                            style = MaterialTheme.typography.titleMedium,
                            color = EmeraldSecondary
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "List your business free with GPS District Verification ^& WhatsApp Badge.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(Modifier.height(12.dp))
                        Button(
                            onClick = onOpenAddBusiness,
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldSecondary)
                        ) {
                            Icon(Icons.Default.AddBusiness, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Add Your Business (Free)", style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }

            item {
                Text("Famous Across India", style = MaterialTheme.typography.titleLarge)
            }

            items(specialties) { item ->
                SpecialtySummaryCard(item = item, onClick = { onSelectSpecialty(item) })
            }

            item {
                Text(
                    "Developed by IT Web Solutions, Brampton",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun CategoryGridCard(
    category: IndustryCategory,
    showHindi: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .heightIn(min = 104.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = categoryIcon(category.iconKey),
                contentDescription = category.name,
                tint = SaffronPrimary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.height(6.dp))
            Text(category.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            if (showHindi) {
                Text(category.hindiName, style = MaterialTheme.typography.bodyMedium, color = Color.DarkGray)
            }
            Text("${category.itemCount} Hubs", style = MaterialTheme.typography.bodyMedium, color = EmeraldSecondary)
        }
    }
}

@Composable
fun SpecialtySummaryCard(item: FamousProductDetail, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "${item.city}, ${item.state}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = SaffronPrimary
                )
                if (item.hasGiTag) {
                    Surface(
                        color = Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            "GI TAGGED",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            color = EmeraldSecondary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(Modifier.height(4.dp))
            Text(item.title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text(item.whyItIsFamous, style = MaterialTheme.typography.bodyMedium, maxLines = 2)
            Spacer(Modifier.height(8.dp))
            Text(
                "Tap for Heritage, Quality Guide ^& ${item.verifiedBusinesses.size} Verified Sellers →",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = EmeraldSecondary
            )
        }
    }
}

private fun categoryIcon(key: String): ImageVector = when (key) {
    "textile" -> Icons.Default.Checkroom
    "food" -> Icons.Default.Restaurant
    "craft" -> Icons.Default.Handyman
    "agri" -> Icons.Default.Agriculture
    "tools" -> Icons.Default.PrecisionManufacturing
    else -> Icons.Default.ShoppingBag
}
