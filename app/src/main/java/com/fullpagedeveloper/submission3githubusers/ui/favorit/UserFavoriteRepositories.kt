package com.fullpagedeveloper.submission3githubusers.ui.favorit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.fullpagedeveloper.submission3githubusers.data.ServiceGenerator
import com.fullpagedeveloper.submission3githubusers.data.model.GithubUser
import com.fullpagedeveloper.submission3githubusers.database.UserDao
import com.fullpagedeveloper.submission3githubusers.utils.Resource
import kotlinx.coroutines.Dispatchers

class UserFavoriteRepositories(private val userDao: UserDao) {
    private val favorite: MutableLiveData<Boolean> = MutableLiveData()

    fun getDetailUser(username: String) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        val user = userDao.getUserDetail(username)
        if (user != null){
            favorite.postValue(true)
            emit(Resource.success(user))
        } else {
            favorite.postValue(false)
            try {
                emit(Resource.success(ServiceGenerator.apiClient.userDetail(username)))
            } catch (e: Exception){
                emit(Resource.error(null, e.message ?: "Error"))
            }
        }
    }

    suspend fun insert(user: GithubUser){
        userDao.insertUser(user)
        favorite.value = true
    }

    suspend fun delete(user: GithubUser){
        userDao.deleteUser(user)
        favorite.value = false
    }

    val isFavorite: LiveData<Boolean> = favorite
}