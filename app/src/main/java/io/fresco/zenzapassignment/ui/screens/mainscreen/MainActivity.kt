package io.fresco.zenzapassignment.ui.screens.mainscreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import io.fresco.zenzapassignment.R
import io.fresco.zenzapassignment.ui.components.MainScreenComponent
import io.fresco.zenzapassignment.ui.components.TopTitleBar
import io.fresco.zenzapassignment.ui.theme.ZenZapAssignmentTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZenZapAssignmentTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopTitleBar(title = ContextCompat.getString(this, R.string.title_text))
                    },
                    content = { innerPadding ->
                        MainScreenComponent(modifier = Modifier.padding(innerPadding))
                    },
                )
            }
        }
    }
}
