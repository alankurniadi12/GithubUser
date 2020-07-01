package com.alankurniadi.consumerapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.alankurniadi.consumerapp.adapter.AdapterConsumer
import com.alankurniadi.consumerapp.db.DatabaseContract.CONTENT_URI
import com.alankurniadi.consumerapp.helper.MappingHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: AdapterConsumer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Favorite List"

        adapter = AdapterConsumer(this)

        GlobalScope.launch(Dispatchers.Main) {
            val deferredGit = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val fav = deferredGit.await()
            if (fav.size > 0) {
                adapter.listFavorite = fav
            }else {
                adapter.listFavorite = ArrayList()
                showSnackbarMessage("Data isEmpty")
            }
        }

        showRecycletView()


    }

    private fun showRecycletView() {
        rv_user.adapter = adapter
        rv_user.layoutManager = LinearLayoutManager(this)
        rv_user.setHasFixedSize(true)
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(rv_user, message, Snackbar.LENGTH_SHORT).show()
    }
}
