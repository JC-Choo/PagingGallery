package kr.dev.chu.camerapaging.paging

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_gallery.*
import kr.dev.chu.camerapaging.R
import kr.dev.chu.camerapaging.base.BaseViewHolder
import kr.dev.chu.camerapaging.paging.item.PhotoItem
import kr.dev.chu.camerapaging.paging.item.getGalleryItems

class GalleryPagedAdapter : PagedListAdapter<PhotoItem, GalleryPagedAdapter.ViewHolder>(
    diffCallback
) {
    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<PhotoItem>() {
            override fun areItemsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean = oldItem.imageDataPath == newItem.imageDataPath

            override fun areContentsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean = oldItem.imageDataPath == newItem.imageDataPath
        }
    }

    interface ACallback {
        fun onClickEvent(position: Int,
                         uri: Uri)
        fun onClickLongEvent(uri: Uri)
    }

    private var callback: ACallback? = null

    fun setCallback(callback: ACallback) {
        this.callback = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { item ->
            with(holder) {
                val uri = item.imageDataPath.toUri()

                Glide.with(holder.containerView.context)
                    .load(item.imageDataPath)
                    .into(ivGallery)

//                ivGallery.setImageURI(uri)

                ivGallery.setOnClickListener {
                    callback?.onClickEvent(position, uri)
                }

                ivGallery.setOnLongClickListener {
                    callback?.onClickLongEvent(uri)
                    return@setOnLongClickListener true
                }
            }
        }
    }

    inner class ViewHolder(parent: ViewGroup):
        BaseViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_gallery, parent, false))
}