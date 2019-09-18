package kr.dev.chu.camerapaging.paging

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import kr.dev.chu.camerapaging.paging.item.PhotoItem

class GalleryViewModel(application: Application): AndroidViewModel(application) {
    private val activity = application.applicationContext

    /**
     * PagedList :
     * DataSource 에서 가져온 데이터는 모두 PagedList 로 전달됩니다.
     * 데이터 로딩이 필요하면 DataSource 를 통해 가져옵니다.
     * 또한, UI에 데이터를 제공하는 역할을 합니다.
     *
     * LivePagedListBuilder :
     * PagedList를 생성하는 빌더입니다. 빌더는 LiveData로 리턴합니다.
     *
     * Placeholders :
     * 데이터가 로딩되지 않아 화면에 보여지지 않을 때, 가상의 객체를 미리 그리고 데이터 로딩이 완료될 때 실제 데이터를 보여주는 것을 말합니다.
     *
     * 장점 :
     * 1. 빠르게 스크롤 할 수 있다
     * 2. 스크롤바 위치가 정확하다
     * 3. 스피너 등으로 더 보기 같은 기능을 만들 필요가 없다
     * 조건 :
     * 1. 아이템이 보여지는 View의 크기가 동일해야 한다
     * 2. Adapter가 null을 처리해야 한다
     * 3. DataSource에서 제공하는 아이템의 개수가 정해져 있어야 한다
     */

    fun getImages(): LiveData<PagedList<PhotoItem>> {
        val dataSourceFactory = GalleryDataSourceFactory(activity.contentResolver)
        val pagedListConfig = PagedList.Config.Builder()
            .setPageSize(20)
            .setInitialLoadSizeHint(60)     // default : page size * 3
//            .setPrefetchDistance(20)        // default : page size
            .setEnablePlaceholders(false)   // default : true
            .build()

        val data = LivePagedListBuilder(dataSourceFactory, pagedListConfig).build()
        return data
    }
}