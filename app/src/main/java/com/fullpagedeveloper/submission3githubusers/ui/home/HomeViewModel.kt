package com.fullpagedeveloper.submission3githubusers.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.fullpagedeveloper.submission3githubusers.data.model.GithubUser
import com.fullpagedeveloper.submission3githubusers.utils.Resource

class HomeViewModel : ViewModel() {

    private val username: MutableLiveData<String> = MutableLiveData()

    val searchResult: LiveData<Resource<List<GithubUser>>> = Transformations
        .switchMap(username) {
            UserRepositories.searchUsers(it)
        }

    fun setSearch(query: String) {
        if (username.value == query) {
            return
        }
        username.value = query
    }
}