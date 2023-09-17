package com.dailyrounds.bookstore.Repositories

import com.dailyrounds.bookstore.Daos.CountryDao
import com.dailyrounds.bookstore.Daos.UserDao
import com.dailyrounds.bookstore.Entities.CountryEntity
import com.dailyrounds.bookstore.Entities.UserEntity
import com.dailyrounds.bookstore.Models.Country
import com.dailyrounds.bookstore.Models.CountryList
import com.dailyrounds.bookstore.Models.User

class UserRepository(private val userDao: UserDao, private val countryDao: CountryDao) {
    suspend fun insertUser(user: User) {
        try {
            userDao.insert(UserEntity(user.username, user.password, user.country))
        } catch (e: Exception) {
            throw e
        }
    }


    suspend fun getUserById(userId: String): User? {
        try {
            val userEntity = userDao.getUserById(userId) ?: return null
            return User(userEntity.userId, userEntity.password, userEntity.country)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getCountries(): List<Country> {
        try {

            return countryDao.getCountries().map {
                Country(name = it.CountryName, code = it.CountryCode)
            }

        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun saveCountries(countryList: CountryList) {

        countryList.map {
            CountryEntity(CountryCode = it.code, CountryName = it.name)
        }.forEach {
            try {
                countryDao.insert(it)
            } catch (e: Exception) {
                throw e
            }
        }
    }


}