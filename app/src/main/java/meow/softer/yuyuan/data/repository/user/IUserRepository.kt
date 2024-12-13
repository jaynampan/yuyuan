package meow.softer.yuyuan.data.repository.user

import meow.softer.yuyuan.data.Result
import meow.softer.yuyuan.data.local.entiity.User
import meow.softer.yuyuan.data.repository.setting.DefaultUserId

/**
 * Responsible for User data
 */
interface IUserRepository {
    /**
     * Get all user data
     */
    suspend fun getAllUsers(): Result<List<User>>

    /**
     * Get a user by its id
     * defaults to default user
     */
    suspend fun getUserById(id: Int = DefaultUserId): Result<User>

    /**
     * Get a user's current book by userId
     */
    suspend fun getCurrentBookId(userId: Int): Result<Int>
}