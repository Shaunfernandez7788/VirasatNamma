package com.example.virasatnamma.ui.screens.scanner

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.virasatnamma.ui.theme.*
import com.google.accompanist.permissions.*
import com.google.mlkit.vision.barcode.*
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScannerScreen(onSiteFound: (String) -> Unit) {

    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    if (cameraPermissionState.status.isGranted) {
        CameraPreview(onSiteFound)
    } else {
        PermissionDeniedContent {
            cameraPermissionState.launchPermissionRequest()
        }
    }
}

@SuppressLint("UnsafeOptInUsageError")
@Composable
fun CameraPreview(onSiteFound: (String) -> Unit) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var scanned by remember { mutableStateOf(false) }

    AndroidView(
        factory = { ctx ->

            val previewView = PreviewView(ctx)

            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

            cameraProviderFuture.addListener({

                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                // 🔥 IMPORTANT: Enable only QR scanning (faster + accurate)
                val options = BarcodeScannerOptions.Builder()
                    .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                    .build()

                val scanner = BarcodeScanning.getClient(options)

                val executor = Executors.newSingleThreadExecutor()

                val analysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()

                analysis.setAnalyzer(executor) { imageProxy ->

                    if (scanned) {
                        imageProxy.close()
                        return@setAnalyzer
                    }

                    val mediaImage = imageProxy.image

                    if (mediaImage != null) {

                        val image = InputImage.fromMediaImage(
                            mediaImage,
                            imageProxy.imageInfo.rotationDegrees
                        )

                        scanner.process(image)
                            .addOnSuccessListener { barcodes ->

                                if (barcodes.isNotEmpty()) {

                                    val rawValue = barcodes[0].rawValue

                                    Log.d("QR", "Detected: $rawValue")

                                    val siteId = extractSiteId(rawValue ?: "")

                                    if (siteId != null && !scanned) {
                                        scanned = true
                                        onSiteFound(siteId)
                                    }
                                }
                            }
                            .addOnFailureListener {
                                Log.e("QR", "Scan failed: ${it.message}")
                            }
                            .addOnCompleteListener {
                                imageProxy.close()
                            }

                    } else {
                        imageProxy.close()
                    }
                }

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        analysis
                    )
                } catch (e: Exception) {
                    Log.e("QR", "Camera binding failed", e)
                }

            }, ContextCompat.getMainExecutor(ctx))

            previewView
        },
        modifier = Modifier.fillMaxSize()
    )

    // Overlay UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrownDark.copy(alpha = 0.4f)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(250.dp)
                .border(
                    3.dp,
                    Brush.linearGradient(listOf(GoldPrimary, Color.White, GoldPrimary)),
                    RoundedCornerShape(16.dp)
                )
        )
    }
}

private fun extractSiteId(raw: String): String? {

    val clean = raw.trim()

    if (clean.startsWith("virasat://")) {
        return clean.removePrefix("virasat://")
    }

    return if (clean.matches(Regex("site_\\d{3}"))) clean else null
}

@Composable
fun PermissionDeniedContent(onRequest: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(BrownDark, BrownMedium))),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.QrCodeScanner,
            contentDescription = null,
            tint = GoldPrimary,
            modifier = Modifier.size(80.dp)
        )

        Spacer(Modifier.height(20.dp))

        Text(
            "Camera Permission Required",
            color = GoldPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Spacer(Modifier.height(20.dp))

        Button(onClick = onRequest) {
            Text("Grant Permission")
        }
    }
}