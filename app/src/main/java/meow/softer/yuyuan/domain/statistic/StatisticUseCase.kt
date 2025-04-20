package meow.softer.yuyuan.domain.statistic

import meow.softer.yuyuan.data.repository.dailyplan.DailyPlanRepository
import javax.inject.Inject

class StatisticUseCase @Inject constructor(
    private val dailyPlanRepository: DailyPlanRepository
) {
    fun getStatisticBarTexts(): List<String> {
        return listOf("10/10", "1/25", "10%")// todo
    }
}