package com.example.testmapscompose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings

@Composable
fun MapsComposeDemoControls() {
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        // Can set default settings
        uiSettings = MapUiSettings(
            zoomControlsEnabled = true,
            zoomGesturesEnabled = true,
        ),
        // Can set default properties
        properties = MapProperties(
            maxZoomPreference = 20f,
            minZoomPreference = 3f,
            isMyLocationEnabled = true,
            mapType = MapType.SATELLITE
        )
    ) {
        //Would have to use MapEffect to get access to map object to edit positions of controls
        MapEffect { map ->
            map.setPadding(0, 100, 0, 100)
        }
    }
}