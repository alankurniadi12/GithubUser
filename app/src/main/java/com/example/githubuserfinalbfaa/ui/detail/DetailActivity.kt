package com.example.githubuserfinalbfaa.ui.detail

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.net.Uri
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuserfinalbfaa.ui.favorite.FavoriteActivity
import com.example.githubuserfinalbfaa.R
import com.example.githubuserfinalbfaa.db.DatabaseContract
import com.example.githubuserfinalbfaa.db.DatabaseContract.CONTENT_URI
import com.example.githubuserfinalbfaa.helper.MappingHelper
import com.example.githubuserfinalbfaa.model.UserModel
import com.example.githubuserfinalbfaa.ui.detail.followers.FollowersActivity
import com.example.githubuserfinalbfaa.ui.home.MainActivity
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private var isFavorite = false
    private var menuItem: Menu? = null
    private var dataMain: UserModel? = null
    private var fromFavorite: String? = null
    private var fromMainActivity: String? = null
    private lateinit var listRepoAdapter: ListRepoAdapter
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var uriWithId: Uri


    companion object {
        internal val TAG = DetailActivity::class.java.simpleName
        const val EXTRA_STATE = "extra_state"
        const val EXTRA_FAV = "extra_fav"
        const val EXTRA_MAIN = "extra_main"
    }

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setSupportActionBar(toolbar_detail)

        listRepoAdapter = ListRepoAdapter()

        fromFavorite = intent.getStringExtra(EXTRA_FAV)
        fromMainActivity = intent.getStringExtra(EXTRA_MAIN)

        dataMain = intent.getParcelableExtra(EXTRA_STATE)
        Log.e(TAG, "getParcelableExtra: $dataMain")

        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + dataMain?.id)
        val dataGitFav = contentResolver?.query(uriWithId, null, null, null, null)
        cekFavorite(dataGitFav)

        dataMain?.let {mData ->
            setDataDetail(mData)
        }


        view_followers.setOnClickListener {
            val intent = Intent(this@DetailActivity, FollowersActivity::class.java)
            intent.putExtra(FollowersActivity.EXTRA_FOLLOWERS, dataMain)
            startActivity(intent)
        }

        view_following.setOnClickListener {
            val intent = Intent(this@DetailActivity, FollowersActivity::class.java)
            intent.putExtra(FollowersActivity.EXTRA_FOLLOWING, dataMain)
            startActivity(intent)
        }
    }


    private fun cekFavorite(dataGitFav: Cursor?) {
        val dataGitObject = MappingHelper.mapCursorToArrayList(dataGitFav)
        for (data in dataGitObject) {
            if (this.dataMain?.id == data.id) {
                Log.e(TAG, "cekFavorite dataGitObject: $dataGitObject")
                Log.e(TAG, "cekFavorite data: $data")
                isFavorite = true
            }
        }
    }

    private fun setDataDetail(data: UserModel) {
        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            DetailViewModel::class.java)
        detailViewModel.setDetailUser(data.login)
        detailViewModel.getDetailData().observe(this, Observer { userModel ->
            if (userModel != null ) {
                tv_title_detail.text = userModel.login
                tv_detail_username.text = userModel.name
                tv_detail_company.text = userModel.company
                tv_detail_location.text = userModel.location
                tv_detail_blog.text = userModel.blog
                tv_repo_value.text = userModel.repository.toString()
                tv_follower_value.text = userModel.follower.toString()
                tv_following_value.text = userModel.following.toString()
                Glide.with(this)
                    .load(userModel.avatar)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_loading))
                    .error(R.drawable.ic_error)
                    .into(img_detail_user)
                Log.e(TAG, "Observer viewmodel: $userModel")
            }
        })
        detailViewModel.setRepo(data.login).observe(this, Observer {
            Log.e("DetailActivity", "Data setRepo: $it")
            if (it != null) {
                listRepoAdapter.setData(it)
                rv_repo.layoutManager = LinearLayoutManager(this)
                rv_repo.adapter = listRepoAdapter
            }

        })
    }



    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            //Cek. Apakah ke DetailActivity dari listitem hasil pencarian atau listitem favorite?
            if (fromMainActivity != null) {
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
            menu.getItem(0)?.icon = ContextCompat.getDrawable(this,
                R.drawable.ic_favorite_black_24dp
            )
        } else {
            menu.getItem(0)?.icon = ContextCompat.getDrawable(this,
                R.drawable.ic_favorite_border_black_24dp
            )
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
            dataMain?.let {
                //gitHelper.deleteById("${it.id}")
                contentResolver.delete(uriWithId, null, null)
                Log.d(TAG, "Favorite deleted")
                showToastMessage("${it.login} Unfavorite")
                menuItem?.let { mn ->
                    isFavorite = false
                    setIconFavorite(mn)
                }
            }
        }else {
            val values = ContentValues()
            values.put(DatabaseContract.GitColumns._ID, dataMain?.id)
            values.put(DatabaseContract.GitColumns.LOGIN_NAME, dataMain?.login)
            values.put(DatabaseContract.GitColumns.AVATAR, dataMain?.avatar)
            contentResolver.insert(CONTENT_URI, values)
            Log.e(TAG, "InsertData: $values")
            showToastMessage("${dataMain?.login} Favorite")
            menuItem?.let { mn ->
                isFavorite = true
                setIconFavorite(mn)
            }
        }
    }


    private fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}
