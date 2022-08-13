package com.chaudharynabin6.cheese_content_provider.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.chaudharynabin6.cheese_content_provider.data.datasource.local.entities.Cheese
import com.chaudharynabin6.cheese_content_provider.data.datasource.local.dao.CheeseDao

@Database(
    entities = [Cheese::class],
    version = 1
)
abstract class CheeseDatabase : RoomDatabase() {
    abstract val cheeseDao: CheeseDao
}