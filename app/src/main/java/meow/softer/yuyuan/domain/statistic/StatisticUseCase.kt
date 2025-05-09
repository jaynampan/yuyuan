package meow.softer.yuyuan.domain.statistic

import javax.inject.Inject

class StatisticUseCase @Inject constructor(
) {
    fun getStatisticBarTexts(): List<String> {
        return listOf("10/10", "1/25", "10%")// todo
    }
}