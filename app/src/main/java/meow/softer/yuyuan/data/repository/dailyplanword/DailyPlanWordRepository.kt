package meow.softer.yuyuan.data.repository.dailyplanword

import meow.softer.yuyuan.data.Result
import meow.softer.yuyuan.data.local.dao.DailyPlanWordDao
import meow.softer.yuyuan.data.local.entiity.DailyPlanWord
import javax.inject.Inject

class DailyPlanWordRepository @Inject constructor(
    private val dailyPlanWordDao: DailyPlanWordDao
) : IDailyPlanWordRepository {
    override suspend fun getAll(): Result<List<DailyPlanWord>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllByPlanId(dailyPlanId: Int): Result<List<DailyPlanWord>> {
        TODO("Not yet implemented")
    }

    override suspend fun addToPlan(wordId: Int): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun addToPlan(wordIds: List<Int>): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun clearAll(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun clearByPlanId(dailyPlanId: Int): Result<Unit> {
        TODO("Not yet implemented")
    }
}