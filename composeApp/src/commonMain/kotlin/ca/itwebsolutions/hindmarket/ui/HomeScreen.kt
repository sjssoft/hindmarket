package ca.itwebsolutions.hindmarket.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
    var showTipBanner by remember { mutableStateOf(false) } // Hidden by default; tap bell to expand
    var tipIndex by remember { mutableStateOf(0) }

    Scaffold(
        containerColor = CanvasBackground,
        topBar = {
            Surface(color = SurfaceWhite, shadowElevation = 1.dp) {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = SurfaceWhite,
                        titleContentColor = InkPrimary
                    ),
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(TerracottaPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Rounded.Storefront,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text("HindMarket", style = MaterialTheme.typography.titleLarge)
                                Text(
                                    "Famous Hubs & Verified Makers",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    },
                    actions = {
                        // Toggleable Tip/Notification Pill Trigger
                        IconButton(onClick = { showTipBanner = !showTipBanner }) {
                            BadgedBox(
                                badge = {
                                    if (announcements.isNotEmpty()) {
                                        Badge(containerColor = TerracottaPrimary) {
                                            Text("${announcements.size}", color = Color.White)
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    if (showTipBanner) Icons.Rounded.NotificationsActive else Icons.Outlined.Notifications,
                                    contentDescription = "Notifications & Tips",
                                    tint = InkPrimary
                                )
                            }
                        }
                        IconButton(onClick = onOpenSettings) {
                            Icon(Icons.Outlined.Settings, contentDescription = "Settings", tint = InkPrimary)
                        }
                    }
                )
            }
        }
    ) { padding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.TopCenter
        ) {
            // Responsive column count & max width for tablets/foldables
            val effectiveColumns = if (maxWidth >= 600.dp) maxOf(gridColumns, 3) else gridColumns
            val contentWidthModifier = if (maxWidth > 720.dp) Modifier.width(720.dp) else Modifier.fillMaxWidth()

            LazyColumn(
                modifier = contentWidthModifier.fillMaxHeight(),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // 5. Hidden / Expandable Notification & Tip Strip
                item {
                    AnimatedVisibility(visible = showTipBanner && announcements.isNotEmpty()) {
                        val tip = announcements[tipIndex % announcements.size]
                        Surface(
                            color = TerracottaSoft,
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, TerracottaPrimary.copy(alpha = 0.25f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                        Icon(Icons.Rounded.TipsAndUpdates, contentDescription = null, tint = TerracottaPrimary)
                                        Spacer(Modifier.width(8.dp))
                                        Text(tip.title, style = MaterialTheme.typography.titleMedium, color = TerracottaPrimary)
                                    }
                                    IconButton(onClick = { showTipBanner = false }, modifier = Modifier.size(28.dp)) {
                                        Icon(Icons.Rounded.Close, contentDescription = "Close", tint = InkSecondary)
                                    }
                                }
                                Spacer(Modifier.height(6.dp))
                                Text(tip.message, style = MaterialTheme.typography.bodyMedium, color = InkPrimary)
                                if (announcements.size > 1) {
                                    Spacer(Modifier.height(6.dp))
                                    Text(
                                        "Tap for next tip →",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = TerracottaPrimary,
                                        modifier = Modifier.clickable { tipIndex++ }
                                    )
                                }
                            }
                        }
                    }
                }

                // 1A. Clean Pill Search Bar
                item {
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = SurfaceWhite,
                        border = BorderStroke(1.dp, HairlineBorder),
                        shadowElevation = 2.dp
                    ) {
                        TextField(
                            value = searchText,
                            onValueChange = { searchText = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            placeholder = {
                                Text(
                                    "Search city, district, product or service...",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = InkSecondary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            leadingIcon = {
                                Icon(Icons.Rounded.Search, contentDescription = null, tint = TerracottaPrimary)
                            },
                            trailingIcon = {
                                if (searchText.isNotEmpty()) {
                                    IconButton(onClick = { onOpenSearchFilter("ALL", searchText, null) }) {
                                        Icon(Icons.Rounded.ArrowForward, contentDescription = "Search", tint = TerracottaPrimary)
                                    }
                                }
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = SurfaceWhite,
                                unfocusedContainerColor = SurfaceWhite,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )
                    }
                }

                // 1B. Two Minimal Parallel Action Cards (Find by Area | Find by Product)
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        MinimalActionTile(
                            title = "Find by Area",
                            subtitle = "State & District",
                            icon = Icons.Rounded.TravelExplore,
                            accentColor = TerracottaPrimary,
                            softBg = TerracottaSoft,
                            modifier = Modifier.weight(1f),
                            onClick = { onOpenSearchFilter("AREA", searchText, null) }
                        )
                        MinimalActionTile(
                            title = "Find by Product",
                            subtitle = "Goods & Services",
                            icon = Icons.Rounded.Inventory2,
                            accentColor = ForestVerified,
                            softBg = ForestSoft,
                            modifier = Modifier.weight(1f),
                            onClick = { onOpenSearchFilter("PRODUCT", searchText, null) }
                        )
                    }
                }

                // 2. Top Industries / Categories Grid (2 or 3 per row)
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Top Industries", style = MaterialTheme.typography.titleLarge)
                        Text(
                            "${categories.size} Sectors",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    val rows = categories.chunked(effectiveColumns)
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        rows.forEach { rowItems ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                rowItems.forEach { cat ->
                                    MinimalCategoryCard(
                                        category = cat,
                                        showHindi = showHindiSubtitle,
                                        modifier = Modifier.weight(1f),
                                        onClick = { onOpenSearchFilter("PRODUCT", "", cat.id) }
                                    )
                                }
                                repeat(effectiveColumns - rowItems.size) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }

                // 3. Minimal "Add Your Business" Banner CTA
                item {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = SurfaceWhite,
                        border = BorderStroke(1.dp, HairlineBorder)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                                Text("List Your Business", style = MaterialTheme.typography.titleMedium)
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    "Free GPS district badge & WhatsApp verification for genuine makers.",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            Button(
                                onClick = onOpenAddBusiness,
                                shape = RoundedCornerShape(14.dp),
                                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ForestVerified)
                            ) {
                                Icon(Icons.Rounded.Add, contentDescription = null, modifier = Modifier.size(20.dp))
                                Spacer(Modifier.width(6.dp))
                                Text("Add Free", style = MaterialTheme.typography.labelLarge)
                            }
                        }
                    }
                }

                // Featured Specialties List
                item {
                    Text("Famous Across India", style = MaterialTheme.typography.titleLarge)
                }

                items(specialties) { item ->
                    SpecialtySummaryCard(item = item, onClick = { onSelectSpecialty(item) })
                }

                // Clean Minimal Footer
                item {
                    Text(
                        "HindMarket • IT Web Solutions, Brampton",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MinimalActionTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    softBg: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        color = SurfaceWhite,
        border = BorderStroke(1.dp, HairlineBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(softBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(24.dp))
            }
            Spacer(Modifier.height(12.dp))
            Text(title, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.height(2.dp))
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
private fun MinimalCategoryCard(
    category: IndustryCategory,
    showHindi: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        color = SurfaceWhite,
        border = BorderStroke(1.dp, HairlineBorder)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(TerracottaSoft),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = categoryIcon(category.iconKey),
                    contentDescription = category.name,
                    tint = TerracottaPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(Modifier.height(10.dp))
            Text(
                category.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (showHindi) {
                Spacer(Modifier.height(2.dp))
                Text(
                    category.hindiName,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun SpecialtySummaryCard(item: FamousProductDetail, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        color = SurfaceWhite,
        border = BorderStroke(1.dp, HairlineBorder)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Rounded.Place,
                        contentDescription = null,
                        tint = TerracottaPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "${item.city}, ${item.state}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TerracottaPrimary
                    )
                }
                if (item.hasGiTag) {
                    Surface(color = ForestSoft, shape = RoundedCornerShape(8.dp)) {
                        Text(
                            "GI Tag",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = ForestVerified
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(item.title, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(6.dp))
            Text(
                item.whyItIsFamous,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    item.priceRangeInr,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = InkPrimary
                )
                Text(
                    "${item.verifiedBusinesses.size} Verified →",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = ForestVerified
                )
            }
        }
    }
}

private fun categoryIcon(key: String): ImageVector = when (key) {
    "textile" -> Icons.Rounded.Checkroom
    "food" -> Icons.Rounded.Restaurant
    "craft" -> Icons.Rounded.Handyman
    "agri" -> Icons.Rounded.Eco
    "tools" -> Icons.Rounded.PrecisionManufacturing
    else -> Icons.Rounded.ShoppingBag
}
