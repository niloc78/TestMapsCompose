package com.example.testmapscompose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapsComposeDemoRoutes() {
    val cameraPositionState = rememberCameraPositionState()
    GoogleMap(
        cameraPositionState = cameraPositionState,
        modifier = Modifier.fillMaxSize()
    ) {
        val baseLocation = LatLng(40.5931, -74.07834)
        val baseRadius = 1000.0
        val pointList = remember {
            mutableStateListOf<LatLng>().apply {
                //startpoint
                add(baseLocation)
                for(i in 1..5) {
                    add(getRandomLocation(baseLocation, baseRadius))
                }
            }
        }
        Polyline(
            points = pointList,
            color = Color.Magenta,
            width = 10f
        )
    }
}