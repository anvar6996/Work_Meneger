package uz.gita.newtztodo.base.maneger

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import uz.gita.newtztodo.R


class WorkManagerToDo(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
//        setNotification(inputData.getInt("id", 200), inputData.getString("title") as String, inputData.getString("description") as String)
        setNotification(
            inputData.getInt("id", 101),
            inputData.getString("title") as String,
            inputData.getString("description") as String
        )
        return Result.success()

    }

    private fun setNotification(id: Int, title: String, description: String) {
        val manager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel("worker", "Work Manager", NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(channel)
        }

        val build = NotificationCompat.Builder(applicationContext, "worker")
            .setContentTitle(title)
            .setContentText(description)
            .setSmallIcon(R.drawable.ic_launcher_foreground)

        manager.notify(id, build.build())
    }
}