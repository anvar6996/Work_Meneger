package uz.gita.newtztodo.ui.screen

import android.content.*
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.newtztodo.R
import uz.gita.newtztodo.base.data.TaskData
import uz.gita.newtztodo.databinding.ShowScreenBinding

@AndroidEntryPoint
class ShowScreen : Fragment(R.layout.show_screen) {

    private val binding by viewBinding(ShowScreenBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments.let {
            val value = it?.getSerializable("value") as TaskData
            binding.nameTitleShow.text = value.title
            binding.titleShow.text = value.description
            val text = value.time.split("  ")
            binding.timeTitleShow.text = text[0]
            binding.dateTitleShow.text = text[1]
        }
        binding.copy.setOnClickListener {
            (requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).apply {
                setPrimaryClip(
                    ClipData.newPlainText(
                        "simple text", "${binding.nameTitleShow.text}\n" +
                                "${binding.titleShow.text}\n" +
                                "${binding.timeTitleShow.text} ${binding.dateTitleShow.text}"
                    )
                )
                Toast.makeText(requireContext(), "COPY", Toast.LENGTH_SHORT).show()
            }
        }
        binding.send.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plaing"
            var strAppLink = ""
            strAppLink = try {
                "${binding.nameTitleShow.text}\n" +
                        "${binding.titleShow.text}\n" +
                        "${binding.timeTitleShow.text} ${binding.dateTitleShow.text}"
            } catch (anfe: ActivityNotFoundException) {
                "${binding.nameTitleShow.text}\n" +
                        "${binding.titleShow.text}\n" +
                        "${binding.timeTitleShow.text} ${binding.dateTitleShow.text}"
            }
            val shareSub = "Professional Definition"
            intent.putExtra(Intent.EXTRA_SUBJECT, shareSub)
            intent.putExtra(Intent.EXTRA_TEXT, strAppLink)
            startActivity(Intent.createChooser(intent, "Share Using"))
        }
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}