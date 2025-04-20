package meow.softer.yuyuan.domain

import android.content.res.AssetManager
import meow.softer.yuyuan.data.local.entiity.Sentence
import meow.softer.yuyuan.data.local.entiity.Word
import meow.softer.yuyuan.data.repository.media.SoundRepositoryOld
import meow.softer.yuyuan.data.repository.media.toSentenceAudioPath
import meow.softer.yuyuan.data.repository.media.toWordAudioPath
import javax.inject.Inject

class PlayHanziAudioUseCase @Inject constructor(
    private val soundRepositoryOld: SoundRepositoryOld,
    private val assetManager: AssetManager,
    private val settingsUseCase: GetSettingsUseCase
) {
    suspend fun initializeAudio(){
        val userSettings = settingsUseCase()
        soundRepositoryOld.setSpeed(userSettings.currentAudioSpeed)
    }

    suspend operator fun invoke(word: Word) {
        val source = word.audioFile.toWordAudioPath()
        soundRepositoryOld.playOnce(assetManager.openFd(source))
    }
    suspend operator fun invoke(sentence: Sentence) {
        val source = sentence.audioFile.toSentenceAudioPath()
        soundRepositoryOld.playOnce(assetManager.openFd(source))
    }
}