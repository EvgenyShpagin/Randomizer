package com.random.randomizer.presentation.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.random.randomizer.presentation.util.supportsTransparentNavigationBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (supportsTransparentNavigationBar()) {
            window.setNavigationBarContrastEnforced(false)
        }
        setContent {
            RandomizerApp()
        }
    }
}