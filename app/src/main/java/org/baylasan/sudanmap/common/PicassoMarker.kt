package org.baylasan.sudanmap.common

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toBitmap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.squareup.picasso.Picasso.LoadedFrom
import com.squareup.picasso.Target

class PicassoMarker(var marker: Marker?) : Target {
    override fun hashCode(): Int {
        return marker.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return if (other is PicassoMarker) {
            val marker = other.marker
            this.marker == marker
        } else {
            false
        }
    }

    override fun onBitmapLoaded(bitmap: Bitmap, from: LoadedFrom) {

        marker?.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap))
    }

    override fun onBitmapFailed(
        e: Exception,
        errorDrawable: Drawable?
    ) {
        if (errorDrawable != null)
            marker?.setIcon(BitmapDescriptorFactory.fromBitmap(errorDrawable.toBitmap()))
    }

    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
        if (placeHolderDrawable != null)
            marker?.setIcon(BitmapDescriptorFactory.fromBitmap(placeHolderDrawable.toBitmap()))

    }

}