package com.fullpagedeveloper.submission3githubusers.ui.follow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.fullpagedeveloper.submission3githubusers.data.model.GithubUser
import com.fullpagedeveloper.submission3githubusers.ui.home.UserRepositories
import com.fullpagedeveloper.submission3githubusers.utils.Resource
import com.fullpagedeveloper.submission3githubusers.utils.TypeView

class FollowViewModel : ViewModel() {
    private val username: MutableLiveData<String> = MutableLiveData()

    private lateinit var type: TypeView

    val dataFollow: LiveData<Resource<List<GithubUser>>> = Transformations
        .switchMap(username) {
            when (type) {
                TypeView.FOLLOWER -> {
                    UserRepositories.getFollowers(it)
                }
                TypeView.FOLLOWING -> {
                    UserRepositories.getFollowing(it)
                }
            }
        }

    fun setFollow(user: String, typeView: TypeView) {
        if (username.value == user) {
            return
        }
        username.value = user
        type = typeView
    }
}