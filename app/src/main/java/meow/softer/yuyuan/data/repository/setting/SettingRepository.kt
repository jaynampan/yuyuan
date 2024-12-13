package meow.softer.yuyuan.data.repository.setting

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import meow.softer.yuyuan.YuyuanSetting
import javax.inject.Inject

class SettingRepository @Inject constructor(
    private val yuyuanSetting: DataStore<YuyuanSetting>
) {
    private val currentUserFlow: Flow<Int> = yuyuanSetting.data
        .map { settings ->
            settings.currentUserId
        }
    private val wordsPerDayFlow: Flow<Int> = yuyuanSetting.data
        .map { settings ->
            settings.wordsPerDay
        }

    suspend fun getCurrentUserId(): Int {
        val cachedUserId = currentUserFlow.first()
        if (cachedUserId == 0) {
            setDefaultUser(DefaultUserId)
            return DefaultUserId
        } else {
            return cachedUserId
        }
    }

    suspend fun getWordsPerDay(): Int {
        val cachedWordsPerDay = wordsPerDayFlow.first()
        if (cachedWordsPerDay == 0) {
            setDefaultWordsPerDay(DefaultWordsPerDay)
            return DefaultWordsPerDay
        } else {
            return cachedWordsPerDay
        }
    }

    private suspend fun setDefaultWordsPerDay(amount: Int) {
        yuyuanSetting.updateData { currentSettings ->
            currentSettings.toBuilder()
                .setWordsPerDay(amount)
                .build()
        }
    }

    private suspend fun setDefaultUser(userId: Int) {
        yuyuanSetting.updateData { currentSettings ->
            currentSettings.toBuilder()
                .setCurrentUserId(userId)
                .build()
        }
    }

}