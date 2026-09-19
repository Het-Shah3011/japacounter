package com.japa.counter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.japa.counter.data.database.AppDatabase
import com.japa.counter.data.repository.DeityRepository
import com.japa.counter.ui.screens.AppNavigation
import com.japa.counter.ui.theme.JapaCounterTheme
import com.japa.counter.ui.viewmodel.CounterViewModel
import com.japa.counter.utils.VibrationHelper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(applicationContext)
        val repository = DeityRepository(database.deityDao())
        val vibrationHelper = VibrationHelper(applicationContext)

        setContent {
            JapaCounterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: CounterViewModel = viewModel(
                        factory = CounterViewModel.Factory(repository, vibrationHelper)
                    )
                    AppNavigation(viewModel = viewModel)
                }
            }
        }
    }
}
