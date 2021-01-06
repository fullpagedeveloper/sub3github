@file:Suppress("Annotator")

package com.fullpagedeveloper.submission3githubusers.ui.favorit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.fullpagedeveloper.submission3githubusers.R
import com.fullpagedeveloper.submission3githubusers.databinding.FavoriteFragmentBinding
import com.fullpagedeveloper.submission3githubusers.ui.UserAdapter
import com.fullpagedeveloper.submission3githubusers.utils.ShowStates
import kotlinx.android.synthetic.main.favorite_fragment.*

class FavoriteFragment : Fragment(), ShowStates {
    private lateinit var favoriteBinding: FavoriteFragmentBinding
    private lateinit var favoriteAdapter: UserAdapter
    private val favoriteViewModel: FavoriteViewModel by navGraphViewModels(R.id.my_navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = context?.resources?.getString(R.string.favorite)
        favoriteBinding = FavoriteFragmentBinding.inflate(layoutInflater, container, false)
        return favoriteBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoriteAdapter = UserAdapter(arrayListOf()) { username, iv ->
            findNavController().navigate(
                FavoriteFragmentDirections.actionFavoriteFragmentToDetailsDestination(username),
                FragmentNavigatorExtras(iv to username)
            )
        }

        favoriteBinding.recyclerFav.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = favoriteAdapter
        }

        observeFavorite()
    }

    private fun observeFavorite() {
        favoriteLoading(favoriteBinding)
        favoriteViewModel.dataFavorite.observe(viewLifecycleOwner, Observer {
            it?.let { users ->
                if (!users.isNullOrEmpty()){
                    favoriteSuccess(favoriteBinding)
                    favoriteAdapter.setData(users)
                } else {
                    favoriteError(
                        favoriteBinding,
                        resources.getString(R.string.not_have, "", resources.getString(R.string.favorite))
                    )
                }
            }
        })
    }

    override fun favoriteLoading(favoriteFragmentBinding: FavoriteFragmentBinding): Int? {
        favoriteBinding.apply {
            errlayout.mainNotFound.visibility = gone
            progress_bar.visibility = visible
            recyclerFav.visibility = gone
        }
        return super.favoriteLoading(favoriteFragmentBinding)
    }

    override fun favoriteSuccess(favoriteFragmentBinding: FavoriteFragmentBinding): Int? {
        favoriteBinding.apply {
            errlayout.mainNotFound.visibility = gone
            progress_bar.visibility = gone
            recyclerFav.visibility = visible
        }
        return super.favoriteSuccess(favoriteFragmentBinding)
    }

    override fun favoriteError(
        favoriteFragmentBinding: FavoriteFragmentBinding,
        message: String?
    ): Int? {
        favoriteBinding.apply {
            errlayout.apply {
                mainNotFound.visibility = visible
                emptyText.text = message ?: resources.getString(R.string.not_found)
            }
            progress_bar.visibility = gone
            recyclerFav.visibility = gone
        }
        return super.favoriteError(favoriteFragmentBinding, message)
    }
}