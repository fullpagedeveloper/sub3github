package com.fullpagedeveloper.submission3githubusers.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.fullpagedeveloper.submission3githubusers.data.model.GithubUser
import com.fullpagedeveloper.submission3githubusers.database.UserDao
import com.fullpagedeveloper.submission3githubusers.database.UserDatabase
import com.fullpagedeveloper.submission3githubusers.ui.favorit.UserFavoriteRepositories
import com.fullpagedeveloper.submission3githubusers.utils.Resource
import kotlinx.coroutines.launch

class DetailViewModel(app: Application) : AndroidViewModel(app) {

    private var userDao: UserDao = UserDatabase.getDatabase(app).userDao()
    private var userFavoriteRepos: UserFavoriteRepositories

    init {
        userFavoriteRepos = UserFavoriteRepositories(userDao)
    }

    fun data(username: String): LiveData<Resource<GithubUser>> = userFavoriteRepos.getDetailUser(username)

    fun addFavorite(githubUser: GithubUser) = viewModelScope.launch {
        userFavoriteRepos.insert(githubUser)
    }

    fun removeFavorite(githubUser: GithubUser) = viewModelScope.launch {
        userFavoriteRepos.delete(githubUser)
    }

    val isFavorite: LiveData<Boolean> = userFavoriteRepos.isFavorite
}