package meow.softer.yuyuan.data.repository.setting

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import meow.softer.yuyuan.data.local.datastore.YuyuanSetting
import meow.softer.yuyuan.di.IoDispatcher
import javax.inject.Inject

const val DefaultAudioSpeed = 10
const val DefaultBookId = 1


class SettingRepository @Inject constructor(
    private val settings: DataStore<Preferences>,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ISettingRepository {
    val IS_SETUP = booleanPreferencesKey("is_setup")
    val CURRENT_BOOK = intPreferencesKey("current_book")
    val CURRENT_SPEED = intPreferencesKey("current_speed")
    val yuyuanSettings: Flow<YuyuanSetting> = settings.data
        .map { preferences ->
            // No type safety.
            YuyuanSetting(
                isSetup = preferences[IS_SETUP] ?: false,
                currentBookId = preferences[CURRENT_BOOK] ?: DefaultBookId,
                currentSpeed = preferences[CURRENT_SPEED] ?: DefaultAudioSpeed
            )
        }

    override suspend fun getSettings(): YuyuanSetting {
        return withContext(ioDispatcher) {
            yuyuanSettings.first()
        }

    }


    override suspend fun setCurrentBookId(bookId: Int) {
        withContext(ioDispatcher) {
            settings.edit { config ->
                config[CURRENT_BOOK] = bookId
            }
        }

    }

    /**
     * Set speed, using real speed, like 0.5f
     * @param audioSpeed real speed in float format
     */
    override suspend fun setCurrentAudioSpeed(audioSpeed: Float) {
        withContext(ioDispatcher) {
            val speed = (audioSpeed * 10 + 0.1).toInt()
            settings.edit { config ->
                config[CURRENT_SPEED] = speed
            }
        }
    }
}