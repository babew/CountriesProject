package lyubomir.babev.countries.project.extensions

import android.widget.ImageView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import lyubomir.babev.countries.project.R

fun ImageView.loadSvg(url: String) {
    val imageLoader = ImageLoader.Builder(this.context)
        .components { add(SvgDecoder.Factory()) }
        .build()

    val request = ImageRequest.Builder(this.context)
        .crossfade(true)
        .crossfade(500)
        .placeholder(R.drawable.loading_animation)
        .error(R.drawable.ic_broken_image)
        .data(url)
        .target(this)
        .build()

    imageLoader.enqueue(request)
}