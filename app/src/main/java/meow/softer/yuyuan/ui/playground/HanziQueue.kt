package meow.softer.yuyuan.ui.playground

import meow.softer.yuyuan.domain.GetHanziInfoUseCase
import meow.softer.yuyuan.domain.HanziInfo
import javax.inject.Inject

/**
 * Todo: add cache
 */
class HanziQueue
@Inject constructor(
    private val getHanziInfoUseCase: GetHanziInfoUseCase
) {

    suspend fun getHead(): HanziInfo? {
        val hanziInfos = getHanziInfoUseCase(1)
        if (hanziInfos.isEmpty()){
            return null
        }
        return hanziInfos[0]
    }
}