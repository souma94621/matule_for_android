package com.example.myapplication

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.myapplication.di.AppContainer
import com.example.myapplication.presentation.navigation.AppNavigation
import com.example.uikit.theme.MatuleTheme

class MainActivity : ComponentActivity() {

    private lateinit var container: AppContainer

    private val notificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        container = (application as MatuleApp).container
        notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)

        setContent {
            MatuleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(container)
                }
            }
        }
    }

    /** Пользователь вернулся в приложение: напоминание «давно не заходили» больше не нужно. */
    override fun onStart() {
        super.onStart()
        container.reminders.cancelAppInactivity()
    }

    /** Приложение ушло в фон: через минуту напомним о себе (один раз до следующего входа). */
    override fun onStop() {
        super.onStop()
        container.reminders.scheduleAppInactivity()
    }
}