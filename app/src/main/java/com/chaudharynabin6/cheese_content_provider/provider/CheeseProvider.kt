package com.chaudharynabin6.cheese_content_provider.provider

import android.content.*
import android.database.Cursor
import android.net.Uri
import androidx.sqlite.db.SimpleSQLiteQuery
import com.chaudharynabin6.cheese_content_provider.data.datasource.local.CheeseDatabase
import com.chaudharynabin6.cheese_content_provider.data.datasource.local.entities.Cheese
import java.util.concurrent.Callable

class CheeseProvider : ContentProvider() {


    lateinit var cheeseDatabase: CheeseDatabase

    companion object {
        const val AUTHORITY = "com.chaudharynabin6.cheese_content_provider.provider"
        val URI_CHEESE = Uri.parse(
            "content://$AUTHORITY/${Cheese.TABLE_NAME}"
        )
        const val CODE_CHEESE_DIR = 100
        const val CODE_CHEESE_ITEM = 101
        val MATCHER = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, Cheese.TABLE_NAME, CODE_CHEESE_DIR)
            addURI(AUTHORITY, "${Cheese.TABLE_NAME}/#", CODE_CHEESE_ITEM)
        }
    }


    override fun onCreate(): Boolean {
        cheeseDatabase = context?.let { CheeseDatabase.getDb(context = it) }!!
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?,
    ): Cursor {
        val code = MATCHER.match(uri)

        val constructedQuery = """
              select ${
            projection?.reduce { acc, s ->
                "$acc , $s"
            } ?: "*"
        } from ${Cheese.TABLE_NAME} 
                                ${
            if (selection != null) {
                " where $selection"
            } else ""
        }   
        ${
            if (sortOrder != null) {
                "order by $sortOrder"
            } else
                ""
        }
        
               """.trimIndent()
        return when (code) {
            CODE_CHEESE_DIR -> {
                cheeseDatabase.cheeseDao.selectUsingRawQuery(
                    query = SimpleSQLiteQuery(
                        constructedQuery,
                        selectionArgs
                    )

                )
            }

            CODE_CHEESE_ITEM -> {
                cheeseDatabase.cheeseDao.selectById(ContentUris.parseId(uri))
            }
            else -> {
                throw IllegalArgumentException("Unknown URI: $uri")
            }
        }
    }

    override fun getType(uri: Uri): String {
        return when (MATCHER.match(uri)) {
            CODE_CHEESE_DIR -> {
                "vnd.android.cursor.dir/$AUTHORITY.${Cheese.TABLE_NAME}"
            }
            CODE_CHEESE_ITEM -> {
                "vnd.android.cursor.item/$AUTHORITY.${Cheese.TABLE_NAME}"
            }
            else -> {
                throw IllegalArgumentException("Unknown URI: $uri")
            }
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return when (MATCHER.match(uri)) {
            CODE_CHEESE_DIR -> {
                values?.let {
                    val id = Cheese.fromContentValues(values)
                        ?.let { cheese ->
                            cheeseDatabase.cheeseDao.insert(cheese)
                        }
                    context?.contentResolver?.notifyChange(uri, null)
                    ContentUris.withAppendedId(uri, id ?: return null)
                }
            }
            CODE_CHEESE_ITEM -> {
                throw IllegalArgumentException("Invalid URI, cannot insert with ID: $uri")
            }
            else -> {
                throw IllegalArgumentException("Unknown URI: $uri")
            }
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {

        return when (MATCHER.match(uri)) {
            CODE_CHEESE_DIR -> {
                throw IllegalArgumentException("Invalid URI, cannot update without ID$uri")
            }
            CODE_CHEESE_ITEM -> {
                val count = cheeseDatabase.cheeseDao.deleteById(
                    ContentUris.parseId(uri)
                )
                context?.contentResolver?.notifyChange(uri, null)
                count
            }
            else -> {
                throw IllegalArgumentException("Unknown URI: $uri")
            }
        }
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?,
    ): Int {

        return when (MATCHER.match(uri)) {
            CODE_CHEESE_DIR -> throw IllegalArgumentException("Invalid URI, cannot update without ID$uri")

            CODE_CHEESE_ITEM -> {
                val count = values?.let {
                    Cheese.fromContentValues(values)?.let { cheese ->
                        cheeseDatabase.cheeseDao.update(cheese)
                    }
                }
                count ?: 0
            }
            else -> {
                throw IllegalArgumentException("Unknown URI: $uri")
            }
        }
    }

    override fun applyBatch(operations: ArrayList<ContentProviderOperation>): Array<ContentProviderResult> {
        return cheeseDatabase.runInTransaction(
            Callable { this@CheeseProvider.applyBatch(operations) }
        )
    }

    override fun bulkInsert(uri: Uri, values: Array<out ContentValues>): Int {

        when (MATCHER.match(uri)) {
            CODE_CHEESE_DIR -> {
                val listCheese = values.mapNotNull {
                    Cheese.fromContentValues(it)
                }
                return cheeseDatabase.cheeseDao.insertAll(cheeseList = listCheese).size
            }
            CODE_CHEESE_ITEM -> {
                throw IllegalArgumentException("Invalid URI, cannot insert with ID: $uri")
            }
            else -> {
                throw IllegalArgumentException("Unknown URI: $uri")
            }
        }
    }
}