package com.fullpagedeveloper.submission3githubusers.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fullpagedeveloper.submission3githubusers.R
import com.fullpagedeveloper.submission3githubusers.data.model.GithubUser
import com.fullpagedeveloper.submission3githubusers.databinding.DetailFragmentBinding
import com.fullpagedeveloper.submission3githubusers.ui.follow.FollowFragment
import com.fullpagedeveloper.submission3githubusers.utils.State
import com.google.android.material.tabs.TabLayoutMediator

class DetailFragment : Fragment() {

    private lateinit var detailBinding: DetailFragmentBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var githubUser: GithubUser
    private val args: DetailFragmentArgs by navArgs()
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailViewModel = ViewModelProvider(
            this
        ).get(DetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        detailBinding = DetailFragmentBinding.inflate(layoutInflater, container, false)
        detailBinding.lifecycleOwner = viewLifecycleOwner
        observeDetail()
        return detailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailBinding.content.transitionName = args.Username
        detailBinding.fabFavorite.setOnClickListener { addOrRemoveFavorite() }
        val tabList = arrayOf(
            resources.getString(R.string.followers),
            resources.getString(R.string.following)
        )
        viewPagerAdapter = ViewPagerAdapter(tabList, args.Username, this)
        detailBinding.pager.adapter = viewPagerAdapter

        TabLayoutMediator(detailBinding.tabs, detailBinding.pager) { tab, position ->
            tab.text = tabList[position]
        }.attach()
    }

    private fun observeDetail() {
        detailViewModel.data(args.Username).observe(viewLifecycleOwner, Observer {
            if(it.state == State.SUCCESS){
                githubUser = it.data!!
                detailBinding.data = it.data
            }
        })

        detailViewModel.isFavorite.observe(viewLifecycleOwner, Observer { fav ->
            isFavorite = fav
            changeFavorite(fav)
        })
    }

    private fun addOrRemoveFavorite(){
        if (!isFavorite){
            detailViewModel.addFavorite(githubUser)
        } else {
            detailViewModel.removeFavorite(githubUser)
        }
    }

    private fun changeFavorite(condition: Boolean){
        if (condition){
            detailBinding.fabFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            detailBinding.fabFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    inner class ViewPagerAdapter(
        private val tabList: Array<String>,
        private val username: String,
        fragment: Fragment
    ) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = tabList.size

        override fun createFragment(position: Int): Fragment =
            FollowFragment.newInstance(username, tabList[position])
    }
}