package com.techcode.gymcontrol.infraestructure.di

import androidx.room.Room
import com.techcode.gymcontrol.data.db.AppDatabase
import com.techcode.gymcontrol.data.db.dao.UsuariosDatabaseDao
import com.techcode.gymcontrol.data.repository.UsuarioRepositoryIMPL
import com.techcode.gymcontrol.domain.repository.UsuarioRepository
import com.techcode.gymcontrol.infraestructure.MyApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
	
	@Provides
	
	fun provideAppDatabase(): AppDatabase {
		return Room.databaseBuilder(
			MyApp.myApp.baseContext,
			AppDatabase::class.java,
			"db_usuarios"
		).build()
	}
	
	@Provides
	fun provideUsuariosDao(database: AppDatabase): UsuariosDatabaseDao {
		return database.usuariosDao()
	}
	
	@Provides
	fun provideUsuarioRepository(dao: UsuariosDatabaseDao): UsuarioRepository {
		return UsuarioRepositoryIMPL(dao)
	}
	
}
