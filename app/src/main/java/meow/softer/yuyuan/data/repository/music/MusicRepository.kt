package meow.softer.yuyuan.data.repository.music

import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MusicRepository @Inject constructor(
) : IMusicRepository {
    private val mediaPlayer: MediaPlayer = MediaPlayer()

    // StateFlow to expose the current music playback state
    private val _playbackState = MutableStateFlow(MusicState())
    val playbackState: StateFlow<MusicState> get() = _playbackState

    // Initialize the MediaPlayer
    init {
        mediaPlayer.setOnCompletionListener {
            // When playback is complete, update the state
//            _playbackState.value = _playbackState.value.copy(isPlaying = false, currentPosition = 0)
//            mediaPlayer.start()

        }

        mediaPlayer.setOnPreparedListener {
            // When media is prepared, update the state with the total duration
//            _playbackState.value = _playbackState.value.copy(
//                totalDuration = mediaPlayer.duration
//            )
            mediaPlayer.start()
        }

        mediaPlayer.setOnErrorListener { mp, what, extra ->
            // Handle errors here (e.g., unsupported format)
            Log.e("YuyuanTest", mp.toString()+ what.toString()+ extra.toString())
            false
        }
    }

    /**
     * Set the audio source (URL or local file path) to the MediaPlayer.
     */
    override suspend fun setSource(source: String) {
        try {
            // Reset MediaPlayer in case there's an existing track loaded
            mediaPlayer.reset()

            // Set the data source (can be a file path, URL, or URI)
            mediaPlayer.setDataSource(source)

            // Prepare the MediaPlayer asynchronously (blocking until prepared)
            mediaPlayer.prepareAsync()
        } catch (e: Exception) {
            // Handle any exceptions (e.g., invalid URL or path)
            e.printStackTrace()
        }
    }

    override suspend fun setSource(afd: AssetFileDescriptor) {
        try {
            // Reset MediaPlayer in case there's an existing track loaded
            mediaPlayer.reset()

            // Set the data source (can be a file path, URL, or URI)
            mediaPlayer.setDataSource(afd)

            // Prepare the MediaPlayer asynchronously (blocking until prepared)
            mediaPlayer.prepareAsync()
        } catch (e: Exception) {
            // Handle any exceptions (e.g., invalid URL or path)
            e.printStackTrace()
        }
    }

    /**
     * Start or resume playback.
     */
    override suspend fun play() {
        try {
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()

                // Update the playback state to reflect the change
                _playbackState.value = _playbackState.value.copy(isPlaying = true)
            }
        } catch (e: Exception) {
            // Handle any playback issues
            e.printStackTrace()
        }
    }

    /**
     * Pause playback.
     */
    override suspend fun pause() {
        try {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()

                // Update the playback state to reflect the change
                _playbackState.value = _playbackState.value.copy(isPlaying = false)
            }
        } catch (e: Exception) {
            // Handle any issues during pause
            e.printStackTrace()
        }
    }

    /**
     * Stop playback and release resources.
     */
    override suspend fun stop() {
        try {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
                mediaPlayer.reset()

                // Reset the state to indicate that playback has stopped
                _playbackState.value =
                    _playbackState.value.copy(isPlaying = false, currentPosition = 0)
            }
        } catch (e: Exception) {
            // Handle any issues during stop
            e.printStackTrace()
        }
    }

    /**
     * Seek to a specific position in the track.
     */
    override suspend fun seekTo(position: Int) {
        try {
            if (mediaPlayer.isPlaying || mediaPlayer.isLooping) {
                mediaPlayer.seekTo(position)

                // Update the state to reflect the change
                _playbackState.value = _playbackState.value.copy(currentPosition = position)
            }
        } catch (e: Exception) {
            // Handle any issues during seeking
            e.printStackTrace()
        }
    }

    /**
     * Update the playback position regularly (e.g., for a progress bar).
     */
    override suspend fun updatePlaybackPosition() {
        try {
            if (mediaPlayer.isPlaying) {
                // Update the current playback position
                _playbackState.value = _playbackState.value.copy(
                    currentPosition = mediaPlayer.currentPosition
                )
            }
        } catch (e: Exception) {
            // Handle any errors during position update
            e.printStackTrace()
        }
    }

    // Optionally, a method to release the MediaPlayer when no longer needed
    override fun release() {
        try {
            mediaPlayer.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}