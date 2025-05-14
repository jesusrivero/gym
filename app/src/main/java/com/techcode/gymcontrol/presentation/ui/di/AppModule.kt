package com.techcode.gymcontrol.presentation.ui.di

import android.content.Context
import androidx.room.Room
import com.techcode.gymcontrol.data.db.AppDatabase
import com.techcode.gymcontrol.data.db.dao.UsuariosDatabaseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "db_usuarios"
        ).build()
    }

    @Provides
    fun provideUsuariosDao(database: AppDatabase): UsuariosDatabaseDao {
        return database.usuariosDao()
    }
}
