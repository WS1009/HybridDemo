package org.devio.`as`.proj.hi_jetpack.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CacheDao {
    @Query("select * from table_cache where `cache_key`=:keyword limit 1")
    fun query(keyword: String): List<Cache>

    //可以通过livedata以观察者的形式获取数据库数据，可以避免不必要的npe
    //更重要的是他可以监听数据表中的数据的变化，一旦发生insert、update、delete。room自动读取表中最新的数据发送给UI层，刷新页面
    @Query("select * from table_cache")
    fun query2(): LiveData<List<Cache>>

    @Delete()
    fun delete(cache: Cache)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cache: Cache)

    @Update()
    fun update(cache: Cache)
}