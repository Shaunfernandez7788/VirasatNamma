package com.example.virasatnamma.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.virasatnamma.ui.theme.BrownDark
import com.example.virasatnamma.ui.theme.BrownMedium
import com.example.virasatnamma.ui.theme.GoldPrimary
import com.example.virasatnamma.viewmodel.HeritageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    siteId: String,
    viewModel: HeritageViewModel,
    onAskAI: () -> Unit,
    onOpenExpense: () -> Unit,
    onBack: () -> Unit
) {

    val site = viewModel.getSiteById(siteId)
    val isPlaying by viewModel.isPlaying.collectAsState()
    val isCheckedIn by viewModel.isCheckedIn.collectAsState()

    LaunchedEffect(siteId) {
        viewModel.loadCheckInStatus(siteId)
    }

    DisposableEffect(Unit) {
        onDispose { viewModel.releaseAudio() }
    }

    if (site == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Site not found", color = BrownDark)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {

        // ── IMAGE ─────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        ) {
            AsyncImage(
                model = site.imageRes,
                contentDescription = site.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                error = painterResource(id = android.R.drawable.ic_menu_gallery)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                BrownDark.copy(alpha = 0.55f),
                                Color.Transparent,
                                BrownDark.copy(alpha = 0.7f)
                            )
                        )
                    )
            )

            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.TopStart)
                    .clip(CircleShape)
                    .background(BrownDark.copy(alpha = 0.6f))
            ) {
                Icon(Icons.Default.ArrowBack, null, tint = Color.White)
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(site.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, tint = GoldPrimary, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(site.location, color = Color.White.copy(0.85f), fontSize = 13.sp)
                }
            }
        }

        // ── CONTENT ───────────────────────────────────────────
        Column(modifier = Modifier.padding(20.dp)) {

            // Audio Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = BrownDark)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        if (isPlaying) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                        null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(40.dp)
                    )

                    Spacer(Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text("Audio Guide", color = GoldPrimary, fontWeight = FontWeight.Bold)
                        Text(
                            if (isPlaying) "Playing..." else "Tap to listen",
                            color = Color.White.copy(0.7f)
                        )
                    }

                    Button(
                        onClick = { viewModel.toggleAudio(site.audioRes) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldPrimary,
                            contentColor = BrownDark
                        )
                    ) {
                        Text(if (isPlaying) "Pause" else "Play")
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // About
            Text("About", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(8.dp))
            Text(site.description)

            Spacer(Modifier.height(28.dp))

            // ── ACTION ROW ─────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { if (!isCheckedIn) viewModel.checkIn(site) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCheckedIn) BrownMedium else BrownDark
                    )
                ) {
                    Text(if (isCheckedIn) "Checked In" else "Check In")
                }

                Button(
                    onClick = onAskAI,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldPrimary,
                        contentColor = BrownDark
                    )
                ) {
                    Text("Ask AI")
                }
            }

            Spacer(Modifier.height(12.dp))

            // ── EXPENSE BUTTON ─────────────────────────────────
            Button(
                onClick = onOpenExpense,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldPrimary,
                    contentColor = BrownDark
                )
            ) {
                Icon(Icons.Default.AttachMoney, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("Estimate Trip Cost")
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}