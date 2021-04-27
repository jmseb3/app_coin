package com.wonddak.coinaverage.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [CoinInfo::class, CoinDetail::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dbDao(): dbDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "coindb")
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                getInstance(context).dbDao().insertCouinInfoData(CoinInfo(1,"메인코인"))
                                getInstance(context).dbDao().insertCoinDetailData(CoinDetail(null,1))
                                getInstance(context).dbDao().insertCoinDetailData(CoinDetail(null,1))
                            }
                        }

                    })
                    .build()
            }.also {
                INSTANCE = it
            }
        }
    }
}