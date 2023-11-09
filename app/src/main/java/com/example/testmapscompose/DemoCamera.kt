package com.example.testmapscompose

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.launch

@Composable
fun MapsComposeDemoCamera() {
    val cameraPositionState = rememberCameraPositionState()
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        var showMarker by remember {
            mutableStateOf(true)
        }
        // a marker state can only be associated with one marker at a time.
        val markerPositionMap: SnapshotStateMap<Int, LatLng> = remember {
            mutableStateMapOf()
        }
        val basePosition = LatLng(0.0, 0.0)
        val baseRadius = 10000000.0

        val markerMap = remember {
            mutableMapOf<Int, MarkerState>()
        }

        var size by remember {
            mutableStateOf(25.dp)
        }

        val scope = rememberCoroutineScope()

        for(index in 1..10) {
            if(!markerPositionMap.containsKey(index) && !markerMap.containsKey(index)) {
                markerPositionMap[index] = getRandomLocation(basePosition, baseRadius)
                markerMap[index] = rememberMarkerState(key = index.toString(), position = markerPositionMap[index]!!)
            }
            // Better practice to use visibility modifier instead of conditionals.
            // Noticed more frequent crashing when using conditionals due to markerState being reused
            MarkerComposable(
                visible = showMarker,
                state = markerMap[index]!!,
                onClick = {
                    scope.launch {
                        cameraPositionState.animate(
                            CameraUpdateFactory.newCameraPosition(
                                CameraPosition.fromLatLngZoom(it.position, 20f)))
                    }
                    false
                }
            ) {
                Card(
                    modifier = Modifier
                        .size(size)
                        .animateContentSize(), // size must be > 0 or will crash
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = if(index % 2 == 0) Color.Red else Color.Blue
                    )
                ) {

                }
            }

        }
    }
}