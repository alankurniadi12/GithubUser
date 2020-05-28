package com.example.githubuserfinalbfaa

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.githubuserfinalbfaa.adapter.FavoriteAdapter
import com.example.githubuserfinalbfaa.adapter.SectionsPagerAdaper
import com.example.githubuserfinalbfaa.db.DatabaseContract
import com.example.githubuserfinalbfaa.db.GitHelper
import com.example.githubuserfinalbfaa.model.UserModel
import com.example.githubuserfinalbfaa.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private var isFavorite = false
    private var menuItem: Menu? = null
    private lateinit var gitHelper: GitHelper
    private lateinit var userModel: UserModel

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var adapter: FavoriteAdapter

    companion object {
        internal val TAG = DetailActivity::class.java.simpleName
        const val EXTRA_STATE = "extra_state"
        const val EXTRA_FAV = "extra_fav"
        const val EXTRA_FAV_POSITION = "extra_fav_position"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        adapter = FavoriteAdapter(this)
        gitHelper = GitHelper.getInstance(applicationContext)
        gitHelper.open()


        val model = intent.getParcelableExtra(EXTRA_STATE) as UserModel
        tv_detail_loginname.text = model.login
        Glide.with(this).load(model.avatar).into(img_detail_user)
        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)
        detailViewModel.setDetailUser(model.login)

        detailViewModel.getDetailData().observe(this, Observer { userModel ->
            if (userModel != null) {
                Log.d(TAG, "GetDetail Success")
                tv_detail_username.text = userModel.name
                tv_detail_company.text = userModel.company
                tv_detail_location.text = userModel.location
                tv_detail_blog.text = userModel.blog
            }
        })
        //isFavoriteCheck(userModel)

        val sectionsPagerAdaper = SectionsPagerAdaper(this, supportFragmentManager)
        sectionsPagerAdaper.setData(model.login.toString())
        view_pager.adapter = sectionsPagerAdaper
        tabs.setupWithViewPager(view_pager)
        supportActionBar?.elevation = 0f

        val bundle = Bundle()
        val followerFragment = FollowerFragment()
        bundle.putString(FollowerFragment.EXTRA_FOLLOWERS, model.login)
        followerFragment.arguments = bundle
        val followingFragment = FollowingFragment()
        bundle.putString(FollowingFragment.EXTRA_FOLLOWING, model.login)
        followingFragment.arguments = bundle

    }

    private fun isFavoriteCheck(userModel: UserModel) {
        isFavorite = true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuItem = menu
        menuInflater.inflate(R.menu.favorite_menu, menu)
        if (!isFavorite){
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_black_24dp)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.add_favorite ->{
                val favData = intent.getParcelableExtra(EXTRA_STATE) as UserModel
                if (!isFavorite){
                    gitHelper.deleteByLoginName(favData.login.toString()).toLong()
                    Log.d(TAG, "Hapus data favorite dari DB!!")

                    menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_black_24dp)
                    Toast.makeText(this, "Favorite removed", Toast.LENGTH_SHORT).show()
                } else{
                    val values = ContentValues()
                    values.put(DatabaseContract.GitColumns.LOGIN_NAME, favData.login)
                    values.put(DatabaseContract.GitColumns.AVATAR, favData.avatar)
                    gitHelper.insert(values)
                    Log.d(TAG, "Data favorite berhasil dimasukan ke DB!!")

                    menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_black_24dp)
                    Toast.makeText(this, "Favorite added", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        gitHelper.close()
    }
}
