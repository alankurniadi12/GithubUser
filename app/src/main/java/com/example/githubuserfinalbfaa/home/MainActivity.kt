package com.example.githubuserfinalbfaa.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserfinalbfaa.DetailActivity
import com.example.githubuserfinalbfaa.FavoriteActivity
import com.example.githubuserfinalbfaa.R
import com.example.githubuserfinalbfaa.model.UserModel
import com.example.githubuserfinalbfaa.setting.SettingActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var resultViewModel: ResultViewModel
    private lateinit var resultAdapter: ResultAdapter
    private var backPress: Long = 0
    private var backToast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar_main)

        resultAdapter = ResultAdapter()
        resultAdapter.notifyDataSetChanged()

        resultViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(ResultViewModel::class.java)

        val searchView = findViewById<SearchView>(R.id.search)
        searchView.queryHint = resources.getString(R.string.hint_text)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isEmpty()) {
                    return true
                } else {
                    progress_bar.visibility = View.VISIBLE
                    resultViewModel.setSearchUserGit(query)
                }
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

        })

        resultViewModel.getGitSearch().observe(this, Observer { data ->
            Log.e("MainActivity", "ObserveData: $data")
            if (data != null) {
                resultAdapter.setData(data)
                progress_bar.visibility = View.GONE
                rv_search.visibility = View.VISIBLE
                rv_search.layoutManager = LinearLayoutManager(this)
                rv_search.adapter = resultAdapter

                resultAdapter.setOnitemClickCallback(object : ResultAdapter.OnitemClickCallback {
                    override fun onItemClicked(data: UserModel) {
                        val intent = Intent(this@MainActivity, DetailActivity::class.java)
                        intent.putExtra(DetailActivity.EXTRA_STATE, data)
                        intent.putExtra(DetailActivity.EXTRA_MAIN, "mainactivity")
                        startActivity(intent)
                    }
                })
            }else{
                progress_bar.visibility = View.GONE
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)

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
