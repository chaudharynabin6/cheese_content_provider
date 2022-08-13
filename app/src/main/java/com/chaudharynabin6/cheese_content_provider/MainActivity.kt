package com.chaudharynabin6.cheese_content_provider

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.chaudharynabin6.cheese_content_provider.data.datasource.local.CheeseDatabase
import com.chaudharynabin6.cheese_content_provider.data.datasource.local.entities.Cheese
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var db: CheeseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch(Dispatchers.IO) {

            val id = db.cheeseDao.insert(
                Cheese(
                    name = "test"
                )
            )

            Timber.e("id $id")
        }

    }
}