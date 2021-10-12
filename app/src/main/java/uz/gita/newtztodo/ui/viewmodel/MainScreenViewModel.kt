package uz.gita.newtztodo.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.gita.newtztodo.base.data.TaskData
import uz.gita.newtztodo.domain.RepositoryMain
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(private val repository: RepositoryMain) :
    ViewModel() {

    private val _taskLiveData = MutableLiveData<List<TaskData>>()
    val taskLiveData: LiveData<List<TaskData>> get() = _taskLiveData

    fun load() {
        repository.loadBase().onEach {
            _taskLiveData.value = it
        }.launchIn(viewModelScope)
    }

    fun delete(value: TaskData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(value)
        }
    }

    fun add(value: TaskData) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("TTT", value.description + "   " + value.id)
            repository.add(value)
        }
    }

    fun update(value: TaskData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(value)
        }
    }

    fun search(query: String) {
        repository.search(query).onEach {
            _taskLiveData.value = it
        }.launchIn(viewModelScope)
    }

}
