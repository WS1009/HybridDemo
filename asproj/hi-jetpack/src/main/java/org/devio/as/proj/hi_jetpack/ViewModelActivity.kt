package org.devio.`as`.proj.hi_jetpack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.*

class ViewModelActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_model)

        val viewModel = ViewModelProvider(this).get(HiViewModel::class.java)
        viewModel.loadInitData().observe(this, Observer {
            //渲染列表
        })
    }
}


class HiViewModel(private val saveState: SavedStateHandle) : ViewModel() {
    private val KEY_HOME_PAGE_DATA = "key_home_page_data"
    private val liveData = MutableLiveData<List<GoodsModel>>()

    fun loadInitData(): LiveData<List<GoodsModel>> {
        //1. from memory .
        if (liveData.value == null) {
            val memoryData = saveState.get<List<GoodsModel>>(KEY_HOME_PAGE_DATA)
            liveData.postValue(memoryData)
        }

        // 2 . from remote
        // 为了适配因配置变更而导致的页面重建，重复利用之前的数据，加快新页面的渲染，不再请求接口
        if (liveData.value == null) {
            val remoteData = fetchDataFromRemote()
            saveState.set(KEY_HOME_PAGE_DATA, remoteData)
            liveData.postValue(remoteData)
        }
        return liveData
    }

    private fun fetchDataFromRemote(): Any {
        return arrayOf(GoodsModel)
    }

}

class GoodsModel {

}
