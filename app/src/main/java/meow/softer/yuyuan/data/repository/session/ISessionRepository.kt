package meow.softer.yuyuan.data.repository.session

import meow.softer.yuyuan.data.Result
import meow.softer.yuyuan.data.local.entiity.DailyPlan
import meow.softer.yuyuan.data.local.entiity.Sentence
import meow.softer.yuyuan.data.local.entiity.Word

/**
 * Responsible for preparing, providing and updating learning
 * data in a session
 */
interface ISessionRepository {
    suspend fun preparePlan():Result<Unit>
    suspend fun mockWord(): Word?
    suspend fun mockSentence(): Sentence?
    suspend fun playWordSound(source:String)
    suspend fun playSentenceSound(source: String)
    suspend fun generateNewPlan(currentUserId: Int, currentUserId1: Int): DailyPlan
}