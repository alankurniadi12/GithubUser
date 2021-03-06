package com.example.githubuserfinalbfaa.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserfinalbfaa.model.UserModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class ResultViewModel: ViewModel() {

    val listGitSearch = MutableLiveData<ArrayList<UserModel>>()

    fun setSearchUserGit(searchId: String){
        Log.e("ResultViewModel", searchId)
        val listItems = ArrayList<UserModel>()

        val asyncClient = AsyncHttpClient()
        asyncClient.addHeader("Authorization", "token eca6d6fc61cc9b9295b7c51b9eada7931b37#UseYourToken")
        asyncClient.addHeader("User-Agent", "request")
        val url = "https://api.github.com/search/users?q=$searchId"
        asyncClient.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val responObject = JSONObject(result)
                    val item = responObject.getJSONArray("items")
                    for (i in 0 until item.length()) {
                        val user = item.getJSONObject(i)
                        val userModel = UserModel ()
                        userModel.id = user.getInt("id")
                        userModel.login = user.getString("login")
                        userModel.avatar = user.getString("avatar_url")
                        listItems.add(userModel)
                    }
                    listGitSearch.postValue(listItems)
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

    fun getGitSearch(): LiveData<ArrayList<UserModel>> {
        return listGitSearch
    }
}