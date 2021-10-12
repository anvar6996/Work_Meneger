package uz.gita.newtztodo.ui.screen

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.newtztodo.R
import uz.gita.newtztodo.adapter.TaskAdapter
import uz.gita.newtztodo.base.data.TaskData
import uz.gita.newtztodo.databinding.MainScreenBinding
import uz.gita.newtztodo.ui.dialog.BottomDialog
import uz.gita.newtztodo.ui.viewmodel.MainScreenViewModel

@AndroidEntryPoint
class MainScreen : Fragment(R.layout.main_screen) {
    private val binding by viewBinding(MainScreenBinding::bind)
    private var list = ArrayList<TaskData>()
    private lateinit var hendler: Handler
    private var querySt = ""
    private var adapter: TaskAdapter = TaskAdapter(list, querySt)
    private val viewModel: MainScreenViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyklerNotes.adapter = adapter
        binding.recyklerNotes.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        viewModel.load()
        loadStateInfo()
        viewModel.taskLiveData.observe(viewLifecycleOwner, taskLiveDataObserver)
        binding.buttonAdd.setOnClickListener {
            findNavController().navigate(R.id.action_mainScreen_to_addScreen)
        }
        adapter.setClickItem() { pos, value ->
            val dialog = BottomDialog()
            dialog.show(childFragmentManager, "Delete")
            dialog.setDeletestener {
                viewModel.delete(value)
                list.removeAt(pos)
                adapter.notifyItemRemoved(pos)
                loadStateInfo()
            }
            dialog.setEdit {
                val bundle = Bundle()
                bundle.putSerializable("editvalue", value)
                findNavController().navigate(R.id.action_mainScreen_to_editScreen, bundle)
            }
        }
        adapter.setOneCkick() { pos, value ->
            val bundle = Bundle()
            bundle.putSerializable("value", value)
            findNavController().navigate(R.id.action_mainScreen_to_showScreen, bundle)
        }
        hendler = Handler(Looper.getMainLooper())
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                hendler.removeCallbacksAndMessages(null)
                query?.let {
                    querySt = it.trim()
                    viewModel.search("%${querySt}%")
                    viewModel.taskLiveData.observe(viewLifecycleOwner, taskLiveDataObserver)
                    binding.searchView.setQuery(querySt, false)
                    loadStateInfo()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                hendler.removeCallbacksAndMessages(null)
                hendler.postDelayed({
                    newText?.let {
                        querySt = it.trim()
                        adapter.query = querySt
                        viewModel.search("%${querySt}%")
                        viewModel.taskLiveData.observe(viewLifecycleOwner, taskLiveDataObserver)
                        binding.searchView.setQuery(querySt, false)
                        loadStateInfo()
                    }
                }, 500)
                return true
            }
        })
        val searchText =
            binding.searchView.findViewById<View>(androidx.appcompat.R.id.search_src_text) as EditText
        searchText.setTextColor(resources.getColor(R.color.black))
        searchText.setHintTextColor(Color.GRAY)

        val closeButton = binding.searchView.findViewById(R.id.search_close_btn) as ImageView
        closeButton.setOnClickListener {
            closeSearch()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private val taskLiveDataObserver = Observer<List<TaskData>> {
        list.clear()
        list.addAll(it)
        loadStateInfo()
        adapter.notifyDataSetChanged()
    }

    fun loadStateInfo() {
        if (adapter.list.isEmpty()) {
            binding.Empty.visibility = View.VISIBLE
        } else {
            binding.Empty.visibility = View.GONE
        }
    }

    fun closeSearch() {
        binding.searchView.setQuery(null, false)
        binding.searchView.clearFocus()
    }
}
