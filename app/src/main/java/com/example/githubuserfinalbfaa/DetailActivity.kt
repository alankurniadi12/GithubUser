package com.example.githubuserfinalbfaa

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.githubuserfinalbfaa.adapter.SectionsPagerAdaper
import com.example.githubuserfinalbfaa.db.DatabaseContract
import com.example.githubuserfinalbfaa.db.GitHelper
import com.example.githubuserfinalbfaa.model.UserModel
import com.example.githubuserfinalbfaa.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private var isFavorite = false
    private var menuItem: Menu? = null
    private var userModel: UserModel? = null
    private var fromFavorite: String? = null
    private var fromMainAcitivity: String? = null
    private lateinit var gitHelper: GitHelper
    private lateinit var detailViewModel: DetailViewModel

    companion object {
        internal val TAG = DetailActivity::class.java.simpleName
        const val EXTRA_STATE = "extra_state"
        const val EXTRA_FAV = "extra_fav"
        const val EXTRA_MAIN = "extra_main"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar?.title = "Detail User"

        gitHelper = GitHelper.getInstance(applicationContext)
        gitHelper.open()

        fromFavorite = intent.getStringExtra(EXTRA_FAV)
        fromMainAcitivity = intent.getStringExtra(EXTRA_MAIN)

        userModel = intent.getParcelableExtra(EXTRA_STATE) as UserModel
        tv_detail_loginname.text = userModel?.login
        Glide.with(this).load(userModel?.avatar).into(img_detail_user)

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

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            //Cek. Apakah ke DetailActivity dari listitem hasil pencarian atau listitem favorite?
            if (fromMainAcitivity != null) {
                val intentMain = Intent(this, MainActivity::class.java)
                startActivity(intentMain)
            }else if (fromFavorite != null) {
                val intentFav = Intent(this, FavoriteActivity::class.java)
                startActivity(intentFav)
            }
        }
        return super.onKeyDown(keyCode, event)
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
                showToastMessage("${it.login} Unfavorite")
                menuItem?.let { mn ->
                    isFavorite = false
                    setIconFavorite(mn)
                }
            }
        }else {
            val values = ContentValues()
            values.put(DatabaseContract.GitColumns.LOGIN_NAME, userModel?.login)
            values.put(DatabaseContract.GitColumns.AVATAR, userModel?.avatar)
            val result = gitHelper.insert(values)
            userModel?.id = result.toInt()
            Log.d(TAG, "Favorite Added")
            showToastMessage("${userModel?.login} Favorite")
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

    private fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        gitHelper.close()
    }

}
