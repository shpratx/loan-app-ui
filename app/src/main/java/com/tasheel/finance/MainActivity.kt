package com.tasheel.finance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.tasheel.finance.ui.navigation.NavGraph
import com.tasheel.finance.ui.theme.TasheelTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TasheelTheme {
                val navController = rememberNavController()
                NavGraph(navController)
            }
        }
    }
}
