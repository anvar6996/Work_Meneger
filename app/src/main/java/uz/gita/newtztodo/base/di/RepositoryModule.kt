package uz.gita.newtztodo.base.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.newtztodo.domain.RepositoryImpl
import uz.gita.newtztodo.domain.RepositoryMain

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun getAppRepository(impl: RepositoryImpl): RepositoryMain

}