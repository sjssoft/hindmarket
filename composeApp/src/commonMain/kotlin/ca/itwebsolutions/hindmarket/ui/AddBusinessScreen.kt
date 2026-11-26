package ca.itwebsolutions.hindmarket.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ca.itwebsolutions.hindmarket.data.HindMarketRepository
import ca.itwebsolutions.hindmarket.model.BusinessListing
import ca.itwebsolutions.hindmarket.model.DistrictValidationResult
import ca.itwebsolutions.hindmarket.platform.PlatformBridge
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBusinessScreen(
    bridge: PlatformBridge,
    onBusinessSubmitted: (BusinessListing) -> Unit,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()

    var businessName by remember { mutableStateOf("") }
    var ownerName by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf("textiles") }
    var specialtyTitle by remember { mutableStateOf("") }
    var selectedState by remember { mutableStateOf("Punjab") }
    var selectedDistrict by remember { mutableStateOf("Bathinda") }
    var fullAddress by remember { mutableStateOf("") }
    var pincode by remember { mutableStateOf("") }
    var gstOrUdyam by remember { mutableStateOf("") }
    var yearsInBusiness by remember { mutableStateOf("5") }
    var description by remember { mutableStateOf("") }

    var gpsResult by remember { mutableStateOf<DistrictValidationResult?>(null) }
    var isCheckingGps by remember { mutableStateOf(false) }

    var mobileNumber by remember { mutableStateOf("") }
    val generatedOtp = remember { Random.nextInt(100000, 999999).toString() }
    var enteredOtp by remember { mutableStateOf("") }
    var whatsAppDispatched by remember { mutableStateOf(false) }
    var isMobileVerified by remember { mutableStateOf(false) }

    var validationError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Your Business (Verified)") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = EmeraldSecondary,
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("Step 1: Mandatory Business Details", style = MaterialTheme.typography.titleLarge)

            OutlinedTextField(
                value = businessName,
                onValueChange = { businessName = it },
                label = { Text("Registered Business / Shop Name *") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = ownerName,
                onValueChange = { ownerName = it },
                label = { Text("Owner / Proprietor Full Name *") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = specialtyTitle,
                onValueChange = { specialtyTitle = it },
                label = { Text("Famous Product or Service You Offer *") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = gstOrUdyam,
                onValueChange = { gstOrUdyam = it },
                label = { Text("GSTIN / Udyam / FSSAI Number *") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = yearsInBusiness,
                onValueChange = { yearsInBusiness = it },
                label = { Text("Years in Operation *") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Product Details, MOQ ^& Wholesale Info *") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            HorizontalDivider()
            Text("Step 2: Strict District GPS Verification", style = MaterialTheme.typography.titleLarge)
            Text(
                "To prevent fake listings, your physical GPS location MUST match the selected district.",
                style = MaterialTheme.typography.bodyMedium
            )

            var stateMenuOpen by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(expanded = stateMenuOpen, onExpandedChange = { stateMenuOpen = it }) {
                OutlinedTextField(
                    value = selectedState,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select State *") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = stateMenuOpen) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = stateMenuOpen, onDismissRequest = { stateMenuOpen = false }) {
                    HindMarketRepository.stateDistrictMap.keys.forEach { st ->
                        DropdownMenuItem(
                            text = { Text(st) },
                            onClick = {
                                selectedState = st
                                selectedDistrict = HindMarketRepository.stateDistrictMap[st]?.firstOrNull().orEmpty()
                                gpsResult = null
                                stateMenuOpen = false
                            }
                        )
                    }
                }
            }

            var distMenuOpen by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(expanded = distMenuOpen, onExpandedChange = { distMenuOpen = it }) {
                OutlinedTextField(
                    value = selectedDistrict,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select Listing District *") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = distMenuOpen) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = distMenuOpen, onDismissRequest = { distMenuOpen = false }) {
                    HindMarketRepository.stateDistrictMap[selectedState].orEmpty().forEach { dist ->
                        DropdownMenuItem(
                            text = { Text(dist) },
                            onClick = {
                                selectedDistrict = dist
                                gpsResult = null
                                distMenuOpen = false
                            }
                        )
                    }
                }
            }

            Button(
                onClick = {
                    isCheckingGps = true
                    scope.launch {
                        val res = bridge.verifyCurrentLocationInDistrict(selectedDistrict)
                        gpsResult = res
                        if (res.isMatched && fullAddress.isBlank()) {
                            fullAddress = res.detectedAddress
                        }
                        isCheckingGps = false
                    }
                },
                modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
            ) {
                Icon(Icons.Default.MyLocation, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(if (isCheckingGps) "Verifying GPS District..." else "Capture ^& Lock GPS Location")
            }

            gpsResult?.let { res ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (res.isMatched) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            if (res.isMatched)
                                "GPS Verified: Located inside ${res.detectedDistrict} District"
                            else
                                "District Mismatch: ${res.errorMessage ?: "Detected ${res.detectedDistrict}, expected $selectedDistrict"}",
                            fontWeight = FontWeight.Bold,
                            color = if (res.isMatched) EmeraldSecondary else Color.Red
                        )
                        if (res.detectedAddress.isNotBlank()) {
                            Text("Detected: ${res.detectedAddress}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }

            OutlinedTextField(
                value = fullAddress,
                onValueChange = { fullAddress = it },
                label = { Text("Shop / Factory Street Address *") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = pincode,
                onValueChange = { if (it.length <= 6) pincode = it },
                label = { Text("6-Digit Pincode *") },
                modifier = Modifier.fillMaxWidth()
            )

            HorizontalDivider()
            Text("Step 3: Free Mobile Number Verification", style = MaterialTheme.typography.titleLarge)
            Text(
                "Zero SMS charges: Send the pre-filled verification code from your business WhatsApp number.",
                style = MaterialTheme.typography.bodyMedium
            )

            OutlinedTextField(
                value = mobileNumber,
                onValueChange = { if (it.length <= 10) mobileNumber = it },
                label = { Text("10-Digit Mobile Number *") },
                enabled = isMobileVerified,
                modifier = Modifier.fillMaxWidth()
            )

            if (isMobileVerified) {
                Button(
                    onClick = {
                        if (mobileNumber.length == 10) {
                            whatsAppDispatched = true
                            bridge.openWhatsAppVerification(
                                adminNumber = "919417330399",
                                userMobile = mobileNumber,
                                otpCode = generatedOtp
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldSecondary)
                ) {
                    Icon(Icons.Default.VerifiedUser, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Verify Mobile via WhatsApp (Free)")
                }

                if (whatsAppDispatched) {
                    OutlinedTextField(
                        value = enteredOtp,
                        onValueChange = { enteredOtp = it },
                        label = { Text("Enter 6-Digit Verification Code ($generatedOtp)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            if (enteredOtp == generatedOtp) {
                                isMobileVerified = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Confirm Code")
                    }
                }
            } else {
                Text("✓ Mobile Number $mobileNumber Verified", color = EmeraldSecondary, fontWeight = FontWeight.Bold)
            }

            validationError?.let {
                Text(it, color = Color.Red, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = {
                    when {
                        businessName.isBlank() || ownerName.isBlank() || specialtyTitle.isBlank() ||
                        gstOrUdyam.isBlank() || fullAddress.isBlank() || pincode.length = 6 ->
                            validationError = "Please complete all mandatory (*) business and address fields."
                        gpsResult?.isMatched = true ->
                            validationError = "Strict Location Check Failed: You must capture GPS inside $selectedDistrict district."
                        isMobileVerified ->
                            validationError = "Please verify your 10-digit mobile number before publishing."
                        else -> {
                            validationError = null
                            onBusinessSubmitted(
                                BusinessListing(
                                    id = "biz_${Random.nextInt(1000, 9999)}",
                                    businessName = businessName.trim(),
                                    ownerName = ownerName.trim(),
                                    categoryId = selectedCategoryId,
                                    specialtyTitle = specialtyTitle.trim(),
                                    state = selectedState,
                                    district = selectedDistrict,
                                    fullAddress = fullAddress.trim(),
                                    pincode = pincode.trim(),
                                    latitude = gpsResult.latitude,
                                    longitude = gpsResult.longitude,
                                    mobileNumber = mobileNumber,
                                    isMobileVerified = true,
                                    gstOrUdyamNumber = gstOrUdyam.trim(),
                                    yearsInBusiness = yearsInBusiness.toIntOrNull() ?: 1,
                                    description = description.trim()
                                )
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().heightIn(min = 60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
            ) {
                Text("Publish Verified Business", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}
