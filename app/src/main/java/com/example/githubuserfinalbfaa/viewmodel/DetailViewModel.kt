package com.example.githubuserfinalbfaa.viewmodel

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

class DetailViewModel: ViewModel() {

    private lateinit var detailActivity: DetailActivity

    fun setDetailUser(login: UserModel){
        val asyncClient = AsyncHttpClient()
        asyncClient.addHeader("Authorization", "token eca6d6fc61cc9b9295b7c51b9eada7931b37xxxx")
        asyncClient.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$login"

        asyncClient.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val responObject = JSONObject(result)
                    val mModel = UserModel()
                    //mModel.id = responObject.getString("id")
                    mModel.name = responObject.getString("name")
                    mModel.company = responObject.getString("company")
                    mModel.location = responObject.getString("location")
                    mModel.blog = responObject.getString("blog")

                    detailActivity.tv_detail_username.text = mModel.name
                    detailActivity.tv_detail_company.text = mModel.company
                    detailActivity.tv_detail_location.text = mModel.location
                    detailActivity.tv_detail_blog.text = mModel.blog
                }catch (e: Exception) {
                    //Toast.makeText(detailActivity.applicationContext, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }
            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable
            ) {
                //detailActivity.showLoading(false)
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                //Toast.makeText(detailActivity.applicationContext, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}