package com.fullpagedeveloper.consumergithub.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.fullpagedeveloper.consumergithub.data.dbcontact.UserDataSource

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserRepository

    init {
        val source = UserDataSource(application.contentResolver)
        repository = UserRepository(source)
    }

    var userLists = repository.getUserList()
}