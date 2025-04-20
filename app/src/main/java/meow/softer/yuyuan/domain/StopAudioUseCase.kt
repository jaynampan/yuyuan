package meow.softer.yuyuan.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import meow.softer.yuyuan.data.repository.media.SoundRepositoryOld
import javax.inject.Inject

class StopAudioUseCase @Inject constructor(
    private val soundRepositoryOld: SoundRepositoryOld
) {
    suspend operator fun invoke() {
        withContext(Dispatchers.Default) {
            soundRepositoryOld.release() //todo: how to stop audio
        }
    }
}