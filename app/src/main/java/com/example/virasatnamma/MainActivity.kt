package com.example.virasatnamma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.virasatnamma.ui.navigation.VirasatNavGraph
import com.example.virasatnamma.ui.theme.VirasatNammaTheme
import com.example.virasatnamma.viewmodel.HeritageViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VirasatNammaTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val heritageViewModel: HeritageViewModel = viewModel()
                    VirasatNavGraph(viewModel = heritageViewModel)
                }
            }
        }
    }
}
