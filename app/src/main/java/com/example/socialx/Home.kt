package com.example.socialx

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.google.firebase.auth.FirebaseAuth
class Home : AppCompatActivity(), NewsItemClicked {
    private lateinit var mAdapter: NewsAdapter
    private lateinit var firebaseAuth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerview)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.addAuthStateListener(FirebaseAuth.AuthStateListener {
            if(firebaseAuth.currentUser==null){
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
        })
        recyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = NewsAdapter(this)
        recyclerView.adapter = mAdapter
        fetchData()
    }
    fun fetchData() {
        val url = "https://saurav.tech/NewsAPI/everything/cnn.json"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            {
                val newsJsonArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for (i in 0 until newsJsonArray.length()) {
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    val news = News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("description"),
                        newsJsonObject.getString("urlToImage"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getJSONObject("source"),
                        newsJsonObject.getString("publishedAt")
//                        newsJsonObject.getJSONArray("source")
                    )
                    newsArray.add(news)
                }

                mAdapter.updateNews(newsArray)
            },
            {

            }
        )
        Log.d("BANSAL", jsonObjectRequest.toString())
        Mysingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
    override fun onItemClicked(item: News) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }
    fun logout(view: View) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Log Out")
        builder.setMessage("Are you sure you want to Log Out")
        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            firebaseAuth.signOut()
        }
        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            dialog.cancel()
        }
        builder.show()
    }
    }