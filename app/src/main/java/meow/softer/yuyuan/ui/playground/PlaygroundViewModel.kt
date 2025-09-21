package meow.softer.yuyuan.ui.playground

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import meow.softer.yuyuan.data.local.entiity.Sentence
import meow.softer.yuyuan.data.local.entiity.Word
import meow.softer.yuyuan.domain.HanziInfo
import meow.softer.yuyuan.domain.PlayHanziAudioUseCase
import meow.softer.yuyuan.domain.SaveProgressUseCase
import meow.softer.yuyuan.domain.StopAudioUseCase
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
    private val playHanziAudioUseCase: PlayHanziAudioUseCase,
    private val stopAudioUseCase: StopAudioUseCase,
    private val hanziQueue: HanziQueue
    ) : ViewModel() {
    private val viewModelState = MutableStateFlow(
        PlaygroundViewModelState(isLoading = true)
    )

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
            WhileSubscribed(5000),
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
        saveProgress(previousWord)
        pushNextAction()
    }

    private fun saveProgress(previousWord: Word?) {
        if (previousWord == null) return
        viewModelScope.launch {
            debug("saving progress for $previousWord")
            val newLearnt = saveProgressUseCase(previousWord)
            if (newLearnt > 0) {
                //todo: update data
            } else {
                //todo: display error
                debug("Error: saveProgress: newLearnt <= 0")
            }

        }

    }

    fun pushNextAction() {
        viewModelScope.launch {
            // check end
            val nextHanzi = hanziQueue.getHead()
            debug("result is : "+nextHanzi?.word?.character)
            if (nextHanzi ==null){
                // reached end of book
                debug("playVM is at end now")
                _navigateBack.value = true //todo : reset?
                stopAudio()
                return@launch
            }
            //not end, push next
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