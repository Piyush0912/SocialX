package com.example.socialx

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.w3c.dom.Text

//class NewsAdapter(private val listener: Home): RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
//
//    private val items: ArrayList<News> = ArrayList()
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_item, parent, false)
//        val viewHolder = NewsViewHolder(view)
//        view.setOnClickListener{
//            listener.onItemClicked(items[viewHolder.adapterPosition])
//        }
//        return viewHolder
//    }
//    override fun getItemCount(): Int {
//        return items.size
//    }
//    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
//        val currentItem = items[position]
//        holder.titleView.text = currentItem.title
////        holder.publish.text = currentItem.author
//////        holder.source.text = currentItem.source
////        holder.description.text = currentItem.description
//        Glide.with(holder.itemView.context).load(currentItem.urlToImage).into(holder.newsImage)
//    }
//
//    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val titleView: TextView = itemView.findViewById(R.id.Title)
//        val newsImage: ImageView = itemView.findViewById(R.id.image)
////        val publish: TextView = itemView.findViewById(R.id.Publish)
////        val source : TextView = itemView.findViewById(R.id.source)
////        val description : TextView = itemView.findViewById(R.id.Description)
//    }
//
//    fun updateNews(updatedNews: ArrayList<News>) {
//        items.clear()
//        items.addAll(updatedNews)
//
//        notifyDataSetChanged()
//    }
//}
//interface NewsItemClicked{
//    fun onItemClicked(item: News)
//}
class NewsAdapter(private val listener: NewsItemClicked): RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private val items: ArrayList<News> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_item, parent, false)
        val viewHolder = NewsViewHolder(view)
        view.setOnClickListener{
            listener.onItemClicked(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }
    override fun getItemCount(): Int {
        return items.size
    }
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = items[position]
        holder.titleView.text = currentItem.title
        holder.description.text = currentItem.description
        holder.source.text = currentItem.src.getString("name")
        holder.publish.text = currentItem.publishedAt
        Glide.with(holder.itemView.context).load(currentItem.imageUrl).into(holder.image)
    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleView: TextView = itemView.findViewById(R.id.Title)
        val image: ImageView = itemView.findViewById(R.id.image)
        val description : TextView = itemView.findViewById(R.id.Description)
        val source : TextView = itemView.findViewById(R.id.source)
        val publish : TextView = itemView.findViewById(R.id.Publish)
    }

    fun updateNews(updatedNews: ArrayList<News>) {
        items.clear()
        items.addAll(updatedNews)

        notifyDataSetChanged()
    }

}

interface NewsItemClicked {
    fun onItemClicked(item: News)
}
