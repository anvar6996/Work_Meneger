package uz.gita.newtztodo.base.data

//import dagger.hilt.components.SingletonComponent
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.gita.newtztodo.base.app.App


@Database(entities = [TaskData::class], version = 3)
abstract class AppBase() : RoomDatabase() {

    abstract fun getDao(): TaskDao


//    companion object {
//        private lateinit var baza: AppBase
//
//        fun getSingle(): AppBase {
//            if (!Companion::baza.isInitialized) {
//                baza = Room.databaseBuilder(App.instance, AppBase::class.java, "To do")
//                    .allowMainThreadQueries().build()
//            }
//            return baza
//        }
//    }
}