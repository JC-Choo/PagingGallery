package kr.dev.chu.camerapaging.paging.item

import android.net.Uri

data class GalleryImageItem(
    var position: Int,
    var uri: Uri? = null
)

private val result: MutableList<GalleryImageItem> = mutableListOf()

fun Int.initGalleryItems(): MutableList<GalleryImageItem> {
    for(i in 0 until this) {
        result.add(GalleryImageItem(i, null))
    }

    return result
}

fun getGalleryItems() = result