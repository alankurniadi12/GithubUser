package com.example.githubuserfinalbfaa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.githubuserfinalbfaa.adapter.SectionsPagerAdaper
import com.example.githubuserfinalbfaa.model.UserModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_detail.*
import org.json.JSONObject
import java.lang.Exception

class DetailActivity : AppCompatActivity() {


    companion object {
        const val EXTRA_LOGIN_NAME = "extra_detail"
        const val EXTRA_AVATAR = "extra_avatar"


    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        showLoading(true)
        //val model = intent.getParcelableExtra(EXTRA_DETAIL) as UserModel
        val username = intent.getStringExtra(EXTRA_LOGIN_NAME)
        val avatar = intent.getStringExtra(EXTRA_AVATAR)
        tv_detail_loginname.text = username
        Glide.with(this).load(avatar).into(img_detail_user)
        setDetailUser(username)


        val sectionsPagerAdaper = SectionsPagerAdaper(this, supportFragmentManager)
        view_pager.adapter = sectionsPagerAdaper
        tabs.setupWithViewPager(view_pager)
        supportActionBar?.elevation = 0f

        val fragment = FollowerFragment()
        val bundle = Bundle()
        bundle.putString(FollowerFragment.EXTRA_FOLLOWERS, username)
        fragment.arguments = bundle
    }

    private fun setDetailUser(login: String?) {
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
                showLoading(false)
                try {
                    val result = String(responseBody)
                    val responObject = JSONObject(result)
                    val mModel = UserModel()
                    mModel.name = responObject.getString("name")
                    mModel.company = responObject.getString("company")
                    mModel.location = responObject.getString("location")
                    mModel.blog = responObject.getString("blog")

                    tv_detail_username.text = mModel.name
                    tv_detail_company.text = mModel.company
                    tv_detail_location.text = mModel.location
                    tv_detail_blog.text = mModel.blog
                }catch (e: Exception) {
                    Toast.makeText(this@DetailActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }
            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable
            ) {
                showLoading(false)
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@DetailActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showLoading(state: Boolean){
        if (state){
            progressbar_detail.visibility = View.VISIBLE
        } else {
            progressbar_detail.visibility = View.GONE
        }
    }
}
