package uz.gita.newtztodo.domain

import kotlinx.coroutines.flow.Flow
import uz.gita.newtztodo.base.data.TaskData
import javax.inject.Singleton

@Singleton
interface RepositoryMain {

    fun loadBase(): Flow<List<TaskData>>//load
    suspend fun delete(value: TaskData)//delete
    suspend fun update(value: TaskData)//update
    suspend fun add(value: TaskData)//add
    fun search(query: String): Flow<List<TaskData>>//search

}