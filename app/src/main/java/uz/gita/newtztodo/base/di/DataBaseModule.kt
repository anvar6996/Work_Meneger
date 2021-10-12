package uz.gita.newtztodo.base.di

import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.newtztodo.base.app.App
import uz.gita.newtztodo.base.data.AppBase
import uz.gita.newtztodo.base.data.TaskDao

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {

    @Provides
    fun getDataBase(): AppBase {
        return Room.databaseBuilder(App.instance, AppBase::class.java, "To do").build()
    }

    @Provides
    fun getTaskDao(base: AppBase): TaskDao {
        return base.getDao()
    }
}