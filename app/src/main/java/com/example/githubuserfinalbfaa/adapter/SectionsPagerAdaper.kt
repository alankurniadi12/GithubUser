package com.example.githubuserfinalbfaa.adapter

import android.content.Context
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.githubuserfinalbfaa.FollowerFragment
import com.example.githubuserfinalbfaa.FollowingFragment
import com.example.githubuserfinalbfaa.R

class SectionsPagerAdaper(private val mContext: Context, fm: FragmentManager): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var username: String? = "username"

    @StringRes
    private val TAB_TITLES = intArrayOf(R.string.tab_follower, R.string.tab_following)

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> {
                fragment = FollowerFragment()
                val bundle = Bundle()
                bundle.putString(FollowerFragment.EXTRA_FOLLOWERS, getData())
                fragment.arguments = bundle
            }

            1 -> {
                fragment = FollowingFragment()
                val bundle = Bundle()
                bundle.putString(FollowingFragment.EXTRA_FOLLOWING, getData())
                fragment.arguments = bundle
            }
        }
        return fragment as Fragment
    }

    fun setData(loginName: String){
        username = loginName
    }

    private fun getData(): String? {
        return username
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[position])
    }
    override fun getCount(): Int {
        return 2
    }

}