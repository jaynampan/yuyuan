package meow.softer.yuyuan.data.repository.plan

import meow.softer.yuyuan.data.Result
import meow.softer.yuyuan.domain.PlanInfo

/**
 * Interface to the Plans in data layer.
 */
interface IPlanRepository {

    /**
     * Get current PLan
     */
    suspend fun getCurrentPlan(): Result<PlanInfo>

    /**
     * update learnt words count
     */
    suspend fun updateLearnt(increment: Int)
}
