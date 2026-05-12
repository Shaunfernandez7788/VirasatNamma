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
import com.example.virasatnamma.ui.theme.*
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
        Header(checkIns.size)

        if (checkIns.isEmpty()) {
            EmptyPassportContent()
        } else {
            StampHeader(checkIns.size)

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(checkIns) { checkIn ->
                    PassportStampCard(checkIn, viewModel)
                }
            }
        }
    }
}

@Composable
private fun Header(count: Int) {
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
                Text("Travel Passport", color = GoldPrimary, fontWeight = FontWeight.Bold)
            }
            Text(
                "$count site${if (count != 1) "s" else ""} visited",
                color = Color.White.copy(0.7f)
            )
        }
    }
}

@Composable
private fun StampHeader(count: Int) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(14.dp),
        color = GoldPrimary.copy(alpha = 0.15f)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Icon(Icons.Default.CheckCircle, null, tint = GoldPrimary)
            Spacer(Modifier.width(8.dp))
            Column {
                Text("$count of 5 stamps collected", fontWeight = FontWeight.Bold)
                Text("Keep exploring Karnataka!")
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
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.CheckCircle, null, tint = GoldPrimary)

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(checkIn.siteName, fontWeight = FontWeight.Bold)
                Row {
                    Icon(Icons.Default.LocationOn, null, tint = GoldPrimary)
                    Spacer(Modifier.width(4.dp))
                    Text(checkIn.siteLocation)
                }
                Text(dateStr, fontSize = 12.sp)
            }

            // ✏️ EDIT BUTTON
            IconButton(onClick = { showEditDialog = true }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }

            // 🗑️ DELETE BUTTON
            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
            }
        }
    }

    // ✏️ EDIT DIALOG
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

    // 🗑️ DELETE CONFIRMATION
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
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
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
            Button(onClick = {
                onSave(checkIn.copy(siteName = name, siteLocation = location))
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Edit Stamp") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Site Name") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Location") })
            }
        }
    )
}

@Composable
private fun EmptyPassportContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.MenuBook, null, tint = GoldPrimary.copy(0.5f))
        Text("Your passport is empty", fontWeight = FontWeight.Bold)
    }
}