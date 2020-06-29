package com.example.githubuserfinalbfaa.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserfinalbfaa.DetailActivity
import com.example.githubuserfinalbfaa.model.UserModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_detail.*
import org.json.JSONObject
import java.lang.Exception
import kotlin.math.log

class DetailViewModel: ViewModel() {

    companion object {
        private val TAG = DetailViewModel::class.java.simpleName
    }
    private val dataDetail = MutableLiveData<UserModel>()

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
                Log.d(TAG, "GetAPI Success")
                try {
                    val responObject = JSONObject(result)
                    val mModel = UserModel()
                    mModel.login = responObject.getString("login")
                    mModel.id = responObject.getString("id").toInt()
                    mModel.name = responObject.getString("name")
                    mModel.company = responObject.getString("company")
                    mModel.location = responObject.getString("location")
                    mModel.blog = responObject.getString("blog")
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
                Log.d(TAG, "GetAPI Failure")
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
            }
        })
    }

    fun getDetailData(): LiveData<UserModel>{
        return dataDetail
    }
}