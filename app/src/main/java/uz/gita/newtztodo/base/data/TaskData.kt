package uz.gita.newtztodo.base.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class TaskData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val description: String,
    val time: String,
) : Serializable
