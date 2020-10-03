package com.example.githubuserfinalbfaa.ui.detail.followers

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.githubuserfinalbfaa.R
import com.example.githubuserfinalbfaa.model.UserModel
import kotlinx.android.synthetic.main.activity_followers.*

class FollowersActivity : AppCompatActivity() {

    private var followers: UserModel? = null
    private var following: UserModel? = null

    companion object {
        const val EXTRA_FOLLOWERS = "extra_followers"
        const val EXTRA_FOLLOWING = "extra_following"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_followers)

        followers = intent.getParcelableExtra(EXTRA_FOLLOWERS)
        following = intent.getParcelableExtra(EXTRA_FOLLOWING)

        val sectionsPagerAdapter = SectionsPagerAdaper(this, supportFragmentManager)
        if(followers != null) {
            sectionsPagerAdapter.setData(followers!!.login.toString())
        }else if (following != null) {
            sectionsPagerAdapter.setData(following!!.login.toString())
        }

        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
    }
}