package uz.gita.newtztodo.base.data

import androidx.room.*


@Dao
interface TaskDao {
//    @Query("SELECT * FROM TaskData")
//    fun getLiastByBositions(pos: Int): Flow<List<TaskData>>

    @Query("SELECT * FROM TaskData")
    fun getAll(): List<TaskData>

    @Query("SELECT * FROM TaskData WHERE TaskData.title LIKE :query")
    fun getSearch(query: String): List<TaskData>

    @Delete
    fun deleteTask(data: TaskData): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data: TaskData)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun insertForEdit(data: TaskData)

    @Update
    fun update(data: TaskData)
}