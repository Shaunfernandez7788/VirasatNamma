package com.example.virasatnamma.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.virasatnamma.ui.theme.BrownDark
import com.example.virasatnamma.ui.theme.BrownMedium
import com.example.virasatnamma.ui.theme.GoldPrimary
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinished: () -> Unit) {

    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 800, easing = EaseInOut),
        label = "alpha"
    )
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "scale"
    )

    LaunchedEffect(Unit) {
        delay(2400L)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(BrownDark, BrownMedium)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .alpha(alpha)
                .scale(scale)
        ) {
            Icon(
                imageVector = Icons.Default.AccountBalance,
                contentDescription = "Logo",
                tint = GoldPrimary,
                modifier = Modifier.size(96.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Virasat-Namma",
                color = GoldPrimary,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                text = "Guide",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = 4.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Discover Karnataka's Living Heritage",
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                letterSpacing = 0.5.sp
            )
        }
    }
}
