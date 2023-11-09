package com.example.testmapscompose

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.GroundOverlay
import com.google.maps.android.compose.GroundOverlayPosition
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapsComposeDemoShapes() {
    val cameraPositionState = rememberCameraPositionState()
    val context = LocalContext.current
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        properties = MapProperties(mapType = MapType.HYBRID),
        cameraPositionState = cameraPositionState
    ) {
        val baseLocation = LatLng(40.5931, -74.07834)
        val baseRadius = 100000.0
        //Draw a circle
        Circle(
            center = baseLocation,
            radius = baseRadius,
            strokeColor = Color.Green,
            fillColor = Color.Green.copy(alpha = .25f),
            onClick = {

            }
        )

        val tpGroundOverlayDescriptor = remember {
            val px = 400.dp.value.toInt()
            val mDotMarkerBitmap = Bitmap.createBitmap(px, px, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(mDotMarkerBitmap)
            val shape = context.getDrawable(R.drawable.accuracy_circle_tp_map)
            shape?.setBounds(0, 0, mDotMarkerBitmap.width, mDotMarkerBitmap.height)
            shape?.draw(canvas)
            BitmapDescriptorFactory.fromBitmap(mDotMarkerBitmap)
        }

        //Draw a ground overlay, have to pass in bitmap descriptor
        GroundOverlay(
            position = GroundOverlayPosition.create(baseLocation, (baseRadius * 2).toFloat()),
            image = tpGroundOverlayDescriptor,
        )

        //Draw a Polygon
//        val pointList = remember {
//            mutableStateListOf<LatLng>()
//                .apply {
//                    add(LatLng(40.0, -75.0))
//                    add(LatLng(41.0, -74.0))
//                    add(LatLng(40.0, -73.0))
//                    add(LatLng(39.0, -74.0))
//                    add(LatLng(39.0, -75.0))
//                }
//        }
//        Polygon(
//            points = pointList,
//            fillColor = Color.Blue.copy(alpha = .25f),
//            strokeColor = Color.Blue
//        )
    }
}