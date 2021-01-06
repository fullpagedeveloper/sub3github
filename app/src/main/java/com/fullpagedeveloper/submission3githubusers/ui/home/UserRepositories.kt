package com.fullpagedeveloper.submission3githubusers.ui.home

import androidx.lifecycle.liveData
import com.fullpagedeveloper.submission3githubusers.data.ServiceGenerator
import com.fullpagedeveloper.submission3githubusers.database.UserDao
import com.fullpagedeveloper.submission3githubusers.utils.Resource
import kotlinx.coroutines.Dispatchers

object UserRepositories {

    fun searchUsers(query: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val userSearch = ServiceGenerator.apiClient.searchUsers(query)
            emit(Resource.success(userSearch.items))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "Error Detected: ${e.localizedMessage}"))
        }
    }

    fun getFollowers(username: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(ServiceGenerator.apiClient.userFollower(username)))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "Error Detected: ${e.localizedMessage}"))
        }
    }

    fun getFollowing(username: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(ServiceGenerator.apiClient.userFollowing(username)))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "Error Detected: ${e.localizedMessage}"))
        }
    }

    fun  getFavorite(userDao: UserDao) = userDao.getUserList()
}