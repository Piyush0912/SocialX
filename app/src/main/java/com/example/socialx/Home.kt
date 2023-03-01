package com.example.socialx

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import okhttp3.Request;
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.JsonObjectRequest
import com.google.firebase.auth.FirebaseAuth
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class Home : AppCompatActivity(), NewsItemClicked {
    private lateinit var mAdapter: NewsAdapter
    private lateinit var firebaseAuth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val searchView = findViewById<SearchView>(R.id.searchView)
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
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // Handle the search query when the user presses the search button or hits enter
                searchNews(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // Handle the search query as the user types
                return false
            }
        })
    }

    private fun searchNews(query: String) {
        val apiKey = "1b778e89dc154ff3949afb013e03ddc0"
        val url = "https://newsapi.org/v2/everything?q=$query&apiKey=$apiKey"

        val request = Request.Builder()
            .url(url)
            .build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle the error
//                Toast.makeText(this@Home, "error", Toast.LENGTH_SHORT).show()
                e.message?.let { Log.d("error", it) }
            }

            override fun onResponse(call: Call, response: Response) {
//                Toast.makeText(this@Home, "succ", Toast.LENGTH_SHORT).show()
                Log.d("succ",response.message)
                val json = response.body?.string()

                // Parse the JSON response
                val data = JSONObject(json).getJSONArray("data")
                val firstItem = data.getJSONObject(0)
                val title = firstItem.getString("title")
                val description = firstItem.getString("description")

                // Display the data to the user
                // Parse the JSON response and display the news articles to the user
            }
        })
    }

    fun fetchData() {
        val url = "https://saurav.tech/NewsAPI/everything/cnn.json"
        val jsonObjectRequest = JsonObjectRequest(
            com.android.volley.Request.Method.GET,
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
//    fun logout(view: View) {
//        val builder = AlertDialog.Builder(this)
//        builder.setTitle("Log Out")
//        builder.setMessage("Are you sure you want to Log Out")
//        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
//            firebaseAuth.signOut()
//        }
//        builder.setNegativeButton(android.R.string.no) { dialog, which ->
//            dialog.cancel()
//        }
//        builder.show()
//    }
    }