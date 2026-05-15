package com.example.virasatnamma.ui.screens.passport

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.virasatnamma.data.local.CheckInEntity
import com.example.virasatnamma.ui.theme.BrownDark
import com.example.virasatnamma.ui.theme.BrownMedium
import com.example.virasatnamma.ui.theme.GoldPrimary
import com.example.virasatnamma.viewmodel.HeritageViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PassportScreen(viewModel: HeritageViewModel) {
    val checkIns by viewModel.checkIns.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(BrownDark, BrownMedium)))
                .padding(20.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.MenuBook, null, tint = GoldPrimary)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Travel Passport",
                        color = GoldPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
                Text(
                    "${checkIns.size} site${if (checkIns.size != 1) "s" else ""} visited",
                    color = Color.White.copy(0.7f),
                    fontSize = 13.sp
                )
            }
        }

        if (checkIns.isEmpty()) {
            EmptyPassportContent()
        } else {
            // Stamp badge
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(14.dp),
                color = GoldPrimary.copy(alpha = 0.15f)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.CheckCircle, null, tint = GoldPrimary, modifier = Modifier.size(28.dp))
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            "${checkIns.size} of 12 stamps collected",
                            fontWeight = FontWeight.Bold,
                            color = BrownDark,
                            fontSize = 15.sp
                        )
                        Text("Keep exploring Karnataka!", color = BrownMedium, fontSize = 12.sp)
                    }
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(checkIns) { checkIn ->
                    PassportStampCard(checkIn, viewModel)
                }
                item { Spacer(Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
private fun PassportStampCard(
    checkIn: CheckInEntity,
    viewModel: HeritageViewModel
) {
    val formatter = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    val dateStr = formatter.format(Date(checkIn.checkedInAt))

    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(50),
                color = BrownDark,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(Icons.Default.CheckCircle, null, tint = GoldPrimary, modifier = Modifier.size(26.dp))
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(checkIn.siteName, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = BrownDark)
                Spacer(Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, tint = GoldPrimary, modifier = Modifier.size(12.dp))
                    Spacer(Modifier.width(3.dp))
                    Text(checkIn.siteLocation, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Spacer(Modifier.height(2.dp))
                Text(dateStr, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.7f))
            }

            IconButton(onClick = { showEditDialog = true }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = BrownMedium)
            }
            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
            }
        }
    }

    if (showEditDialog) {
        EditDialog(
            checkIn = checkIn,
            onDismiss = { showEditDialog = false },
            onSave = {
                viewModel.updateCheckIn(it)
                showEditDialog = false
            }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteCheckIn(checkIn)
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
            },
            title = { Text("Delete Entry") },
            text = { Text("Are you sure you want to delete this stamp?") }
        )
    }
}

@Composable
private fun EditDialog(
    checkIn: CheckInEntity,
    onDismiss: () -> Unit,
    onSave: (CheckInEntity) -> Unit
) {
    var name by remember { mutableStateOf(checkIn.siteName) }
    var location by remember { mutableStateOf(checkIn.siteLocation) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = { onSave(checkIn.copy(siteName = name, siteLocation = location)) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text("Edit Stamp") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Site Name") }
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location") }
                )
            }
        }
    )
}

@Composable
private fun EmptyPassportContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.MenuBook,
            null,
            tint = GoldPrimary.copy(0.4f),
            modifier = Modifier.size(80.dp)
        )
        Spacer(Modifier.height(20.dp))
        Text(
            "Your passport is empty",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = BrownDark
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Visit heritage sites and tap 'Check In' to collect stamps!",
            fontSize = 13.sp,
            color = BrownMedium,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
