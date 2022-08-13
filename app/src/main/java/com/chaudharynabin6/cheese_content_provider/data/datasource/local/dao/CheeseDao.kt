package com.chaudharynabin6.cheese_content_provider.data.datasource.local.dao

import android.database.Cursor
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.chaudharynabin6.cheese_content_provider.data.datasource.local.entities.Cheese

/*
* Data access object for cheese
* */
@Dao
interface CheeseDao {
    @Query("select count(*) from ${Cheese.TABLE_NAME}")
    fun count(): Int

    @Insert
    fun insert(cheese: Cheese): Long

    @Insert
    fun insertAll(cheeseList: List<Cheese>): List<Long>

    @Query("select * from ${Cheese.TABLE_NAME}")
    fun selectAll(): Cursor

    @Query("""
        select * from ${Cheese.TABLE_NAME}
        where ${Cheese.COLUMN_ID} = :id
    """)
    fun selectById(id: Long): Cursor

    @Query("""
        delete from ${Cheese.TABLE_NAME}
        where ${Cheese.COLUMN_ID} = :id
    """)
    fun deleteById(id: Long) : Int

    @Update
    fun update(cheese: Cheese): Int

    @RawQuery
    fun selectUsingRawQuery(query: SupportSQLiteQuery) : Cursor
}