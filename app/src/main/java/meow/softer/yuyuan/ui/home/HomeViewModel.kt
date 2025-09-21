package meow.softer.yuyuan.ui.home

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
import meow.softer.yuyuan.utils.ErrorMessage
import meow.softer.yuyuan.utils.debug
import java.util.UUID
import javax.inject.Inject

data class HomeUiState(
    val currentBook: BookInfo
)

/**
 * UI state for the home
 */
sealed interface HomeUiStateRaw {
    val isLoading: Boolean
    val errorMessages: List<ErrorMessage>

    /**
     * The learning data is loading so no plan info yet.
     */
    data class NoData(
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage>,
    ) : HomeUiStateRaw

    /**
     * The plan info data is loaded.
     */
    data class HasData(
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage>,
        val homeUiState: HomeUiState,
    ) : HomeUiStateRaw
}


/**
 * An internal representation of the Home state, in a raw form
 */
data class HomeViewModelState(
    val currentBook: BookInfo? = null,
    val isLoading: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList(),
    val audioSpeed: Float = 1f,
    val bookList: List<Book>? = null
) {
    /**
     * Converts this [HomeViewModelState] into a more strongly typed [HomeUiStateRaw] for driving
     * the ui.
     */
    fun toUiState(): HomeUiStateRaw =
        if (currentBook != null && bookList != null && !isLoading) {
            HomeUiStateRaw.HasData(
                isLoading = false,
                errorMessages = errorMessages,
                homeUiState = HomeUiState(
                    currentBook = currentBook
                )

            )
        } else {
            HomeUiStateRaw.NoData(
                isLoading = isLoading,
                errorMessages = errorMessages,
            )
        }
}

/**
 * Main ViewModel that hold states for home
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getBookUseCase: GetBookUseCase,
    private val getSettingsUseCase: GetSettingsUseCase,
) : ViewModel() {
    private val viewModelState = MutableStateFlow(
        HomeViewModelState(isLoading = true)
    )

    private var rawAudioSpeedCache: Float = 10f

    // UI state exposed to the UI
    val mainUiState = viewModelState
        .map(HomeViewModelState::toUiState)
        .stateIn(
            viewModelScope,
            WhileSubscribed(5000),
            viewModelState.value.toUiState()
        )

    init {
        debug("MainVM init{}...")
        initialize()
    }

    private fun initialize() {
        debug("Home VM initialize...")
        viewModelScope.launch {
            refresh()
        }

    }

    /**
     * Refresh data and update the UI state accordingly
     */
    fun refresh() {
        // UI is refreshing
        debug("Home VM UI Refreshing")
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
}

