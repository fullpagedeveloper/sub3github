package com.fullpagedeveloper.submission3githubusers.ui.favorit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.fullpagedeveloper.submission3githubusers.data.model.GithubUser
import com.fullpagedeveloper.submission3githubusers.database.UserDatabase
import com.fullpagedeveloper.submission3githubusers.ui.home.UserRepositories

class FavoriteViewModel(app: Application) : AndroidViewModel(app) {
    val dataFavorite: LiveData<List<GithubUser>>

    init {
        val userDao = UserDatabase.getDatabase(app).userDao()
        dataFavorite = UserRepositories.getFavorite(userDao)
    }
}