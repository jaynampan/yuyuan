package meow.softer.yuyuan.data.repository.setting

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.first
import meow.softer.yuyuan.YuyuanSetting
import meow.softer.yuyuan.utils.debug
import javax.inject.Inject

const val DefaultAudioSpeed = 1f
const val DefaultBookId = 1


class SettingRepository @Inject constructor(
    private val settings: DataStore<YuyuanSetting>
) : ISettingRepository {
    /**
     * Get user's settings
     *
     * Note the speed is in raw int32 format
     */
    override suspend fun getSettings(): YuyuanSetting {
        setupSettings()
        return settings.data.first()
    }


    override suspend fun setCurrentBookId(bookId: Int) {
        settings.updateData { current ->
            current.toBuilder()
                .setCurrentBookId(bookId)
                .build()
        }
    }

    /**
     * Set speed, using real speed, like 0.5f
     * @param audioSpeed real speed in float format
     */
    override suspend fun setCurrentAudioSpeed(audioSpeed: Float) {
        val speed = (audioSpeed * 10 + 0.1).toInt()
        settings.updateData { current ->
            current.toBuilder()
                .setCurrentSpeed(speed)
                .build()
        }
    }

    /**
     * Set up default values if not yet
     */
    override suspend fun setupSettings() {
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