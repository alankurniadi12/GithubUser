package com.example.githubuserfinalbfaa.ui.detail.followers

import android.content.Context
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.githubuserfinalbfaa.ListFollowerFragment
import com.example.githubuserfinalbfaa.ListFollowingFragment
import com.example.githubuserfinalbfaa.R

class SectionsPagerAdaper(private val mContext: Context, fm: FragmentManager): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var username: String? = "username"

    fun setData(loginName: String) {
        username = loginName
    }

    @StringRes
    private val TAB_TITLES = intArrayOf(R.string.tab_follower, R.string.tab_following)

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> {
                fragment = ListFollowerFragment()
                val bundle = Bundle()
                bundle.putString(ListFollowerFragment.EXTRA_FOLLOWERS, username)
                fragment.arguments = bundle
            }

            1 -> {
                fragment = ListFollowingFragment()
                val bundle = Bundle()
                bundle.putString(ListFollowingFragment.EXTRA_FOLLOWING, username)
                fragment.arguments = bundle
            }
        }
        return fragment as Fragment
    }


    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[position])
    }
    override fun getCount(): Int = TAB_TITLES.size

}