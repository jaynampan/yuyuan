package meow.softer.yuyuan.data.repository.setting

import androidx.datastore.core.DataStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import meow.softer.yuyuan.YuyuanSetting
import meow.softer.yuyuan.di.IoDispatcher
import meow.softer.yuyuan.utils.debug
import javax.inject.Inject

const val DefaultAudioSpeed = 1f
const val DefaultBookId = 1


class SettingRepository @Inject constructor(
    private val settings: DataStore<YuyuanSetting>,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ISettingRepository {
    /**
     * Get user's settings
     *
     * Note the speed is in raw int32 format
     *
     * Main-Safe
     */
    override suspend fun getSettings(): YuyuanSetting {
        return withContext(ioDispatcher) {
            setupSettings()
            settings.data.first()
        }

    }


    override suspend fun setCurrentBookId(bookId: Int) {
        withContext(ioDispatcher) {
            settings.updateData { current ->
                current.toBuilder()
                    .setCurrentBookId(bookId)
                    .build()
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
            settings.updateData { current ->
                current.toBuilder()
                    .setCurrentSpeed(speed)
                    .build()
            }
        }
    }

    /**
     * Set up default values if not yet
     */
    override suspend fun setupSettings() {
        withContext(ioDispatcher) {
            if (settings.data.first().isSetup == 0) {
                debug("setting repo: setupSettings")
                setCurrentBookId(DefaultBookId)
                setCurrentAudioSpeed(DefaultAudioSpeed)
                settings.updateData { current ->
                    current.toBuilder()
                        .setIsSetup(1)
                        .build()
                }
            }
        }

    }
}