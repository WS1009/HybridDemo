package org.devio.`as`.proj.hi_jetpack.room

import android.graphics.Bitmap
import androidx.annotation.NonNull
import androidx.room.*

@Entity(tableName = "table_cache")
class Cache {

    @PrimaryKey(autoGenerate = false)
    @NonNull
    var cache_key: String = ""

    @ColumnInfo(name = "cacheId")
    var cache_id: Long = 0

    @Ignore
    var bitmap: Bitmap? = null

    @Embedded//如果想让和当前对象中的字段也一同映射成数据库表中的字段，可以使用这个注解
    //要求User对象必须也使用Entity注解标记，并且拥有一个不为空的主键
    var user: User? = null
}

class User {
    @PrimaryKey
    @NonNull
    var name: String = ""
    var age = 10
}