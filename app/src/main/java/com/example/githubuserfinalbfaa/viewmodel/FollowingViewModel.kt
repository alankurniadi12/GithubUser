package com.example.githubuserfinalbfaa.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserfinalbfaa.model.UserModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.lang.Exception

class FollowingViewModel: ViewModel() {

    val listFolloing = MutableLiveData<ArrayList<UserModel>>()

    fun setFollowing(name: String?) {
        val listItems = ArrayList<UserModel>()

        val asyncClient = AsyncHttpClient()
        asyncClient.addHeader("Authorization", "token eca6d6fc61cc9b9295b7c51b9eada7931b37xxxx")
        asyncClient.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$name/following"

        asyncClient.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                try {
                    val result = String (responseBody)
                    val responsArray = JSONArray(result)

                    for(i in 0 until responsArray.length()) {
                        val jsonObject = responsArray.getJSONObject(i)
                        val model = UserModel()
                        model.login = jsonObject.getString("login")
                        model.avatar = jsonObject.getString("avatar_url")
                        listItems.add(model)
                    }
                    listFolloing.postValue(listItems)
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

    fun getFollowing(): LiveData<ArrayList<UserModel>> {
        return listFolloing
    }
}