package meow.softer.yuyuan.domain

import meow.softer.yuyuan.data.repository.media.SoundRepositoryOld
import meow.softer.yuyuan.data.repository.setting.SettingRepository
import meow.softer.yuyuan.utils.debug
import javax.inject.Inject

/**
 * Initialize or update settings
 *
 * Use operator [invoke] with [ConfigType.SET_UP] to initialize settings
 * ```
 *  updateSettingsUseCase(ConfigType.SET_UP)
 * ```
 * Use operator [invoke] with type and the value to update a setting
 * ```
 * updateSettingsUseCase(ConfigType.CURRENT_SPEED, speed)
 * ```
 */
class UpdateSettingsUseCase @Inject constructor(
    private val settingRepository: SettingRepository,
    private val soundRepositoryOld: SoundRepositoryOld
) {
    enum class ConfigType {
        CURRENT_BOOK,
        CURRENT_SPEED
    }


    /**
     * Update a setting with the type and value. When updating audio speed,
     * the sound repository would be set up too
     *
     * Note: use operator [invoke] without passing in a value for setup,
     * ```
     *  updateSettingsUseCase(ConfigType.CURRENT_SPEED, speed)
     * ```
     * @param type specify which setting to update
     * @param value the new value of the setting
     *
     */
    suspend operator fun <T> invoke(type: ConfigType, value: T) {
        when (type) {
            ConfigType.CURRENT_SPEED -> {
                settingRepository.setCurrentAudioSpeed(value as Float)
                debug("Updatesetting: speed= $value")
                soundRepositoryOld.setSpeed(value)
            }

            ConfigType.CURRENT_BOOK -> settingRepository.setCurrentBookId(value as Int)
        }
    }


}