package meow.softer.yuyuan.ui.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import meow.softer.yuyuan.ui.playground.PlaygroundViewModel
import meow.softer.yuyuan.utils.debug
import javax.inject.Inject

class SharedViewModel @Inject constructor() : ViewModel() {
    private val _currentBookLearnt = MutableStateFlow(-1)
    private val _currentBookStatus = MutableStateFlow(-1)

    /**
     * Communicate between [MainViewModel] and [PlaygroundViewModel] for
     * live update of learning progress
     *
     * Default value is -1 which means no value set yet and should be ignored
     */
    val currentBookLearnt: StateFlow<Int> get() = _currentBookLearnt

    fun updateCurrentBookLearnt(newValue: Int) {
        _currentBookLearnt.value = newValue
        debug("updating bookLearnt = $newValue")
    }

    /**
     * Communicate between [MainViewModel] and [PlaygroundViewModel] for
     * notification of the change of current book
     *
     * Default value is -1 which means no value set yet and should be ignored
     */
    val currentBookStatus : StateFlow<Int> get() = _currentBookStatus

    fun notifyCurrentBookChange(newId: Int){
        _currentBookStatus.value = newId
    }

}