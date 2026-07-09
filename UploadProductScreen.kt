package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.viewmodel.UploadUiState
import com.example.ui.viewmodel.UploadViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadProductScreen(
    uploadViewModel: UploadViewModel,
    onNavigateBack: () -> Unit,
    onUploadSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by uploadViewModel.uiState.collectAsState()
    val uploadProgress by uploadViewModel.uploadProgress.collectAsState()

    val title by uploadViewModel.titleInput.collectAsState()
    val description by uploadViewModel.descriptionInput.collectAsState()
    val price by uploadViewModel.priceInput.collectAsState()
    val selectedCategory by uploadViewModel.categoryInput.collectAsState()
    val imageUrls by uploadViewModel.imageUrls.collectAsState()

    val titleError by uploadViewModel.titleError.collectAsState()
    val descriptionError by uploadViewModel.descriptionError.collectAsState()
    val priceError by uploadViewModel.priceError.collectAsState()
    val imagesError by uploadViewModel.imagesError.collectAsState()

    val categories = listOf("Books", "Electronics", "Furniture", "Notes", "Lab Equipment", "Hostel Essentials")

    // High quality preset Unsplash options representing campus products to make demo testing painless and beautiful!
    val presetImages = listOf(
        "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=500&auto=format&fit=crop&q=80", // Textbook
        "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500&auto=format&fit=crop&q=80", // Headphones
        "https://images.unsplash.com/photo-1506880018603-83d5b814b5a6?w=500&auto=format&fit=crop&q=80", // Notes/Pen
        "https://images.unsplash.com/photo-1518455027359-f3f8164ba6bd?w=500&auto=format&fit=crop&q=80", // Desk/Lamp
        "https://images.unsplash.com/photo-1584622650111-993a426fbf0a?w=500&auto=format&fit=crop&q=80", // Fridge
        "https://images.unsplash.com/photo-1548883354-7622d03aca27?w=500&auto=format&fit=crop&q=80", // Jacket
        "https://images.unsplash.com/photo-1485965120184-e220f721d03e?w=500&auto=format&fit=crop&q=80", // Bicycle
        "https://images.unsplash.com/photo-1517842645767-c639042777db?w=500&auto=format&fit=crop&q=80"  // Notebooks
    )

    LaunchedEffect(uiState) {
        if (uiState is UploadUiState.Success) {
            uploadViewModel.resetState()
            onUploadSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "List New Product",
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.Top
        ) {
            // Form Description text
            Text(
                text = "Advertise your item to everyone on campus.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Category Selector
            Text(
                text = "Product Category",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 10.dp)
            )
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(categories) { _, category ->
                    val isSelected = selectedCategory == category
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                            .clickable { uploadViewModel.categoryInput.value = category }
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = category,
                            fontWeight = FontWeight.Medium,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title Field
            OutlinedTextField(
                value = title,
                onValueChange = { uploadViewModel.titleInput.value = it },
                label = { Text("Product Title") },
                placeholder = { Text("e.g. Sony WH-1000XM4 Headphones") },
                leadingIcon = { Icon(Icons.Filled.Title, contentDescription = "Title") },
                isError = titleError != null,
                supportingText = { titleError?.let { Text(it) } },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("upload_title")
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Price Field
            OutlinedTextField(
                value = price,
                onValueChange = { uploadViewModel.priceInput.value = it },
                label = { Text("Price (₹)") },
                placeholder = { Text("e.g. 180") },
                leadingIcon = { Icon(Icons.Filled.AttachMoney, contentDescription = "Price") },
                isError = priceError != null,
                supportingText = { priceError?.let { Text(it) } },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("upload_price")
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Description Field
            OutlinedTextField(
                value = description,
                onValueChange = { uploadViewModel.descriptionInput.value = it },
                label = { Text("Full Description") },
                placeholder = { Text("Mention details, condition, pick-up points, why you are selling, etc...") },
                leadingIcon = { Icon(Icons.Filled.Description, contentDescription = "Description") },
                isError = descriptionError != null,
                supportingText = { descriptionError?.let { Text(it) } },
                shape = RoundedCornerShape(16.dp),
                minLines = 3,
                maxLines = 6,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("upload_description")
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Images Heading
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Filled.Image,
                    contentDescription = "Image logo",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Add Images (Min 3 required)",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Text(
                text = "Tap on preset campus pictures below to auto-select, or input custom Unsplash image links in fields:",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
            )

            // Preset Grid Row
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(presetImages) { _, presetUrl ->
                    AsyncImage(
                        model = presetUrl,
                        contentDescription = "Preset option",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(70.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(
                                width = 2.dp,
                                color = if (imageUrls.contains(presetUrl)) MaterialTheme.colorScheme.primary else Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                // Find first blank field to insert
                                val index = imageUrls.indexOfFirst { it.isBlank() }
                                if (index != -1) {
                                    uploadViewModel.setImageUrl(index, presetUrl)
                                } else {
                                    // Replace first item
                                    uploadViewModel.setImageUrl(0, presetUrl)
                                }
                            }
                    )
                }
            }

            // Image URL Fields
            imageUrls.forEachIndexed { index, url ->
                OutlinedTextField(
                    value = url,
                    onValueChange = { uploadViewModel.setImageUrl(index, it) },
                    label = { Text("Image URL ${index + 1}") },
                    placeholder = { Text("https://example.com/image.jpg") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .testTag("upload_image_url_$index")
                )
            }

            imagesError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Submitting State / Upload Progress Indicator
            AnimatedVisibility(visible = uiState is UploadUiState.Submitting) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Uploading Product Details to Firestore: $uploadProgress%",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    LinearProgressIndicator(
                        progress = { uploadProgress / 100f },
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Submit Button
            Button(
                onClick = { uploadViewModel.uploadProduct() },
                shape = RoundedCornerShape(100.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("submit_product_button"),
                enabled = uiState !is UploadUiState.Submitting
            ) {
                if (uiState is UploadUiState.Submitting) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Confirm"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Publish Advertisement",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
