package com.example.githubuserfinalbfaa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserfinalbfaa.ui.detail.followers.FollowingAdapter
import com.example.githubuserfinalbfaa.viewmodel.FollowingViewModel
import kotlinx.android.synthetic.main.fragment_following_list.*

/**
 * A simple [Fragment] subclass.
 */
class ListFollowingFragment : Fragment() {

    companion object {
        const val EXTRA_FOLLOWING = "extra_following"
    }

    private lateinit var adapter: FollowingAdapter
    private lateinit var followingViewModel: FollowingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FollowingAdapter()

        showRecyclerView()

        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowingViewModel::class.java)

        if (arguments != null) {
            val username = arguments?.getString(EXTRA_FOLLOWING)
            followingViewModel.setFollowing(username)
        }

        followingViewModel.getFollowing().observe(this, Observer { usermodel ->
            if (usermodel != null){
                adapter.setData(usermodel)
            }
        })
    }

    private fun showRecyclerView() {
        rv_following.layoutManager = LinearLayoutManager(context)
        rv_following.adapter = adapter

        adapter.notifyDataSetChanged()
    }

    /*fun showLoading(state: Boolean){
        if (state){
            progressbar_detail.visibility = View.VISIBLE
        } else {
            progressbar_detail.visibility = View.GONE
        }
    }*/

}
