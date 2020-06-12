package com.example.githubuserfinalbfaa

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.githubuserfinalbfaa.adapter.FavoriteAdapter
import com.example.githubuserfinalbfaa.adapter.SectionsPagerAdaper
import com.example.githubuserfinalbfaa.db.DatabaseContract
import com.example.githubuserfinalbfaa.db.GitHelper
import com.example.githubuserfinalbfaa.helper.MappingHelper
import com.example.githubuserfinalbfaa.model.UserModel
import com.example.githubuserfinalbfaa.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.math.log

class DetailActivity : AppCompatActivity() {

    private var isFavorite = false
    private var menuItem: Menu? = null
    private lateinit var gitHelper: GitHelper
    private var userModel: UserModel? = null
    private lateinit var detailViewModel: DetailViewModel

    companion object {
        internal val TAG = DetailActivity::class.java.simpleName
        const val EXTRA_STATE = "extra_state"
        const val EXTRA_FAV_POSITION = "extra_fav_position"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        gitHelper = GitHelper.getInstance(applicationContext)
        gitHelper.open()

        userModel = intent.getParcelableExtra(EXTRA_STATE) as UserModel
        tv_detail_loginname.text = userModel?.login
        Glide.with(this).load(userModel?.avatar).into(img_detail_user)

        //Cek Favorite //masih Error untuk cek data yang sudah dimasukan ke dalam DB
        //Bisa cek dengan gitHelper.queryAll lalu tambahkan metode Mapping.mapcursorToArrayList


        val dataFav = gitHelper.queryAll()
        for (data in dataFav) {
            if (this.userModel?.login == data.login) {
                isFavorite = true
                Log.d(TAG, "this User Favorite")
            }
        }

        setFollowerFollowing(userModel!!)

        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)
        detailViewModel.setDetailUser(userModel?.login)
        detailViewModel.getDetailData().observe(this, Observer { userModel ->
            if (userModel != null) {
                tv_detail_username.text = userModel.name
                tv_detail_company.text = userModel.company
                tv_detail_location.text = userModel.location
                tv_detail_blog.text = userModel.blog
                Log.d(TAG, "GetDetail Success")
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.favorite_menu, menu)
        setIconFavorite(menu)
        this.menuItem = menu
        return super.onCreateOptionsMenu(menu)
    }

    private fun setIconFavorite(menu: Menu) {
        if (isFavorite){
            menu.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_black_24dp)
        } else {
            menu.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_black_24dp)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_favorite -> setFavorite()
        }
        return true
    }

    private fun setFavorite() {
        if (isFavorite) {
            userModel?.let {
                gitHelper.deleteById("${it.id}")
                Log.d(TAG, "Favorite deleted")
                menuItem?.let { mn ->
                    isFavorite = false
                    setIconFavorite(mn)
                }
            }
        }else {
            val values = ContentValues()
            //values.put(DatabaseContract.GitColumns._ID)
            values.put(DatabaseContract.GitColumns.LOGIN_NAME, userModel?.login)
            values.put(DatabaseContract.GitColumns.AVATAR, userModel?.avatar)
            val result = gitHelper.insert(values)
            userModel?.id = result.toInt()
            Log.d(TAG, "Favorite Added")
            menuItem?.let { mn ->
                isFavorite = true
                setIconFavorite(mn)
            }
        }
    }


    private fun setFollowerFollowing(data: UserModel) {
        val sectionsPagerAdaper = SectionsPagerAdaper(this, supportFragmentManager)
        sectionsPagerAdaper.setData(data.login.toString())
        view_pager.adapter = sectionsPagerAdaper
        tabs.setupWithViewPager(view_pager)
        supportActionBar?.elevation = 0f

        val bundle = Bundle()
        val followerFragment = FollowerFragment()
        bundle.putString(FollowerFragment.EXTRA_FOLLOWERS, data.login)
        followerFragment.arguments = bundle
        val followingFragment = FollowingFragment()
        bundle.putString(FollowingFragment.EXTRA_FOLLOWING, data.login)
        followingFragment.arguments = bundle
    }

}
