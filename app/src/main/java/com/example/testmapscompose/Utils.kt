package com.example.testmapscompose

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.location.Location
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import java.util.Collections
import java.util.Random


fun getRandomLocation(point: LatLng, radius: Double): LatLng {
    val randomPoints: MutableList<LatLng> = ArrayList()
    val randomDistances: MutableList<Float> = ArrayList()
    val myLocation = Location("")
    myLocation.setLatitude(point.latitude)
    myLocation.setLongitude(point.longitude)

    //This is to generate 10 random points
    for (i in 0..9) {
        val x0 = point.latitude
        val y0 = point.longitude
        val random = Random()

        val radiusInDegrees = (radius / 111000f)

        // Convert radius from meters to degrees
        val u: Double = random.nextDouble()
        val v: Double = random.nextDouble()
        val w = radiusInDegrees * Math.sqrt(u)
        val t = 2 * Math.PI * v
        val x = w * Math.cos(t)
        val y = w * Math.sin(t)

        // Adjust the x-coordinate for the shrinking of the east-west distances
        val new_x = x / Math.cos(y0)
        val foundLatitude = new_x + x0
        val foundLongitude = y + y0
        val randomLatLng = LatLng(foundLatitude, foundLongitude)
        randomPoints.add(randomLatLng)
        val l1 = Location("")
        l1.setLatitude(randomLatLng.latitude)
        l1.setLongitude(randomLatLng.longitude)
        randomDistances.add(l1.distanceTo(myLocation))
    }
    //Get nearest point to the centre
    val indexOfNearestPointToCentre = randomDistances.indexOf(Collections.min(randomDistances))
    return randomPoints[indexOfNearestPointToCentre]
}

fun MutableCollection<MyClusterItem>.getTopMostPoint(): LatLng {
    return this.maxBy { it.itemPosition.latitude }.itemPosition
}

fun MutableCollection<MyClusterItem>.getBottomMostPoint(): LatLng {
    return this.minBy { it.itemPosition.latitude }.itemPosition
}

fun MutableCollection<MyClusterItem>.getLeftMostPoint(): LatLng {
    return this.minBy { it.itemPosition.longitude }.itemPosition
}

fun MutableCollection<MyClusterItem>.getRightMostPoint(): LatLng {
    return this.maxBy { it.itemPosition.longitude }.itemPosition
}

fun createTrackerIconBitmap(color: Color): Bitmap {
    val width = 80f
    val height = 80f

    val halfWidth = width / 2
    val halfHeight = height / 2
    val outerWhiteCircleRadius = Math.min(halfWidth, halfHeight)

    val innerColoredCircleRadius = Math.min(35f, 35f)

    val innerWhiteCircleRadius = Math.min(15f, 15f)

    val newBitmap = Bitmap.createBitmap(
        width.toInt(),
        height.toInt(),
        Bitmap.Config.ARGB_8888
    )
    // Center coordinates of the image
    val centerX = halfWidth
    val centerY = halfHeight

    val paint = Paint()
    val canvas = Canvas(newBitmap).apply {
        // Set transparent initial area
        drawARGB(0, 0, 0, 0)
    }

    // Draw the transparent initial area
    paint.isAntiAlias = true
    paint.style = Paint.Style.FILL
    canvas.drawCircle(centerX, centerY, outerWhiteCircleRadius, paint)

    //white outer circle
    paint.xfermode = null
    paint.style = Paint.Style.FILL
    paint.color = Color.White.toArgb()
    canvas.drawCircle(centerX, centerY, outerWhiteCircleRadius, paint)

    //inner colored circle
    paint.xfermode = null
    paint.style = Paint.Style.FILL
    paint.color = color.toArgb()
    canvas.drawCircle(centerX, centerY, innerColoredCircleRadius, paint)

    //white outer circle
    paint.xfermode = null
    paint.style = Paint.Style.FILL
    paint.color = Color.White.toArgb()
    canvas.drawCircle(centerX, centerY, innerWhiteCircleRadius, paint)

    return newBitmap
}