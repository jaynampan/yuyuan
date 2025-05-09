package meow.softer.yuyuan.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import meow.softer.yuyuan.R
import meow.softer.yuyuan.utils.ErrorMessage
import meow.softer.yuyuan.data.Result
import meow.softer.yuyuan.data.repository.plan.PlanRepository
import meow.softer.yuyuan.domain.PlanInfo
import meow.softer.yuyuan.domain.session.SessionUseCaseOld
import java.util.UUID
import javax.inject.Inject

/**
 * UI state for the Home route.
 */
sealed interface HomeUiStateOld {
    val isLoading: Boolean
    val errorMessages: List<ErrorMessage>
    val searchInput: String

    /**
     * The learning data is loading so no plan info yet.
     */
    data class NoPlan(
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage>,
        override val searchInput: String,
    ) : HomeUiStateOld

    /**
     * The plan info data is loaded.
     */
    data class HasPLan(
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage>,
        override val searchInput: String,
        val planInfo: PlanInfo
    ) : HomeUiStateOld
}




/**
 * An internal representation of the Home route state, in a raw form
 */
data class HomeViewModelStateOld(
    val planInfo: PlanInfo? = null,
    val isLoading: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList(),
    val searchInput: String = "",
) {
    /**
     * Converts this [HomeViewModelStateOld] into a more strongly typed [HomeUiStateOld] for driving
     * the ui.
     */
    fun toUiState(): HomeUiStateOld =
        if (planInfo == null) {
            HomeUiStateOld.NoPlan(
                isLoading = isLoading,
                errorMessages = errorMessages,
                searchInput = searchInput
            )
        } else {
            HomeUiStateOld.HasPLan(
                planInfo = planInfo,
                isLoading = isLoading,
                errorMessages = errorMessages,
                searchInput = searchInput
            )
        }
}

/**
 * ViewModel that handles Logic for Home Screen
 */
@HiltViewModel
class HomeViewModelOld @Inject constructor(
    private val planRepository: PlanRepository,
    private val sessionUseCaseOld: SessionUseCaseOld,
) : ViewModel() {
    private val viewModelState = MutableStateFlow(
        HomeViewModelStateOld(isLoading = true)
    )


    // UI state exposed to the UI
    val uiState = viewModelState
        .map(HomeViewModelStateOld::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    init {
        refreshPlanInfo()
    }


    /**
     * Refresh plan info and update the UI state accordingly
     */
    private fun refreshPlanInfo() {
        // UI is refreshing
        viewModelState.update { it.copy(isLoading = true) }
        // Fetch data from plan repository
        viewModelScope.launch {
            val result = planRepository.getCurrentPlan()
            viewModelState.update {
                when (result) {
                    is Result.Success -> it.copy(
                        isLoading = false,
                        planInfo = result.data
                    )

                    is Result.Error -> {
                        val errorMessages = it.errorMessages + ErrorMessage(
                            id = UUID.randomUUID().mostSignificantBits,
                            messageId = R.string.load_plan_info_err
                        )
                        it.copy(errorMessages = errorMessages, isLoading = false)
                    }
                }
            }
            sessionUseCaseOld.preparePlan()
        }
    }

}