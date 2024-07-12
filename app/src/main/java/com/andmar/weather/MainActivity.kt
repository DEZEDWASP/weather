package com.andmar.weather

import android.Manifest
import android.os.Bundle
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andmar.weather.ui.theme.MyComposeApplicationTheme
import androidx.core.app.ActivityCompat
import com.andmar.weather.location.hasLocationPermission


var MY_PERMISSION_REQUEST_CODE = 0

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyComposeApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background) {
                    getPermission()
                    WeatherApp(
                        onFinish = {
                            finish()
                        }
                    )
                }
            }
        }
    }
    
    private fun getPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            ),
            MY_PERMISSION_REQUEST_CODE
       ) 
    }
}