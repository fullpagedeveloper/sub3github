package com.fullpagedeveloper.submission3githubusers.ui.follow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fullpagedeveloper.submission3githubusers.R
import com.fullpagedeveloper.submission3githubusers.databinding.FollowFragmentBinding
import com.fullpagedeveloper.submission3githubusers.ui.UserAdapter
import com.fullpagedeveloper.submission3githubusers.utils.ShowStates
import com.fullpagedeveloper.submission3githubusers.utils.State
import com.fullpagedeveloper.submission3githubusers.utils.TypeView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.follow_fragment.*
import kotlinx.android.synthetic.main.include_empty.view.*


class FollowFragment : Fragment(), ShowStates {

    companion object {
        fun newInstance(username: String, type: String) =
            FollowFragment().apply {
                arguments = Bundle().apply {
                    putString(USERNAME, username)
                    putString(TYPE, type)
                }
            }
        private const val TYPE = "type"
        private const val USERNAME = "username"
    }

    private lateinit var followBinding: FollowFragmentBinding
    private lateinit var usersAdapter: UserAdapter
    private lateinit var followViewModel: FollowViewModel
    private lateinit var username: String
    private var type: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            username = it.getString(USERNAME).toString()
            type = it.getString(TYPE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        followBinding = FollowFragmentBinding.inflate(layoutInflater, container, false)
        return followBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followViewModel = ViewModelProvider(
            this, ViewModelProvider.NewInstanceFactory()
        ).get(FollowViewModel::class.java)

        usersAdapter = UserAdapter(arrayListOf()) { user, _ ->
            Snackbar.make(followBinding.root, user, Snackbar.LENGTH_SHORT).show()
        }

        followBinding.recylerFollow.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = usersAdapter
        }

        when (type) {
            resources.getString(R.string.following) -> followViewModel.setFollow(username, TypeView.FOLLOWING)
            resources.getString(R.string.followers) -> followViewModel.setFollow(username, TypeView.FOLLOWER)
            else -> followError(followBinding, null)
        }
        observeFollow()
    }

    private fun observeFollow() {
        followViewModel.dataFollow.observe(viewLifecycleOwner, Observer {
            when (it.state) {
                State.SUCCESS ->
                    if (!it.data.isNullOrEmpty()) {
                        followSuccess(followBinding)
                        usersAdapter.run { setData(it.data) }
                    } else {
                        followError(followBinding, resources.getString(R.string.not_have, username, type))
                    }
                State.LOADING -> followLoading(followBinding)
                State.ERROR -> followError(followBinding, it.message)
            }
        })
    }

    override fun followLoading(followBinding: FollowFragmentBinding): Int? {
        followBinding.apply {
            progress_bar_follow.visibility = visible
            recylerFollow.visibility = gone
        }
        return super.followLoading(followBinding)
    }

    override fun followSuccess(followBinding: FollowFragmentBinding): Int? {
        followBinding.apply {
            progress_bar_follow.visibility = gone
            recylerFollow.visibility = visible
        }
        return super.followSuccess(followBinding)
    }

    override fun followError(followBinding: FollowFragmentBinding, message: String?): Int? {
        followBinding.apply {
            error_layout_follow.apply {
                error_layout_follow.mainNotFound.visibility = visible
                empty_text.text = message ?: resources.getString(R.string.not_found)
            }
            progress_bar_follow.visibility = gone
            recylerFollow.visibility = gone
        }
        return super.followError(followBinding, message)
    }
}