package io.link.minify.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import io.link.minify.ui.mainScreen.MainScreen
import io.link.minify.ui.theme.LinkMinifyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LinkMinifyTheme {
                MainScreen()
            }
        }
    }
}
