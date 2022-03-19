package com.codepath.apps.restclienttemplate

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.codepath.apps.restclienttemplate.models.Tweet
import org.w3c.dom.Text

class TweetsAdapter (private val context: Context, val tweets: ArrayList<Tweet>) : RecyclerView.Adapter<TweetsAdapter.ViewHolder>(){

    //responsible for inflating the layout we want to use for each item we need to display
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetsAdapter.ViewHolder {
        //val context = parent.context
        val inflater = LayoutInflater.from(context)

        //inflate our item layout
        val view = inflater.inflate(R.layout.item_tweet,parent,false)

        //return viewholder with the the view we just inflated
        return ViewHolder(view)
    }

    //populating data into the item through holder
    override fun onBindViewHolder(holder: TweetsAdapter.ViewHolder, position: Int) {
        //Get the data model based on position
        val tweet: Tweet = tweets[position]

        //Set item views based on views and data model

        holder.tvUsername.text = tweet.user?.name
        holder.tvTweetBody.text = tweet.body

        //use glide for images. requires a context,
        //so use the itemView(inflated view from onCreate) of the viewHolder class
        Glide.with(holder.itemView).load(tweet.user?.publicImgUrl).transform(RoundedCorners(100)).into(holder.ivProfileImage)
    }

    //how many views will be in our recyclerView
    override fun getItemCount(): Int {
        return tweets.size
    }

    // Clean all elements of the recycler
    fun clear() {
        tweets.clear()
        notifyDataSetChanged()
    }

    // Add a list of items -- change to type used
    fun addAll(tweetList: List<Tweet>) {
        tweets.addAll(tweetList)
        notifyDataSetChanged()
    }

    inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener  {
        val ivProfileImage = itemView.findViewById<ImageView>(R.id.ivProfileImage)
        val tvUsername = itemView.findViewById<TextView>(R.id.tvUsername)
        val tvTweetBody = itemView.findViewById<TextView>(R.id.tvTweetBody)

        init{
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            //1. Get notified of the particular tweet which was clicked
            val tweet = tweets[bindingAdapterPosition]
            //2. Use the intent system to navigate to new activity
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("Tweet_Content", tweet)

            context.startActivity(intent)
        }
    }


}