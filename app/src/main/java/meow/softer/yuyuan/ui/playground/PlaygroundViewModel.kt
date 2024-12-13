package meow.softer.yuyuan.ui.playground

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import meow.softer.yuyuan.data.local.entiity.Sentence
import meow.softer.yuyuan.data.local.entiity.Word
import meow.softer.yuyuan.data.repository.session.SessionRepository
import javax.inject.Inject

@HiltViewModel
class PlaygroundViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private lateinit var _currentWord: MutableStateFlow<Word>
    val currentWord: StateFlow<Word>
        get() = _currentWord

    private lateinit var _currentSentence: MutableStateFlow<Sentence>
    val currentSentence: StateFlow<Sentence>
        get() = _currentSentence

    init {
        viewModelScope.launch {
            _currentWord = MutableStateFlow(sessionRepository.mockWord()!!)
            _currentSentence = MutableStateFlow(sessionRepository.mockSentence()!!)
        }
    }

    fun toggleStarred() {
        //TODO("Not yet implemented")
    }

    fun getStatisticTexts(): List<String> {
        // TODO("Not yet implemented")
        return listOf("0/50", "0/25", "10%")
    }


    fun playWordSound() {
        viewModelScope.launch {
            sessionRepository.playWordSound("word_audio/" + currentWord.value.charAudioFile)
        }
    }

    fun playSentenceSound() {
        viewModelScope.launch {
            sessionRepository.playSentenceSound("sentence_audio/" + currentSentence.value.sentenceAudioFile)
        }
    }

}