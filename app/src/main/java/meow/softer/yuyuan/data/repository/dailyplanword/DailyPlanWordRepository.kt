package meow.softer.yuyuan.data.repository.dailyplanword

import meow.softer.yuyuan.utils.debug
import meow.softer.yuyuan.data.Result
import meow.softer.yuyuan.data.local.dao.DailyPlanWordDao
import meow.softer.yuyuan.data.local.entiity.DailyPlanWord
import meow.softer.yuyuan.data.repository.runInBackground
import javax.inject.Inject

private val MyTag = DailyPlanWordRepository::class.simpleName

class DailyPlanWordRepository @Inject constructor(
    private val dailyPlanWordDao: DailyPlanWordDao
) : IDailyPlanWordRepository {
    override suspend fun getAll(): Result<List<DailyPlanWord>> {
        return runInBackground { dailyPlanWordDao.getAll() }
    }

    override suspend fun getAllByPlanId(dailyPlanId: Int): Result<List<DailyPlanWord>> {
        return runInBackground { dailyPlanWordDao.getAllByPlanId(dailyPlanId) }
    }

    override suspend fun addToPlan(planId: Int, wordId: Int): Result<Unit> {
        return runInBackground {
            dailyPlanWordDao.insert(
                DailyPlanWord(
                    dailyPlanId = planId,
                    wordId = wordId
                )
            )
        }
    }

    override suspend fun addToPlan(planId: Int, wordIds: List<Int>): Result<Unit> {
        return runInBackground {
            val dailyPlanWords = wordIds.map {
                DailyPlanWord(
                    dailyPlanId = planId,
                    wordId = it
                )
            }
            debug("dailyplan words: $dailyPlanWords", MyTag)
//            dailyPlanWordDao.insertAll(dailyPlanWords)
            dailyPlanWords.forEach { dailyPlanWordDao.insert(it.dailyPlanId, it.wordId) }
        }
    }

    override suspend fun clearAll(): Result<Unit> {
        return runInBackground {
            dailyPlanWordDao.deleteAll()
        }
    }

    override suspend fun clearByPlanId(dailyPlanId: Int): Result<Unit> {
        return runInBackground {
            dailyPlanWordDao.deleteByPlanId(dailyPlanId)
        }
    }


}