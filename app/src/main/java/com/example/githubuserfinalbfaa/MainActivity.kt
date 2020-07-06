package com.example.githubuserfinalbfaa

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserfinalbfaa.adapter.GitAdapter
import com.example.githubuserfinalbfaa.model.UserModel
import com.example.githubuserfinalbfaa.setting.SettingActivity
import com.example.githubuserfinalbfaa.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: GitAdapter
    private lateinit var mainViewModel: MainViewModel
    private var backPress: Long = 0
    private var backToast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = GitAdapter()
        adapter.notifyDataSetChanged()


        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
        mainViewModel.getGitSearch().observe(this, Observer { usermodel ->
            if (usermodel != null){
                adapter.setData(usermodel)
                showLoading(false)
                rv_user.visibility = View.VISIBLE
                showRecyclerView()
            }
        })
    }

    private fun showRecyclerView() {
        rv_user.layoutManager = LinearLayoutManager(this)
        rv_user.adapter = adapter

        adapter.setOnitemClickCallback(object : GitAdapter.OnitemClickCallback{
            override fun onItemClicked(data: UserModel) {
                val intentMain = Intent(this@MainActivity, DetailActivity::class.java)
                intentMain.putExtra(DetailActivity.EXTRA_STATE, data)
                intentMain.putExtra(DetailActivity.EXTRA_MAIN, "mainactivity")
                startActivity(intentMain)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search_user).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isEmpty()) {
                    return true
                } else {
                    showLoading(true)
                    mainViewModel.setSearchUserGit(query)
                }
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_favorite -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.menu_setting -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return false
    }

    private fun showLoading(state: Boolean){
        if (state){
            progress_bar.visibility = View.VISIBLE
        } else {
            progress_bar.visibility = View.GONE
        }
    }

    /*private fun showShimmer(state: Boolean) {
        if (state) {
            shimmer_list_user.startShimmer()
        }else {
            shimmer_list_user.stopShimmer()
            shimmer_list_user.visibility = View.GONE
        }
    }*/

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        backToast = Toast.makeText(baseContext, "Press back again to exit!", Toast.LENGTH_SHORT)
        if (backPress + 2000 > System.currentTimeMillis()) {
            val exit = Intent(Intent.ACTION_MAIN)
            exit.addCategory(Intent.CATEGORY_HOME)
            exit.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(exit)
            backToast?.cancel()
            return super.onKeyDown(keyCode, event)
        } else {
            backToast?.show()
        }
        backPress = System.currentTimeMillis()
        return true
    }
}
