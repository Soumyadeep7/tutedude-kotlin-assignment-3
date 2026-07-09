package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.ui.navigation.CampusCartNavGraph
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Retrieve ViewModelFactory from our custom Application container
        val app = application as CampusCartApplication
        val factory = app.container.viewModelFactory
        
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CampusCartNavGraph(
                        viewModelFactory = factory,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
