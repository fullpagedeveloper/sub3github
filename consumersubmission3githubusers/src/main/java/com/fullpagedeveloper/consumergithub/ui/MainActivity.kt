package com.fullpagedeveloper.consumergithub.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fullpagedeveloper.consumergithub.R
import com.fullpagedeveloper.consumergithub.databinding.ActivityMainBinding
import com.fullpagedeveloper.consumergithub.ui.detail.DetailFragment

class MainActivity : AppCompatActivity() {
    private lateinit var usersViewModel: UserViewModel
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var usersAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        usersAdapter = UserAdapter(ArrayList()) { githubUser ->
            Log.i("tes", "Try this $githubUser")
            supportFragmentManager.beginTransaction().apply {
                val usersFragment = DetailFragment.newInstance(githubUser)
                replace(R.id.main_frame, usersFragment)
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                addToBackStack(null)
                commit()
            }
        }

        mainBinding.mainRv.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = usersAdapter
        }

        usersViewModel = ViewModelProvider(
            viewModelStore,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(UserViewModel::class.java)
        visible()
        usersViewModel.userLists.observe(this, Observer { users ->
            if (!users.isNullOrEmpty()) {
                gone(false)
                usersAdapter.setData(users)
            } else {
                gone(true)
            }
        })
    }

    private fun visible(){
        mainBinding.progress.visibility = View.VISIBLE
    }

    private fun gone(error: Boolean){
        if (error){
            mainBinding.apply {
                progress.visibility = View.GONE
                errLayout.visibility = View.VISIBLE
            }
        } else {
            mainBinding.progress.visibility = View.GONE
        }
    }
}