package uz.gita.newtztodo.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import uz.gita.newtztodo.R

class BottomDialog : BottomSheetDialogFragment() {
    private var deleteListener: (() -> Unit)? = null
    private var editListener: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.bottom_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<LinearLayout>(R.id.trasjLine).setOnClickListener {
            deleteListener?.invoke()
            dismiss()
        }
        view.findViewById<LinearLayout>(R.id.editLine).setOnClickListener {
            editListener?.invoke()
            dismiss()
        }
    }

    fun setDeletestener(f: () -> Unit) {
        deleteListener = f
    }

    fun setEdit(f: () -> Unit) {
        editListener = f
    }
}