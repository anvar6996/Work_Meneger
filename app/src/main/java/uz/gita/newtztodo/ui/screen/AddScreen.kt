package uz.gita.newtztodo.ui.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.android.material.timepicker.TimeFormat.CLOCK_24H
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.newtztodo.R
import uz.gita.newtztodo.base.data.TaskData
import uz.gita.newtztodo.base.maneger.WorkManagerToDo
import uz.gita.newtztodo.databinding.AddScreenBinding
import uz.gita.newtztodo.ui.viewmodel.MainScreenViewModel
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class AddScreen : Fragment(R.layout.add_screen) {
    private val binding by viewBinding(AddScreenBinding::bind)
    private val viewModel: MainScreenViewModel by viewModels()
    private val calendarNot: Calendar = Calendar.getInstance()

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.timeTitle.setOnClickListener {
            val timePick = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_12H)
                .setTimeFormat(CLOCK_24H)
                .setMinute(60).setTitleText("Choose Time").build()
            timePick.show(childFragmentManager, "To do")
            timePick.addOnPositiveButtonClickListener {
                if (timePick.minute < 10) {
                    binding.timeTitle.text = "${timePick.hour}:0${timePick.minute}"
                } else {

                    binding.timeTitle.text = "${timePick.hour}:${timePick.minute}"
                }
                calendarNot.set(Calendar.HOUR_OF_DAY, timePick.hour + 12)
                calendarNot.set(Calendar.MINUTE, timePick.minute)
            }
        }

        binding.add.setOnClickListener {
            if (binding.dateTitle.text.toString().isNotEmpty() && binding.timeTitle.text.toString()
                    .isNotEmpty() &&
                binding.nameTitle.text.toString().isNotEmpty()
            ) {
                viewModel.add(
                    TaskData(
                        0,
                        binding.nameTitle.text.toString(),
                        binding.title.text.toString(), binding.timeTitle.text.toString() + "  " +
                                binding.dateTitle.text.toString()
                    )
                )

                val notificationTime: Long = calendarNot.timeInMillis - System.currentTimeMillis()
                Log.d("TTT", notificationTime.toString())
                Log.d("TTT", calendarNot.timeInMillis.toString())
                Log.d("TTT", System.currentTimeMillis().toString())
                Toast.makeText(requireContext(), notificationTime.toString(), Toast.LENGTH_SHORT)
                    .show()
                if (notificationTime > 0) {
                    Toast.makeText(requireContext(), "if ", Toast.LENGTH_SHORT).show()
                    val uploadWorkRequest = OneTimeWorkRequest.Builder(WorkManagerToDo::class.java)
                        .setInitialDelay(notificationTime, TimeUnit.MILLISECONDS)
                    val data = Data.Builder()
                    data.putInt("id", 0)
                    data.putString("title", "${binding.nameTitle.text}")
                    data.putString("description", "${binding.title.text}")

                    uploadWorkRequest.setInputData(data.build())

                    WorkManager
                        .getInstance(requireContext())
                        .enqueue(uploadWorkRequest.build())

                }

                findNavController().popBackStack()
            }
        }

        binding.cancel.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.dateTitle.setOnClickListener {
            val calendar = Calendar.getInstance()
            var time = ""
            val YEAR = calendar.get(Calendar.YEAR)
            val MONTH = calendar.get(Calendar.MONTH)
            val DATE = calendar.get(Calendar.DATE)
            val dialogPicker = android.app.DatePickerDialog(
                requireContext(),
                { _, year, month, date ->
                    val dateString = if (date < 10) {
                        "0$date"
                    } else "$date"
                    val monthString = if (month < 10) {
                        "0$month"
                    } else "$month"
                    val yearString = if (year < 10) {
                        "0$year"
                    } else "$year"
                    time = "$dateString/$monthString/$yearString"
                    binding.dateTitle.text = time
                }, YEAR, MONTH, DATE
            )
            dialogPicker.show()
            calendarNot.set(
                Calendar.YEAR,
                Calendar.MONTH,
                Calendar.DAY_OF_MONTH,
                dialogPicker.datePicker.year,
                dialogPicker.datePicker.year,
                dialogPicker.datePicker.dayOfMonth
            )
        }
    }


    fun emptyClick(word: String): Boolean {
        return word.isEmpty()
    }
}