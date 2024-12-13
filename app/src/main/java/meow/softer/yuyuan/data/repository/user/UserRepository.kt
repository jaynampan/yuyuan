package meow.softer.yuyuan.data.repository.user

import meow.softer.yuyuan.data.Result
import meow.softer.yuyuan.data.local.dao.UserDao
import meow.softer.yuyuan.data.local.entiity.User
import meow.softer.yuyuan.data.repository.runInBackground
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao
) : IUserRepository {

    override suspend fun getAllUsers(): Result<List<User>> {
        return runInBackground { userDao.getAll() }
    }

    override suspend fun getUserById(id: Int): Result<User> {
        return runInBackground { userDao.getById(id) }
    }

    override suspend fun getCurrentBookId(userId: Int): Result<Int> {
        return runInBackground { userDao.getCurrentBookId(userId) }
    }
}