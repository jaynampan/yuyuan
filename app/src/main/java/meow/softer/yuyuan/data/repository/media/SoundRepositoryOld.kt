package meow.softer.yuyuan.data.repository.media

import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import meow.softer.yuyuan.utils.debug
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoundRepositoryOld @Inject constructor(
) : IMusicRepository {
    private var mediaPlayer: MediaPlayer? = null
    private var _speed = 1f


    fun setNewMediaPlayer(){
        if(mediaPlayer == null){
            mediaPlayer = MediaPlayer()
            mediaPlayer?.setOnPreparedListener {
                mediaPlayer?.start()
            }
            debug("create new player")
            mediaPlayer?.setOnErrorListener { mp, what, extra ->
                // Handle errors here (e.g., unsupported format)
                Log.e("YuyuanTest", mp.toString() + what.toString() + extra.toString())
                false
            }
        }

    }

    /**
     * Set the audio source (URL or local file path) to the MediaPlayer.
     */
    override suspend fun setSource(source: String) {
        try {
            // Reset MediaPlayer in case there's an existing track loaded
            mediaPlayer?.reset()

            // Set the data source (can be a file path, URL, or URI)
            mediaPlayer?.setDataSource(source)

            // Prepare the MediaPlayer asynchronously (blocking until prepared)
            mediaPlayer?.prepareAsync()
        } catch (e: Exception) {
            // Handle any exceptions (e.g., invalid URL or path)
            e.printStackTrace()
        }
    }

    override suspend fun playOnce(afd: AssetFileDescriptor) {
        try {
            setNewMediaPlayer()
            // Reset MediaPlayer in case there's an existing track loaded
            mediaPlayer?.reset()


            // Set the data source (can be a file path, URL, or URI)
            mediaPlayer?.setDataSource(afd)
            // set speed
            val playbackParams = PlaybackParams()
            playbackParams.speed = _speed
            mediaPlayer?.playbackParams = playbackParams
            // Prepare the MediaPlayer asynchronously (blocking until prepared)
            mediaPlayer?.prepareAsync()

            debug("player: actual speed = ${playbackParams.speed}, _speed= $_speed")
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
            if (mediaPlayer?.isPlaying == false) {
                mediaPlayer?.start()
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
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
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
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.stop()
                mediaPlayer?.reset()
                debug("audio stopped")
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
            if (mediaPlayer?.isPlaying == true|| mediaPlayer?.isLooping == true) {
                mediaPlayer?.seekTo(position)

            }
        } catch (e: Exception) {
            // Handle any issues during seeking
            e.printStackTrace()
        }
    }

    override suspend fun updatePlaybackPosition() {
        TODO("Not yet implemented")
    }


    suspend fun setSpeed(speed: Float) {
        withContext(Dispatchers.IO) {
            _speed = speed
            debug("player: speed set to  $speed")

        }
    }

    override fun release() {
        debug("release player called ")
        try {
            if (mediaPlayer != null && mediaPlayer?.isPlaying == true) {
                mediaPlayer?.stop()
                mediaPlayer?.reset()
            }
            mediaPlayer?.release()
            mediaPlayer = null
            debug("release player finished")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}