package com.example.githubuserfinalbfaa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserfinalbfaa.ui.detail.followers.FollowersAdapter
import com.example.githubuserfinalbfaa.viewmodel.FollowersViewModel
import kotlinx.android.synthetic.main.fragment_follower_list.*

/**
 * A simple [Fragment] subclass.
 */
class ListFollowerFragment : Fragment() {

    companion object{
        const val EXTRA_FOLLOWERS = "followers_name"
    }
    private lateinit var adapter: FollowersAdapter
    private lateinit var followersViewModel: FollowersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follower_list, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FollowersAdapter()
        showRecyclerView()

        followersViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowersViewModel::class.java)
        if (arguments != null) {
            val username = arguments?.getString(EXTRA_FOLLOWERS)
            followersViewModel.setFollowers(username.toString())
        }
        followersViewModel.getFollowers().observe(viewLifecycleOwner, Observer { usermodel ->
            if (usermodel != null) {
                adapter.setData(usermodel)
            }
        })

    }


    private fun showRecyclerView() {
        rv_follower.layoutManager = LinearLayoutManager(context)
        rv_follower.adapter = adapter

        adapter.notifyDataSetChanged()
    }

}
