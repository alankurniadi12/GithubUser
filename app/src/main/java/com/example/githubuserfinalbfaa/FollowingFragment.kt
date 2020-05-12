package com.example.githubuserfinalbfaa

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserfinalbfaa.adapter.FollowingAdapter
import com.example.githubuserfinalbfaa.model.UserModel
import com.example.githubuserfinalbfaa.viewmodel.FollowersViewModel
import com.example.githubuserfinalbfaa.viewmodel.FollowingViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.fragment_following.*
import org.json.JSONArray
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 */
class FollowingFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_following, container, false)
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
