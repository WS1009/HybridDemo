package org.devio.`as`.proj.hi_jetpack.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Cache::class, User::class], version = 1, exportSchema = true)
abstract class CacheDatabase : RoomDatabase() {

    abstract val cacheDao: CacheDao

    companion object {
        private var database: CacheDatabase? = null

        @Synchronized
        fun get(context: Context): CacheDatabase {

            if (database == null) {
                //内存数据库，这种数据库当中存储的数据只会存储在内存当中，进程杀死之后数据随之消失
                //database = Room.inMemoryDatabaseBuilder(context, CacheDatabase::class.java).build()

                database = Room.databaseBuilder(context, CacheDatabase::class.java, "jetpack")
                    //允许在主线程操作数据库，默认是不允许的，如果在主线程中操作了数据库会直接报错
                    .allowMainThreadQueries()
                    .addCallback(callback)
                    //制定数据查询时的线程池
                    .setQueryExecutor {}
                    //用来创建openhelper，可以哟用它自行创建supportsplliteopenopenhelper来实现数据库的加密
                    .openHelperFactory()
                    .addMigrations(migration1_2)
                    .build()
            }

            return database!!
        }

        val callback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
            }
        }

        val migration1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("alter table table_cache add column cache_time")
            }
        }
    }


}