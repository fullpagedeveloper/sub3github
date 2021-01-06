package com.fullpagedeveloper.consumergithub.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.fullpagedeveloper.consumergithub.data.dbcontact.UserDataSource
import com.fullpagedeveloper.consumergithub.data.model.GithubUser
import kotlinx.coroutines.Dispatchers

class UserRepository(private val source: UserDataSource) {

    fun getUserList():LiveData<List<GithubUser>> = liveData(Dispatchers.IO){
        emit(source.getUsers())
    }
}