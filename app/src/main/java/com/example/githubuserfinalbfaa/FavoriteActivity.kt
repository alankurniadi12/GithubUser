package com.example.githubuserfinalbfaa

import android.content.Intent
import android.graphics.DiscretePathEffect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.githubuserfinalbfaa.adapter.FavoriteAdapter
import com.example.githubuserfinalbfaa.db.GitHelper
import com.example.githubuserfinalbfaa.helper.MappingHelper
import com.example.githubuserfinalbfaa.model.UserModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.android.synthetic.main.item_user.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var gitHelper: GitHelper
    private lateinit var adapter: FavoriteAdapter
    private var userModel: UserModel? = null
    private var data: Intent? = null
    private var position: Int = 0

    companion object {
        const val EXTRA_FAV = "extra_fav"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Favorite"


        adapter = FavoriteAdapter(this)
        showRecyclerView()

        gitHelper = GitHelper.getInstance(applicationContext)
        gitHelper.open()

        GlobalScope.launch(Dispatchers.Main) {
            val deferredGit = async(Dispatchers.IO) {
                gitHelper.queryAll()
            }
            val fav = deferredGit.await()
            if (fav.size > 0) {
                adapter.listFavorite = fav
            } else {
                adapter.listFavorite = ArrayList()
                showSnackbarMessage("Data isEmpty")
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val intent = Intent(this, MainActivity::class.java)
        onBackPressed()
        startActivity(intent)
        return super.onSupportNavigateUp()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        return super.onKeyDown(keyCode, event)

    }

    private fun showRecyclerView() {
        rv_favorite.adapter = adapter
        rv_favorite.layoutManager = LinearLayoutManager(this)
        rv_favorite.setHasFixedSize(true)

        /*rv_list_item.setOnClickListener(CustomOnItemClickListener(position, object : CustomOnItemClickListener.OnitemClickCallback {
            override fun onItemClicked(view: View, position: Int) {
                val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_STATE, userModel)
                startActivity(intent)
            }
        }))*/
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(rv_favorite, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        gitHelper.close()
    }

}
