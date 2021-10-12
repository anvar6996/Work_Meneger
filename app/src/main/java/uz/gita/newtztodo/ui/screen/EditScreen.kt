package uz.gita.newtztodo.ui.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.newtztodo.R
import uz.gita.newtztodo.base.data.TaskData
import uz.gita.newtztodo.databinding.EditScreenBinding
import uz.gita.newtztodo.ui.viewmodel.MainScreenViewModel
import java.util.*

@AndroidEntryPoint
class EditScreen : Fragment(R.layout.edit_screen) {
    private val binding by viewBinding(EditScreenBinding::bind)
    private val viewmodel: MainScreenViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var id = 0
        arguments?.let {
            val value = it.getSerializable("editvalue") as TaskData
            binding.nameTitleEdit.setText(value.title)
            binding.titleEdit.setText(value.description)
            id = value.id
            val text = value.time.split("  ")
            binding.timeTitleEdit.text = text[0]
            binding.dateTitleEdit.text = text[1]
        }
        binding.cancel.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.timeTitleEdit.setOnClickListener {
            val timePick = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12).setMinute(0).setTitleText("Choose Time").build()
            timePick.show(childFragmentManager, "To do")
            timePick.addOnPositiveButtonClickListener {
                if (timePick.minute < 10) {
                    binding.timeTitleEdit.text = "${timePick.hour}:0${timePick.minute}"
                } else {
                    binding.timeTitleEdit.text = "${timePick.hour}:${timePick.minute}"
                }
            }
        }

        binding.dateTitleEdit.setOnClickListener {
            val calendar = Calendar.getInstance()
            var time = ""
            val YEAR = calendar.get(Calendar.YEAR)
            val MONTH = calendar.get(Calendar.MONTH)
            val DATE = calendar.get(Calendar.DATE)
            val dialogPicker = android.app.DatePickerDialog(
                requireContext(),
                { _, year, month, date ->
                    var dateString = if (date < 10) {
                        "0$date"
                    } else "$date"
                    var monthString = if (month < 10) {
                        "0$month"
                    } else "$month"
                    var yearString = if (year < 10) {
                        "0$year"
                    } else "$year"
                    time = "$dateString/$monthString/$yearString"
                    binding.dateTitleEdit.text = time
                }, YEAR, MONTH, DATE
            )
            dialogPicker.show()
        }
        binding.add.setOnClickListener {
            if (binding.dateTitleEdit.text.toString()
                    .isNotEmpty() && binding.timeTitleEdit.text.toString()
                    .isNotEmpty() &&
                binding.nameTitleEdit.text.toString().isNotEmpty()
            ) {
                viewmodel.update(
                    TaskData(
                        id,
                        binding.nameTitleEdit.text.toString(),
                        binding.titleEdit.text.toString(),
                        binding.timeTitleEdit.text.toString() + "  " +
                                binding.dateTitleEdit.text.toString()
                    )
                )
                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), "Entered incorrectly", Toast.LENGTH_SHORT).show()
            }
        }

    }
}