package meow.softer.yuyuan.ui.playground

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import meow.softer.yuyuan.data.local.entiity.Sentence
import meow.softer.yuyuan.data.local.entiity.Word
import meow.softer.yuyuan.domain.GetHanziInfoUseCase
import meow.softer.yuyuan.domain.HanziInfo
import meow.softer.yuyuan.domain.PlayHanziAudioUseCase
import meow.softer.yuyuan.domain.StopAudioUseCase
import meow.softer.yuyuan.domain.SaveProgressUseCase
import meow.softer.yuyuan.ui.home.SharedViewModel
import meow.softer.yuyuan.utils.ErrorMessage
import meow.softer.yuyuan.utils.debug
import javax.inject.Inject


/**
 * UI state for the Playground.
 */
sealed interface PlaygroundUiState {
    val errorMessages: List<ErrorMessage>
    val isLoading: Boolean

    /**
     * NewWordScreen
     */
    data class HasData(
        override val errorMessages: List<ErrorMessage>,
        override val isLoading: Boolean,
        val hanziInfo: HanziInfo
    ) : PlaygroundUiState

    data class NoData(
        override val errorMessages: List<ErrorMessage>,
        override val isLoading: Boolean
    ) : PlaygroundUiState
}


/**
 * An internal representation of the playground state, in a raw form
 */
data class PlaygroundViewModelState(
    val isLoading: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList(),
    val currentWord: Word? = null,
    val currentSentence: Sentence? = null
) {
    /**
     * Converts this [PlaygroundViewModelState] into a more strongly typed [PlaygroundUiState] for driving
     * the ui.
     */
    fun toUiState(): PlaygroundUiState =
        (if (currentWord == null || currentSentence == null || isLoading) {
            PlaygroundUiState.NoData(
                errorMessages = errorMessages,
                isLoading = true
            )
        } else {
            PlaygroundUiState.HasData(
                errorMessages = errorMessages,
                isLoading = false,
                hanziInfo = HanziInfo(
                    word = currentWord,
                    sentence = currentSentence
                )
            )
        })

}


@HiltViewModel
class PlaygroundViewModel @Inject constructor(
    private val saveProgressUseCase: SaveProgressUseCase,
    private val getHanziInfoUseCase: GetHanziInfoUseCase,
    private val playHanziAudioUseCase: PlayHanziAudioUseCase,
    private val stopAudioUseCase: StopAudioUseCase
) : ViewModel() {
    private val viewModelState = MutableStateFlow(
        PlaygroundViewModelState(isLoading = true)
    )
    private lateinit var sharedViewModel: SharedViewModel

    private var hanziPool = mutableListOf<HanziInfo>()
    private val cacheMax = 12
    private val cacheMin = 4
    private var isAtEnd = false

    /**
     * There are two cases to navigate back: user clicks the back arrow or the words are all learnt.
     * In the later case, the UI needs to be notified by this state
     */
    private val _navigateBack = MutableStateFlow(false)
    val navigateBack: StateFlow<Boolean> = _navigateBack

    // UI state exposed to the UI
    val uiState = viewModelState
        .map(PlaygroundViewModelState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()

        )

    init {
        debug("PlayVM init{}...")
        viewModelScope.launch {
            // UI is refreshing
            viewModelState.update { it.copy(isLoading = true) }
            // Loading initial data
            pushNextAction()
            // Initialize audio settings
            playHanziAudioUseCase.initializeAudio()
        }

    }

    fun initSharedViewModel(viewModelSoreOwner: ViewModelStoreOwner) {
        debug("PlayVM initSharedViewModel()...")
        sharedViewModel = ViewModelProvider(viewModelSoreOwner)[SharedViewModel::class]
        viewModelScope.launch {
            // listen to setting change from other view models
            sharedViewModel.currentBookStatus.collect { value ->
                if (value != -1) {
                    invalidateCache()
                }
            }
        }
    }

    fun playWordSound() {
        viewModelScope.launch {
            playHanziAudioUseCase(viewModelState.value.currentWord!!)
        }
    }

    fun playSentenceSound() {
        viewModelScope.launch {
            playHanziAudioUseCase(viewModelState.value.currentSentence!!)
        }
    }

    fun onNextClick() {
        val previousWord = viewModelState.value.currentWord
        pushNextAction()
        saveProgress(previousWord)
    }

    private fun saveProgress(previousWord: Word?) {
        if (previousWord == null) return
        viewModelScope.launch {
            debug("saving progress for $previousWord")
            val newLearnt = saveProgressUseCase(previousWord)
            if (newLearnt > 0) {
                sharedViewModel.updateCurrentBookLearnt(newLearnt)
            } else {
                //todo: display error
                debug("saveProgress: newLearnt <= 0")
            }

        }

    }

    /**
     * Invalidate cache when user changes book
     */
    private fun invalidateCache() {
        hanziPool.clear()
        pushNextAction()
    }

    private suspend fun refillCache() {
        debug("refilling cache...")
        if (isAtEnd) return
        if (hanziPool.size <= cacheMin) {
            val newCache = getHanziInfoUseCase(cacheMax - hanziPool.size)
            if (newCache.isEmpty()) {
                isAtEnd = true
            } else {
                hanziPool = hanziPool.plus(newCache) as MutableList<HanziInfo>
                debug("refilled, size = ${hanziPool.size}")
            }
        }
    }


    fun pushNextAction() {
        viewModelScope.launch {
            // check end
            refillCache()
            if (isAtEnd) {
                // reached end of book
                _navigateBack.value = true //todo : reset?
                stopAudio()
                return@launch

            }
            //not end, push next
            val nextHanzi = hanziPool.removeAt(0)
            viewModelState.update {
                it.copy(
                    isLoading = false,
                    errorMessages = emptyList(),
                    currentWord = nextHanzi.word,
                    currentSentence = nextHanzi.sentence
                )
            }
        }

    }

    fun stopAudio() {
        debug("stop audio called")
        viewModelScope.launch {
            stopAudioUseCase()
        }

    }

}