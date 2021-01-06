package com.fullpagedeveloper.submission3githubusers.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.fullpagedeveloper.submission3githubusers.R
import com.fullpagedeveloper.submission3githubusers.databinding.HomeFragmentBinding
import com.fullpagedeveloper.submission3githubusers.ui.UserAdapter
import com.fullpagedeveloper.submission3githubusers.utils.ShowStates
import com.fullpagedeveloper.submission3githubusers.utils.State
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : Fragment(), ShowStates {

    private lateinit var homeBinding: HomeFragmentBinding
    private lateinit var homeAdapter: UserAdapter
    private val homeViewModel: HomeViewModel by navGraphViewModels(R.id.my_navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeBinding = HomeFragmentBinding.inflate(layoutInflater, container, false)
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = context?.resources?.getString(R.string.home)
        homeBinding.errorLayout.emptyText.text = resources.getString(R.string.search_hint)

        homeAdapter = UserAdapter(arrayListOf()) { username, iv ->
            findNavController().navigate(HomeFragmentDirections.detailsAction(username),
                FragmentNavigatorExtras(
                    iv to username
                )
            )
        }

        homeBinding.recyclerHome.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = homeAdapter
        }

        homeBinding.searchView.apply {
            queryHint = resources.getString(R.string.search_hint)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    homeViewModel.setSearch(query)
                    homeBinding.searchView.clearFocus()
                    return true
                }
                override fun onQueryTextChange(newText: String): Boolean = false
            })
        }
        observeHome()
    }

    private fun observeHome() {
        homeViewModel.searchResult.observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.state) {
                    State.SUCCESS -> {
                        resource.data?.let { users ->
                            if (!users.isNullOrEmpty()) {
                                homeSuccess(homeBinding)
                                homeAdapter.setData(users)
                            } else {
                                homeError(homeBinding, null)
                            }
                        }
                    }
                    State.LOADING -> homeLoading(homeBinding)
                    State.ERROR -> homeError(homeBinding, it.message)
            }
            }
        })
    }

    override fun homeLoading(homeBinding: HomeFragmentBinding): Int? {
        homeBinding.apply {
            errorLayout.mainNotFound.visibility = gone
            progress_bar.visibility = visible
            recyclerHome.visibility = gone
        }
        return super.homeLoading(homeBinding)
    }

    override fun homeSuccess(homeBinding: HomeFragmentBinding): Int? {
        homeBinding.apply {
            errorLayout.mainNotFound.visibility = gone
            progress_bar.visibility = gone
            recyclerHome.visibility = visible
        }
        return super.homeSuccess(homeBinding)
    }

    override fun homeError(homeBinding: HomeFragmentBinding, message: String?): Int? {
        homeBinding.apply {
            errorLayout.apply {
                mainNotFound.visibility = visible
                emptyText.text = message ?: resources.getString(R.string.not_found)
            }
            progress_bar.visibility = gone
            recyclerHome.visibility = gone
        }
        return super.homeError(homeBinding, message)
    }
}