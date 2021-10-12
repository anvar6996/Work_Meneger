package uz.gita.newtztodo.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import uz.gita.newtztodo.base.data.AppBase
import uz.gita.newtztodo.base.data.TaskData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor(val base: AppBase) : RepositoryMain {
    private val taskDao = base.getDao()

    override fun search(query: String): Flow<List<TaskData>> = flow {
        emit(taskDao.getSearch(query))
    }.flowOn(Dispatchers.Default)

    override fun loadBase(): Flow<List<TaskData>> = flow {
        emit(taskDao.getAll())
    }.flowOn(Dispatchers.Default)

    override suspend fun delete(value: TaskData) {
        taskDao.deleteTask(value)
    }

    override suspend fun update(value: TaskData) {
        taskDao.update(value)
    }

    override suspend fun add(value: TaskData) {

        taskDao.insert(value)
    }
}