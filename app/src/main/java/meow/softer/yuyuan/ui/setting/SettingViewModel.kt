package meow.softer.yuyuan.ui.setting


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import meow.softer.yuyuan.R
import meow.softer.yuyuan.data.local.entiity.Book
import meow.softer.yuyuan.domain.BookInfo
import meow.softer.yuyuan.domain.GetBookUseCase
import meow.softer.yuyuan.domain.GetSettingsUseCase
import meow.softer.yuyuan.domain.UpdateSettingsUseCase
import meow.softer.yuyuan.domain.UpdateSettingsUseCase.ConfigType
import meow.softer.yuyuan.utils.ErrorMessage
import meow.softer.yuyuan.utils.debug
import java.util.UUID
import javax.inject.Inject


data class SettingUiState(
    val currentBookId: Int,
    val bookList: List<Book>,
    val currentSpeed: Float
)

sealed interface SettingUiStateRaw {
    val isLoading: Boolean
    val errorMessages: List<ErrorMessage>

    data class NoData(
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage>,
    ) : SettingUiStateRaw


    data class HasData(
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage>,
        val settingUiState: SettingUiState
    ) : SettingUiStateRaw
}

data class SettingViewModelState(
    val isLoading: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList(),
    val currentBook: BookInfo? = null,
    val audioSpeed: Float = 1f,
    val bookList: List<Book>? = null
) {
    fun toUiState(): SettingUiStateRaw =
        if (currentBook != null && bookList != null && !isLoading) {
            SettingUiStateRaw.HasData(
                isLoading = false,
                errorMessages = errorMessages,
                settingUiState = SettingUiState(
                    currentBookId = currentBook.bookId,
                    bookList = bookList,
                    currentSpeed = audioSpeed
                )
            )
        } else {
            SettingUiStateRaw.NoData(
                isLoading = isLoading,
                errorMessages = errorMessages,
            )
        }
}

/**
 * SettingViewModel that hold states for SettingScreen
 */
@HiltViewModel
class SettingViewModel @Inject constructor(
    private val getBookUseCase: GetBookUseCase,
    private val updateSettingsUseCase: UpdateSettingsUseCase,
    private val getSettingsUseCase: GetSettingsUseCase
) : ViewModel() {
    private val viewModelState = MutableStateFlow(
        SettingViewModelState(isLoading = true)
    )

    private var rawAudioSpeedCache: Float = 10f

    // UI state exposed to the UI
    val settingUiStates = viewModelState
        .map(SettingViewModelState::toUiState)
        .stateIn(
            viewModelScope,
            WhileSubscribed(5000),
            viewModelState.value.toUiState()
        )

    init {
        debug("SettingVM init{}...")
        initialize()
    }

    private fun initialize() {
        refresh()
    }

    fun refresh() {
        viewModelState.update { it.copy(isLoading = true) }

        // Fetch data from
        viewModelScope.launch {
            val userSetting = getSettingsUseCase()
            val bookInfo = getBookUseCase(userSetting.currentBookId)
            val bookList = getBookUseCase()
            viewModelState.update {
                if (userSetting.currentBookId != 0) {
                    debug("UI has data $bookInfo  $userSetting")
                    rawAudioSpeedCache = userSetting.currentAudioSpeed * 10
                    it.copy(
                        isLoading = false,
                        currentBook = bookInfo,
                        audioSpeed = userSetting.currentAudioSpeed,
                        bookList = bookList,
                    )

                } else {
                    debug("Cant get UI data")
                    val errorMessages = it.errorMessages + ErrorMessage(
                        id = UUID.randomUUID().mostSignificantBits,
                        messageId = R.string.load_plan_info_err
                    )
                    it.copy(errorMessages = errorMessages, isLoading = false)
                }
            }
        }
    }

    fun onBookChosen(bookId: Int) {
        viewModelScope.launch {
            // update ui state
            viewModelState.update {
                it.copy(
                    currentBook = getBookUseCase(bookId)
                )
            }
            // update setting
            updateSettingsUseCase(ConfigType.CURRENT_BOOK, bookId)
            // notify other view models
//            sharedViewModel.notifyCurrentBookChange(bookId)
            //todo: update data
        }
    }

    fun onRawSpeedChosen() {
        viewModelScope.launch {
            val realSpeed = (rawAudioSpeedCache + 0.1).toInt() / 10f
            // update ui state
            viewModelState.update {
                it.copy(
                    audioSpeed = realSpeed
                )
            }
            // update setting
            updateSettingsUseCase(ConfigType.CURRENT_SPEED, realSpeed)
        }
    }

    fun onRawSpeedChange(rawSpeed: Float) {
        rawAudioSpeedCache = rawSpeed
    }
}