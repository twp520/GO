package com.colin.go.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.colin.go.bean.DATABASE_NAME
import com.colin.go.bean.WeatherInfo

/**
 * create by colin
 * 2020/8/19
 */

@Database(entities = [WeatherInfo::class], version = 1, exportSchema = false)
abstract class GoDatabase : RoomDatabase() {

    abstract fun getWeatherDao(): WeatherDao


    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: GoDatabase? = null

        fun getInstance(context: Context): GoDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): GoDatabase {
            return Room.databaseBuilder(context, GoDatabase::class.java, DATABASE_NAME)
                .build()
        }
    }
}