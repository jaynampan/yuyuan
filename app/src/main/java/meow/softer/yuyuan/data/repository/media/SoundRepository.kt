package meow.softer.yuyuan.data.repository.media

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackParameters
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SoundRepository @Inject constructor(
    private val context: Context
) {
    private var player: ExoPlayer = ExoPlayer.Builder(context).build()
    private var speed: Float = 1f

    suspend fun play(uri: Uri) {
        withContext(Dispatchers.IO) {
            playInQueue(listOf(uri))
        }

    }

    suspend fun playInQueue(uris : List<Uri>, interval:Long = 0){
        withContext(Dispatchers.IO) {
            player.stop()
            player.clearMediaItems()
            player.setMediaItems(uris.map { MediaItem.fromUri(it) })
            // Prepare the player.
            player.prepare()
            player.playbackParameters = PlaybackParameters.DEFAULT.also { it.speed = speed }
            // Start the playback.
            player.play()
        }
    }

}