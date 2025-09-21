package meow.softer.yuyuan.data.repository.setting

import meow.softer.yuyuan.data.local.datastore.YuyuanSetting

/**
 * Store user's preferences and configs
 */
interface ISettingRepository {
    suspend fun getSettings(): YuyuanSetting
    /**
     *  setter of current book id
     */
    suspend fun setCurrentBookId(bookId: Int)

    /**
     * set audio playback speed, in float type
     * Note the speed data is stored as int32 which needs converting
     */
    suspend fun setCurrentAudioSpeed(audioSpeed: Float)
}




