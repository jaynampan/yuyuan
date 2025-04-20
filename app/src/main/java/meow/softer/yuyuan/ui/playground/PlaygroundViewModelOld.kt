package meow.softer.yuyuan.ui.playground

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import meow.softer.yuyuan.utils.ErrorMessage
import meow.softer.yuyuan.utils.debug
import meow.softer.yuyuan.data.local.entiity.Sentence
import meow.softer.yuyuan.data.local.entiity.Word
import meow.softer.yuyuan.data.repository.media.toSentenceAudioPath
import meow.softer.yuyuan.data.repository.media.toWordAudioPath
import meow.softer.yuyuan.domain.session.ActionType
import meow.softer.yuyuan.domain.session.SessionUseCaseOld
import meow.softer.yuyuan.domain.statistic.StatisticUseCase
import javax.inject.Inject

private val MyTag = PlaygroundViewModelOld::class.simpleName

/**
 * UI state for the Playground.
 */
sealed interface PlaygroundUiStateOld {
    val errorMessages: List<ErrorMessage>
    val isLoading: Boolean

    /**
     * NewWordScreen
     */
    data class NewWordScreen(
        override val errorMessages: List<ErrorMessage>,
        override val isLoading: Boolean,
        val statisticTexts: List<String>,
        val currentWord: Word,
        val currentSentence: Sentence
    ) : PlaygroundUiStateOld

    data class NoData(
        override val errorMessages: List<ErrorMessage>,
        override val isLoading: Boolean
    ) : PlaygroundUiStateOld
}


/**
 * An internal representation of the playground state, in a raw form
 */
data class PlaygroundViewModelStateOld(
    val isLoading: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList(),
    val currentWord: Word? = null,
    val currentSentence: Sentence? = null,
    val statisticTexts: List<String>? = null
) {
    /**
     * Converts this [PlaygroundViewModelStateOld] into a more strongly typed [PlaygroundUiStateOld] for driving
     * the ui.
     */
    fun toUiState(): PlaygroundUiStateOld =
        if (currentWord == null || currentSentence == null || statisticTexts == null || isLoading) {
            PlaygroundUiStateOld.NoData(
                errorMessages = errorMessages,
                isLoading = true
            )
        } else {
            PlaygroundUiStateOld.NewWordScreen(
                errorMessages = errorMessages,
                currentWord = currentWord,
                currentSentence = currentSentence,
                statisticTexts = statisticTexts,
                isLoading = false
            )
        }

}


@HiltViewModel
class PlaygroundViewModelOld @Inject constructor(
    private val sessionUseCaseOld: SessionUseCaseOld,
    private val statisticUseCase: StatisticUseCase
) : ViewModel() {
    private val viewModelState = MutableStateFlow(
        PlaygroundViewModelStateOld(isLoading = true)
    )
    private val _navigateBack = MutableStateFlow(false)
    val navigateBack: StateFlow<Boolean> = _navigateBack

    // UI state exposed to the UI
    val uiState = viewModelState
        .map(PlaygroundViewModelStateOld::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()

        )

    init {
        refreshData()
    }

    fun refreshData() {
        debug("prepared: ${sessionUseCaseOld.isPlanPrepared.value}", MyTag)
        if(!sessionUseCaseOld.isPlanPrepared.value){
            return
        }
        // UI is refreshing
        viewModelState.update { it.copy(isLoading = true) }
        // Fetch data from plan repository

        viewModelScope.launch {
            val action = sessionUseCaseOld.getNextAction()
            if (action.type == ActionType.End) {
                _navigateBack.value = true
                return@launch
            }
            val currentWord = if (action.type == ActionType.Ok) {
                action.word!!
            } else {
                sessionUseCaseOld.mockWord()!!
            }
            val currentSentence = if (action.type == ActionType.Ok) {
                action.sentence!!
            } else {
                //TODO: show error
                sessionUseCaseOld.mockSentence()!!
            }
            val statisticTexts = statisticUseCase.getStatisticBarTexts()
            viewModelState.update {
                viewModelState.value.copy(
                    isLoading = false,
                    currentWord = currentWord,
                    currentSentence = currentSentence,
                    statisticTexts = statisticTexts,
                )
            }
        }
    }

    fun toggleStarred() {
        sessionUseCaseOld.toggleStarred()
    }

    fun playWordSound() {
        viewModelScope.launch {
            sessionUseCaseOld.playWordSound(viewModelState.value.currentWord!!.audioFile.toWordAudioPath())
        }
    }

    fun playSentenceSound() {
        viewModelScope.launch {
            sessionUseCaseOld.playSentenceSound(viewModelState.value.currentSentence!!.audioFile.toSentenceAudioPath())
        }
    }

    fun searchWord() {
        TODO("Not yet implemented")
    }

    fun showMoreMenu() {
        TODO("Not yet implemented")
    }

    fun goNext() {
        refreshData()
    }

}