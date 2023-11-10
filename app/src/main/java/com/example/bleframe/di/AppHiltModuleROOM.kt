package com.example.bleframe.di

import android.content.Context
import androidx.room.Room
import com.example.bleframe.data.room.RoomDataBaseApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/*****************************************************************************/
/****                             Room                                 * *****/
/*****************************************************************************/

@Module
@InstallIn(SingletonComponent::class)
object AppHiltModuleROOM {

    @Provides
    fun providesNameBD() = "bd_app"

    @Singleton
    @Provides
    fun providesInMemoryDatabase(@ApplicationContext applicationContext: Context
    ) = Room.inMemoryDatabaseBuilder(
        applicationContext,
        RoomDataBaseApp::class.java
    ).build()

    /*@Singleton
    @Provides
    fun provideGetDatabase(
        @ApplicationContext applicationContext: Context,
        name: String
    ) = Room.databaseBuilder(
        applicationContext,
        RoomDataBaseApp::class.java,
        name
    )
        /*.addMigrations(MIGRATION_1_2)*/
        .build()*/

    @Singleton
    @Provides
    fun providesDao(db: RoomDataBaseApp) = db.getDataDao()
}
