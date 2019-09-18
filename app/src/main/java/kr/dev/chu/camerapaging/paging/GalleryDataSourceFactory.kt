package kr.dev.chu.camerapaging.paging

import android.content.ContentResolver
import android.provider.MediaStore
import android.util.Log
import androidx.paging.DataSource
import androidx.paging.PositionalDataSource
import kr.dev.chu.camerapaging.paging.item.PhotoItem
import kr.dev.chu.camerapaging.paging.item.initGalleryItems

/**
 * DataSource.Factory:
 * DataSource를 생성하는 역할
 */

class GalleryDataSourceFactory(private val contentResolver: ContentResolver) : DataSource.Factory<Int, PhotoItem>() {
    override fun create(): DataSource<Int, PhotoItem> {
        return GalleryDataSource(contentResolver)
    }
}

/**
 * 참고 싸이트 : https://codechacha.com/ko/android-jetpack-paging/
 *
 * DataSource
 * "데이터를 로딩"하는 객체"로 로컬 또는 Backend의 데이터를 가져오는 역할입니다.
 *
 * DataSource의 파생클래스
 * PositionalDataSource: 위치기반의 데이터를 로딩하는 DataSource입니다.
 *      ** 셀 수 있는 데이터, 고정된 사이즈의 데이터를 로딩할 때 사용됩니다.
 *      만약 끝을 알 수 없는 무한대의 아이템이라면, ItemKeyedDataSource 또는 PageKeyedDataSource이 적합합니다.
 *      ** Room은 PositionalDataSource 타입의 소스를 제공합니다.
 * ItemKeyedDataSource: 키 기반의 아이템을 로딩하는 DataSource입니다.
 * PageKeyedDataSource: 페이지 기반의 아이템을 로딩하는 DataSource입니다.
 *
 * 공통점 : 데이터를 가져온다는 것
 * 차이점 : 데이터 덩어리를 가져오는 방식이 다르다는 것
 *
 * PositionalDataSource
 * 특정 위치(index)에서 원하는 개수만큼 데이터를 가져올 수 있다면 PositionalDataSource 를 적용
 * loadInitial : 처음 데이터를 가져올 때 호출되는 함수
 * loadRange : 다음 데이터를 가져올 때 호출
 */

class GalleryDataSource(private val contentResolver: ContentResolver) : PositionalDataSource<PhotoItem>() {
    private val TAG: String = GalleryDataSource::class.java.simpleName

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<PhotoItem>) {
        Log.i(TAG, "loadInitial start: ${params.requestedStartPosition}, size: ${params.requestedLoadSize}")

        callback.onResult(getImages(params.requestedLoadSize, params.requestedStartPosition), 0)
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<PhotoItem>) {
        Log.i(TAG, "loadRange start: ${params.startPosition}, size: ${params.loadSize}")
        callback.onResult(getImages(params.loadSize, params.startPosition))
    }

    // 특정 포지션으로부터 원하는 만큼의 데이터를 이곳에서 로드
    private fun getImages(limit: Int, offset: Int): MutableList<PhotoItem> {
        val photos: MutableList<PhotoItem> = mutableListOf()

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE
        )
        val orderBy = MediaStore.Video.Media.DATE_TAKEN
        val sortOrder = "$orderBy DESC LIMIT $limit OFFSET $offset"
        val imageCursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection, null, null, sortOrder
        )

        if (imageCursor != null) {
            if (imageCursor.moveToFirst()) {
                do {
                    val imgData = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA)
                    val imageDataPath = imageCursor.getString(imgData)
                    photos.add(PhotoItem(imageDataPath))
                    imageCursor.moveToNext()
                } while (imageCursor.moveToNext())
            }
            imageCursor.close()
        }

        photos.size.initGalleryItems()

        return photos
    }
}