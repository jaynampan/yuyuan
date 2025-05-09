package meow.softer.yuyuan.data.repository.media

import android.content.res.AssetFileDescriptor

interface IMusicRepository {
    suspend fun setSource(source: String)
    suspend fun play()
    suspend fun pause()
    suspend fun stop()
    fun release()
    suspend fun seekTo(position: Int)
    suspend fun updatePlaybackPosition()
    suspend fun playOnce(afd: AssetFileDescriptor)
}

const val WordAudioExtension = ".opus"
const val SentenceAudioExtension = ".opus"
const val WordAudioFolder = "word_audio/"
const val SentenceAudioFolder = "sentence_audio/"