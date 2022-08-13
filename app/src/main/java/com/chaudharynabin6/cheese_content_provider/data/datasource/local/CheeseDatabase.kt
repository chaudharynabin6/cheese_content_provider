package com.chaudharynabin6.cheese_content_provider.data.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.chaudharynabin6.cheese_content_provider.data.datasource.local.dao.CheeseDao
import com.chaudharynabin6.cheese_content_provider.data.datasource.local.entities.Cheese

@Database(
    entities = [Cheese::class],
    version = 1
)
abstract class CheeseDatabase : RoomDatabase() {
    abstract val cheeseDao: CheeseDao

    companion object {
//        https://developer.android.com/codelabs/android-room-with-a-view-kotlin#7
        @Volatile
        private var database: CheeseDatabase? = null
        fun getDb(context: Context): CheeseDatabase {

            return database ?: synchronized(this) {
                val db = Room.databaseBuilder(
                    context,
                    CheeseDatabase::class.java,
                    "cheese.db"
                ).build()
                database = db
                db
            }
        }
    }
}