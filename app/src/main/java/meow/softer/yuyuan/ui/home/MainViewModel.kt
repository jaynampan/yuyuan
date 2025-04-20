package meow.softer.yuyuan.ui.home

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.observeOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import meow.softer.yuyuan.R
import meow.softer.yuyuan.data.local.entiity.Book
import meow.softer.yuyuan.utils.ErrorMessage
import meow.softer.yuyuan.utils.debug
import meow.softer.yuyuan.domain.BookInfo
import meow.softer.yuyuan.domain.GetBookUseCase
import meow.softer.yuyuan.domain.GetSettingsUseCase
import meow.softer.yuyuan.domain.UpdateSettingsUseCase
import meow.softer.yuyuan.domain.UpdateSettingsUseCase.ConfigType
import meow.softer.yuyuan.domain.session.SessionUseCase
import java.util.UUID
import javax.inject.Inject

data class HomeUiState(
    val currentBook: BookInfo
)

data class SettingUiState(
    val currentBookId: Int,
    val bookList: List<Book>,
    val currentSpeed: Float
)

/**
 * UI state for the home
 */
sealed interface MainUiState {
    val isLoading: Boolean
    val errorMessages: List<ErrorMessage>

    /**
     * The learning data is loading so no plan info yet.
     */
    data class NoData(
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage>,
    ) : MainUiState

    /**
     * The plan info data is loaded.
     */
    data class HasData(
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage>,
        val homeUiState: HomeUiState,
        val settingUiState: SettingUiState
    ) : MainUiState
}


/**
 * An internal representation of the Home state, in a raw form
 */
data class MainViewModelState(
    val currentBook: BookInfo? = null,
    val isLoading: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList(),
    val audioSpeed: Float = 1f,
    val bookList: List<Book>? = null
) {
    /**
     * Converts this [MainViewModelState] into a more strongly typed [MainUiState] for driving
     * the ui.
     */
    fun toUiState(): MainUiState =
        if (currentBook != null && bookList != null && !isLoading) {
            MainUiState.HasData(
                isLoading = false,
                errorMessages = errorMessages,
                homeUiState = HomeUiState(
                    currentBook = currentBook
                ),
                settingUiState = SettingUiState(
                    currentBookId = currentBook.bookId,
                    bookList = bookList,
                    currentSpeed = audioSpeed
                )
            )
        } else {
            MainUiState.NoData(
                isLoading = isLoading,
                errorMessages = errorMessages,
            )
        }
}

/**
 * Main ViewModel that hold states for home
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val getBookUseCase: GetBookUseCase,
    private val getSettingsUseCase: GetSettingsUseCase,
    private val updateSettingsUseCase: UpdateSettingsUseCase
) : ViewModel() {
    private lateinit var sharedViewModel: SharedViewModel
    private val viewModelState = MutableStateFlow(
        MainViewModelState(isLoading = true)
    )

    private var rawAudioSpeedCache: Float = 10f

    // UI state exposed to the UI
    val mainUiState = viewModelState
        .map(MainViewModelState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    init {
        debug("MainVM init{}...")
        initialize()
    }

    fun initSharedViewModel(viewModelSoreOwner: ViewModelStoreOwner) {
        debug("MainVM initSharedViewModel...")
        sharedViewModel = ViewModelProvider(viewModelSoreOwner)[SharedViewModel::class]
        viewModelScope.launch {
            // observe updates from other view models
            sharedViewModel.currentBookLearnt.collect { value ->
                debug("MainVM new learnt : collecting $value")
                if (value != -1) {
                    val bookInfo = viewModelState.value.currentBook
                    if (bookInfo != null) {
                        debug("newLearnt collected: $value")
                        viewModelState.update {
                            it.copy(
                                currentBook = bookInfo.copy(learntWords = value)
                            )
                        }
                    }

                }
            }
        }
    }

    private fun initialize() {
        debug("MainVM initialize...")
        viewModelScope.launch {
            refresh()
        }

    }

    /**
     * Refresh data and update the UI state accordingly
     */
    fun refresh() {
        // UI is refreshing
        debug("MainVM UI Refreshing")
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
            sharedViewModel.notifyCurrentBookChange(bookId)
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

