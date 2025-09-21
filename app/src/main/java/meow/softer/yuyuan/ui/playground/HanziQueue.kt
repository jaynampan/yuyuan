package meow.softer.yuyuan.ui.playground

import meow.softer.yuyuan.domain.GetHanziInfoUseCase
import meow.softer.yuyuan.domain.HanziInfo
import javax.inject.Inject


class HanziQueue @Inject constructor(
    private val getHanziInfoUseCase: GetHanziInfoUseCase
) {
    private val cache = mutableListOf<HanziInfo>()
    private val cacheSize = 4

    suspend fun getHead(): HanziInfo? {
        if (cache.isEmpty()) {
            cache.addAll(getHanziInfoUseCase(cacheSize))
        }

        return cache.removeFirstOrNull()
    }

    fun refreshCache(){
        cache.clear()
    }
}