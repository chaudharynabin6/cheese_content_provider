package com.chaudharynabin6.cheese_content_provider.presentation

import android.database.Cursor
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.loader.app.LoaderManager
import androidx.loader.app.LoaderManager.LoaderCallbacks
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import com.chaudharynabin6.cheese_content_provider.data.datasource.local.CheeseDatabase
import com.chaudharynabin6.cheese_content_provider.data.datasource.local.entities.Cheese
import com.chaudharynabin6.cheese_content_provider.databinding.ActivityMainBinding
import com.chaudharynabin6.cheese_content_provider.provider.CheeseProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        const val LOADER_CLASSEs = 1
    }

    @Inject
    lateinit var db: CheeseDatabase

    @Inject
    lateinit var adapter: CheeseAdapter

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch(Dispatchers.IO) {

            val id = db.cheeseDao.insert(
                Cheese(
                    name = "test"
                )
            )

            Timber.e("id $id")
        }

        setupRecyclerView()
        initializeLoaderManager()

    }

    private val mLoaderCallBacks = object : LoaderCallbacks<Cursor> {
        override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
            return CursorLoader(
                applicationContext,
                CheeseProvider.URI_CHEESE,
                arrayOf(Cheese.COLUMN_NAME, Cheese.COLUMN_ID),
                null,
                null,
                null
            )
        }

        override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
//          do here
//            we will get the data as cursor
            data?.let {
                adapter.setCheeses(it)
            }
        }

        override fun onLoaderReset(loader: Loader<Cursor>) {
//            do here
//            will get the reset the data we set
            adapter.setCheeses(null)
        }

    }

    private fun setupRecyclerView() {
        binding.rvMainActivity.apply {
            this.adapter = this@MainActivity.adapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initializeLoaderManager() {
        LoaderManager.getInstance(this).initLoader(
            LOADER_CLASSEs,
            null,
            mLoaderCallBacks
        )
    }
}