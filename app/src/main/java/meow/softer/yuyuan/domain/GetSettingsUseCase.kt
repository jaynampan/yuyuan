package meow.softer.yuyuan.domain

import meow.softer.yuyuan.data.repository.setting.SettingRepository
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(
    private val settingRepository: SettingRepository
) {
    suspend operator fun invoke(): UserSetting {
        val settings = settingRepository.getSettings()
        return UserSetting(
            currentBookId = settings.currentBookId,
            currentAudioSpeed = settings.currentSpeed / 10f
        )
    }
}