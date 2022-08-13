package com.chaudharynabin6.cheese_content_provider.di

import android.content.Context
import androidx.room.Room
import com.chaudharynabin6.cheese_content_provider.data.datasource.local.CheeseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesCheeseDatabase(
        @ApplicationContext context: Context,
    ): CheeseDatabase {
        return Room.databaseBuilder(
            context,
            CheeseDatabase::class.java,
            "cheese.db"
        ).build()
    }
}