package kr.dev.chu.camerapaging.view

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import androidx.recyclerview.widget.GridLayoutManager
import kr.dev.chu.camerapaging.R
import kr.dev.chu.camerapaging.base.BaseActivity
import kr.dev.chu.camerapaging.databinding.ActivityMainBinding
import kr.dev.chu.camerapaging.etc.actionBarHide
import kr.dev.chu.camerapaging.etc.showToast
import kr.dev.chu.camerapaging.paging.item.GalleryImageItem
import kr.dev.chu.camerapaging.paging.item.PhotoItem
import kr.dev.chu.camerapaging.paging.item.getGalleryItems
import kr.dev.chu.camerapaging.paging.GalleryDataSource
import kr.dev.chu.camerapaging.paging.GalleryPagedAdapter
import kr.dev.chu.camerapaging.paging.GalleryViewModel

private const val REQUEST_PERMISSION_CODE = 100

class MainActivity : BaseActivity<ActivityMainBinding>(),
    GalleryPagedAdapter.ACallback {

    override val TAG: String
        get() = MainActivity::class.java.simpleName

    override val getContentView: Int
        get() = R.layout.activity_main

    override fun initView() {
        Log.i(TAG, "initView")

        actionBarHide()

        getBinding().activity = this
    }

    override fun initFunc() {
        super.initFunc()
        Log.i(TAG, "initFunc")

        checkPermission()
    }

    // region 권한
    private fun checkPermission() {
        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            initGalleryItem()
        } else {
            requestPermission()
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                //거부했을 경우
                showToast("기능 사용을 위한 권한 동의가 필요합니다.1")
            } else {
                showToast("기능 사용을 위한 권한 동의가 필요합니다.2")
            }
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_PERMISSION_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 동의 했을 경우
                    initGalleryItem()
                } else {
                    // 거부 했을 경우
                    showToast("기능 사용을 위한 권한 동의가 필요합니다.")
                }
            }
        }
    }
    // endregion

    private fun initGalleryItem() {
//        setPaging()
        setPagingRx()
    }

    private fun setPaging() {
        /**
         * 데이터 흐름 :
         * PagedList는 DataSource를 이용하여 Local또는 Backend로부터 데이터를 가져옵니다.
         * 그 데이터는 PagedListAdapter로 전달되어 RecyclerView에 출력됩니다.
         *
         * PagedList는 어떻게 생성되나 :
         * PagedList는 DataSource를 이용하여 데이터를 가져오고 PagedListAdapter에 전달합니다.
         * 구조적으로 설명하면, PagedListAdapter는 PagedList를 데이터의 스냅샷으로 인식합니다.
         * 스냅샷이 찍혔을 때의 데이터를 화면에 보여줍니다.
         * 만약 데이터베이스에 데이터가 추가되는 경우, PagedList도 새로 생성되어야 합니다.
         * PagedListAdapter는 새로 변경된 PagedList의 데이터를 화면에 출력해줍니다.
         */

        val viewModel = ViewModelProviders.of(this).get(GalleryViewModel::class.java)
        val photoItemPagedList = viewModel.getImages()

        val adapter = GalleryPagedAdapter().apply {
            setCallback(this@MainActivity)
        }

        getBinding().mainRv.run {
            this.adapter = adapter
            this.layoutManager = GridLayoutManager(this@MainActivity, 2)
        }

        photoItemPagedList.observe(this, Observer<PagedList<PhotoItem>> { photoItemPagedList ->
            adapter.submitList(photoItemPagedList)
        })
    }

    private fun setPagingRx() {
        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(60)
            .setPageSize(20)
//            .setPrefetchDistance(5)
            .setEnablePlaceholders(false)
            .build()

        val builder = RxPagedListBuilder<Int, PhotoItem>(object : DataSource.Factory<Int, PhotoItem>() {
            override fun create(): DataSource<Int, PhotoItem> {
                return GalleryDataSource(contentResolver)
            }
        }, config)

        val adapter = GalleryPagedAdapter().apply {
            setCallback(this@MainActivity)
        }

        getBinding().mainRv.run {
            this.adapter = adapter
            this.layoutManager = GridLayoutManager(this@MainActivity, 2)
        }

        builder.buildObservable()
            .subscribe {
                adapter.submitList(it)
            }
    }

    override fun onClickEvent(
        position: Int,
        uri: Uri
    ) {
        getGalleryItems()[position] = GalleryImageItem(position, uri)
    }

    override fun onClickLongEvent(uri: Uri) {
        ImageDialog(this, uri).show()
    }
}
