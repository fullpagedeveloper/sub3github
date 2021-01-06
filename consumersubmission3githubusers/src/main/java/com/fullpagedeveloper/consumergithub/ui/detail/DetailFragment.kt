package com.fullpagedeveloper.consumergithub.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fullpagedeveloper.consumergithub.data.model.GithubUser
import com.fullpagedeveloper.consumergithub.databinding.DetailFragmentBinding

class DetailFragment : Fragment() {

    private lateinit var githubUser: GithubUser
    private lateinit var detailBinding: DetailFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            githubUser = it.getParcelable(USER)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        detailBinding = DetailFragmentBinding.inflate(layoutInflater, container, false)
        detailBinding.databinding = githubUser
        return detailBinding.root
    }

    companion object {
        private const val USER = "USER"

        @JvmStatic
        fun newInstance(githubUser: GithubUser) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(USER, githubUser)
                }
            }
    }
}