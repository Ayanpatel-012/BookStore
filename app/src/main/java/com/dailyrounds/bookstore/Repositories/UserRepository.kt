package com.dailyrounds.bookstore.Repositories

import com.dailyrounds.bookstore.Daos.UserDao
import com.dailyrounds.bookstore.Entities.UserEntity
import com.dailyrounds.bookstore.Models.User

class UserRepository(private val userDao: UserDao) {
    suspend fun insertUser(user: User) {
        try {
            userDao.insert(UserEntity(user.username, user.password))
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getUsers(): List<User> {
        try {
            return userDao.getUsers().map {
                User(it.userId, it.password)
            }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getUserById(userId: String): User? {
        try {
            val usersList = getUsers()
            for (user in usersList) {
                if (user.username == userId) return user
            }
            return null
        } catch (e: Exception) {
            throw e
        }
    }

}