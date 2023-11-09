package com.example.testmapscompose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@Composable
fun MapsComposeDemoClusters() {
    val cameraPositionState = rememberCameraPositionState()
    val scope = rememberCoroutineScope()
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
        val baseRadius = 100000.0

        val markerMap = remember {
            mutableMapOf<Int, MarkerState>()
        }

        var size by remember {
            mutableStateOf(25.dp)
        }

        //items have to extend ClusterItem
        val items = remember { mutableStateListOf<MyClusterItem>() }
        LaunchedEffect(Unit) {
            for(i in 1..10) {
                val pos = getRandomLocation(basePosition, baseRadius)
                items.add(MyClusterItem(pos, "Marker", "Snippet", 0f))
            }
        }
        Clustering(
            items = items,
            //custom render of cluster
            clusterContent = {
                val items = it.items
                Card(
                    shape = CircleShape,
                    modifier = Modifier.size(50.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Blue)
                ) {

                }
            },
            //custom render of non-clusterd item
            clusterItemContent = {
                Card(
                    shape = CircleShape,
                    modifier = Modifier.size(25.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Red)
                ) {

                }
            },
            onClusterClick =  {
                scope.launch {
                    val items = it.items
                    val bounds = getLatLngBoundsOfClusterItems(items)
                    val newPosition = CameraUpdateFactory.newLatLngBounds(
                        bounds, 100
                    )
                    cameraPositionState.animate(newPosition)
                }

                true
            }
        )
    }
}

data class MyClusterItem(
    val itemPosition: LatLng,
    val itemTitle: String,
    val itemSnippet: String,
    val itemIndex: Float
) : ClusterItem {
    override fun getPosition() = itemPosition

    override fun getTitle() = itemTitle

    override fun getSnippet() = itemSnippet

    override fun getZIndex() = itemIndex

}

private fun getLatLngBoundsOfClusterItems(items: MutableCollection<MyClusterItem>): LatLngBounds {
    return LatLngBounds.Builder()
        .include(
            items.getBottomMostPoint()
        )
        .include(
            items.getTopMostPoint()
        )
        .include(
            items.getLeftMostPoint()
        )
        .include(
            items.getRightMostPoint()
        )
        .build()
}