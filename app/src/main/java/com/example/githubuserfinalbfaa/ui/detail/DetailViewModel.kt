package com.example.githubuserfinalbfaa.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserfinalbfaa.model.UserModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class DetailViewModel: ViewModel() {

    private val dataDetail = MutableLiveData<UserModel>()
    val dataListRepo = MutableLiveData<ArrayList<UserModel>>()

    fun setDetailUser(login: String?){
        val asyncClient = AsyncHttpClient()
        asyncClient.addHeader("Authorization", "token eca6d6fc61cc9b9295b7c51b9eada7931b37e126")
        asyncClient.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$login"
        asyncClient.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                try {
                    val responObject = JSONObject(result)
                    val mModel = UserModel()
                    mModel.login = responObject.getString("login")
                    mModel.id = responObject.getInt("id")
                    mModel.avatar = responObject.getString("avatar_url")
                    mModel.name = responObject.getString("name")
                    mModel.company = responObject.getString("company")
                    mModel.location = responObject.getString("location")
                    mModel.blog = responObject.getString("blog")
                    mModel.repository = responObject.getInt("public_repos")
                    mModel.follower = responObject.getInt("followers")
                    mModel.following = responObject.getInt("following")
                    dataDetail.postValue(mModel)
                }catch (e: Exception) {

                    e.printStackTrace()
                }
            }
            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Log.d("DetailViewModel", "$errorMessage GetAPI Failure")
            }
        })
    }

    fun getDetailData(): LiveData<UserModel>{
        return dataDetail
    }

    fun setRepo(login: String?): LiveData<ArrayList<UserModel>>{

        val mlist = ArrayList<UserModel>()

        val asyncClient = AsyncHttpClient()
        asyncClient.addHeader("Authorization", "token eca6d6fc61cc9b9295b7c51b9eada7931b37e126")
        asyncClient.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$login/repos"
        asyncClient.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                try {
                    val listRepo = JSONArray(result)
                    for (i in 0 until  listRepo.length() ) {
                        val list = listRepo.getJSONObject(i)
                        val mModel = UserModel()
                        mModel.repoName = list.getString("name")
                        mlist.add(mModel)
                    }
                    dataListRepo.postValue(mlist)
                }catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Log.e("DetailViewModel", errorMessage)
            }

        })
        return dataListRepo
    }

}