package com.example.testmapscompose

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.delay

@Composable
fun MapsComposeDemoMarkers() {
    val url = "https://assets-global.website-files.com/5ff0b3326e5782a256714165/638b6f3a3d8d2e6bad1cb03b_Random22.1.jpg"
    
    val painter = rememberAsyncImagePainter(ImageRequest.Builder(LocalContext.current).data(url).transformations(CircleCropTransformation()).build())
    val context = LocalContext.current
    
    val tpBitmap = remember {
        val px = 400.dp.value.toInt()
        val mDotMarkerBitmap = Bitmap.createBitmap(px, px, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(mDotMarkerBitmap)
        val shape = context.getDrawable(R.drawable.accuracy_circle_tp_map)
        shape?.setBounds(0, 0, mDotMarkerBitmap.width, mDotMarkerBitmap.height)
        shape?.draw(canvas)
        mDotMarkerBitmap
    }

    val cameraPositionState = rememberCameraPositionState()

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
        MapEffect { map ->

        }

        var showMarker by remember {
            mutableStateOf(true)
        }
        var descriptor = remember {
            BitmapDescriptorFactory.fromBitmap(createTrackerIconBitmap(Color.Cyan))
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

        val animatedSize = animateDpAsState(targetValue = size, tween(5000))

        LaunchedEffect(Unit) {
            while(true) {
                delay(2000L)
                size = 100.dp
                for (i in markerPositionMap.entries.indices) {
                    if(markerPositionMap.containsKey(i)) {
                        val oldLatLng = markerPositionMap[i]!!
                        val newLatLng = getRandomLocation(oldLatLng, 1000000.0)
                        markerPositionMap[i] = newLatLng
                    }

                }

                //descriptor = BitmapDescriptorFactory.fromBitmap(createTrackerIconBitmap(Color.Red))

                showMarker = !showMarker
            }
        }
        for(index in 1..10) {
            if(!markerPositionMap.containsKey(index) && !markerMap.containsKey(index)) {
                markerPositionMap[index] = getRandomLocation(basePosition, baseRadius)
                markerMap[index] = rememberMarkerState(key = index.toString(), position = markerPositionMap[index]!!)
            } else {
                markerMap[index]?.position = markerPositionMap[index]!!
            }
            // Better practice to use visibility modifier instead of conditionals.
            // Noticed more frequent crashing when using conditionals due to markerState being reused
            MarkerComposable(
                visible = showMarker,
                state = markerMap[index]!!
            ) {
                Card(
                    modifier = Modifier
                        .size(animatedSize.value), // size must be > 0 or will crash
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = if(index % 2 == 0) Color.Red else Color.Blue
                    )
                ) {
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .size(animatedSize.value)
                            .clip(CircleShape)
                    )
                }
            }

//            Marker(
//                state = markerMap[index]!!,
//                icon = descriptor
//            )

        }
    }
}

@Composable
fun MarkerComposable2(visible: Boolean, state: MarkerState, size: Dp, color: Color) {
    MarkerComposable(
        visible = visible,
        state = state
    ) {
        Card(
            modifier = Modifier
                .size(size), // size must be > 0 or will crash
            shape = CircleShape,
            colors = CardDefaults.cardColors(
                containerColor = color
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_check_circle_24),
                contentDescription = null,
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
            )
        }

    }
    
}