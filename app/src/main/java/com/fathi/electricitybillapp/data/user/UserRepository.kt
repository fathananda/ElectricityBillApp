package com.fathi.electricitybillapp.data.user

import com.fathi.electricitybillapp.util.toEntity
import com.fathi.electricitybillapp.util.toUser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao
) {
    suspend fun login(username: String, password: String): User? {
        return userDao.login(username, password)?.toUser()
    }

    suspend fun getUserById(id: Int): User? {
        return userDao.getUserById(id)?.toUser()
    }

    suspend fun getAllCustomers(): List<User> {
        return userDao.getAllCustomers().map { it.toUser() }
    }

    suspend fun insertUser(user: User): Long {
        return userDao.insertUser(user.toEntity())
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user.toEntity())
    }

    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user.toEntity())
    }
}