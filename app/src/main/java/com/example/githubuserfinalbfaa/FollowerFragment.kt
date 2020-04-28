package com.example.githubuserfinalbfaa

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserfinalbfaa.adapter.FollowersAdapter
import com.example.githubuserfinalbfaa.model.UserModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.fragment_follower.*
import kotlinx.android.synthetic.main.item_user.*
import org.json.JSONArray
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 */
class FollowerFragment : Fragment() {

    companion object {
        var EXTRA_FOLLOWERS = "extra_followers"
    }

    private lateinit var adapter: FollowersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follower, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FollowersAdapter()
        adapter.notifyDataSetChanged()

        showRecyclerView()
        setFollowersGit()
    }


    private fun showRecyclerView() {
        rv_follower.layoutManager = LinearLayoutManager(context)
        rv_follower.adapter = adapter
    }

    private fun setFollowersGit() {
        val listItems = ArrayList<UserModel>()

        val mLogin = arguments?.getString(EXTRA_FOLLOWERS)

        val asyncClient = AsyncHttpClient()
        asyncClient.addHeader("Authorization", "token eca6d6fc61cc9b9295b7c51b9eada7931b37xxxx")
        asyncClient.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$mLogin/followers"

        asyncClient.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val responsArray = JSONArray(result)

                    for (i in 0 until responsArray.length()){
                        val jsonObject = responsArray.getJSONObject(i)
                        val mModel = UserModel()
                        mModel.login = jsonObject.getString("login")
                        mModel.avatar = jsonObject.getString("avatar_url")

                        listItems.add(mModel)
                    }
                    adapter.setData(listItems)

                }catch (e: Exception){
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable
            ) {
                Log.d("onFailur", error.message.toString())
            }
        })
    }

}
