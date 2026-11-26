package ca.itwebsolutions.hindmarket.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ca.itwebsolutions.hindmarket.model.BusinessListing
import ca.itwebsolutions.hindmarket.model.FamousProductDetail
import ca.itwebsolutions.hindmarket.platform.PlatformBridge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    detail: FamousProductDetail,
    bridge: PlatformBridge,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${detail.city} (${detail.district})") },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(detail.title, style = MaterialTheme.typography.headlineMedium)
                        Text(
                            "Hub: ${detail.city}, District ${detail.district}, ${detail.state}",
                            style = MaterialTheme.typography.titleMedium,
                            color = SaffronPrimary
                        )
                        HorizontalDivider()
                        Text("Historical Origin", style = MaterialTheme.typography.titleMedium)
                        Text(detail.historicalOrigin, style = MaterialTheme.typography.bodyLarge)
                        Spacer(Modifier.height(4.dp))
                        Text("Why It Is Famous", style = MaterialTheme.typography.titleMedium)
                        Text(detail.whyItIsFamous, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }

            item {
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1))) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Commercial ^& Quality Guide", style = MaterialTheme.typography.titleLarge)
                        Text("Typical Price Range: ${detail.priceRangeInr}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                        Text("Best Buying Season: ${detail.peakSeason}", style = MaterialTheme.typography.bodyLarge)
                        Spacer(Modifier.height(4.dp))
                        Text("How to Identify Authentic Quality:", style = MaterialTheme.typography.titleMedium)
                        detail.qualityChecklist.forEach { point ->
                            Row(verticalAlignment = Alignment.Top) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSecondary, modifier = Modifier.size(20.dp))
                                Spacer(Modifier.width(8.dp))
                                Text(point, style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    "Verified Local Manufacturers ^& Sellers (${detail.verifiedBusinesses.size})",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            items(detail.verifiedBusinesses) { biz ->
                BusinessContactCard(biz = biz, bridge = bridge)
            }
        }
    }
}

@Composable
private fun BusinessContactCard(biz: BusinessListing, bridge: PlatformBridge) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(biz.businessName, style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
                if (biz.isMobileVerified) {
                    Icon(Icons.Default.Verified, contentDescription = "Verified", tint = EmeraldSecondary)
                }
            }
            Text("Proprietor: ${biz.ownerName} • ${biz.yearsInBusiness} Yrs Experience", style = MaterialTheme.typography.bodyMedium)
            Text("GPS-Verified Address: ${biz.fullAddress}, Dist. ${biz.district} - ${biz.pincode}", style = MaterialTheme.typography.bodyLarge)
            if (biz.gstOrUdyamNumber.isNotBlank()) {
                Text("Reg/GST/Udyam: ${biz.gstOrUdyamNumber}", style = MaterialTheme.typography.bodyMedium, color = EmeraldSecondary)
            }
            Text(biz.description, style = MaterialTheme.typography.bodyLarge)

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { bridge.openPhoneDialer(biz.mobileNumber) },
                    modifier = Modifier.weight(1f).heightIn(min = 52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
                ) {
                    Icon(Icons.Default.Call, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Call")
                }
                Button(
                    onClick = {
                        bridge.openWhatsAppChat(
                            biz.mobileNumber,
                            "Namaste ${biz.ownerName}, I found ${biz.businessName} on HindMarket."
                        )
                    },
                    modifier = Modifier.weight(1f).heightIn(min = 52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldSecondary)
                ) {
                    Icon(Icons.Default.Chat, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("WhatsApp")
                }
            }
            OutlinedButton(
                onClick = { bridge.openMapCoordinates(biz.latitude, biz.longitude, biz.businessName) },
                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
            ) {
                Icon(Icons.Default.Directions, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("Navigate to Shop via GPS")
            }
        }
    }
}
