package com.codepath.apps.restclienttemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.codepath.apps.restclienttemplate.models.Tweet

class DetailActivity : AppCompatActivity() {

    private lateinit var ivProfileImage: ImageView
    private lateinit var tvUsername: TextView
    private lateinit var tvTweetBody: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        ivProfileImage = findViewById(R.id.ivProfileImage)
        tvUsername = findViewById(R.id.tvUsername)
        tvTweetBody = findViewById(R.id.tvTweetBody)

        val tweet = intent.getParcelableExtra<Tweet>("Tweet_Content") as Tweet
        Glide.with(this).load(tweet.user?.publicImgUrl).transform(RoundedCorners(100)).into(this.ivProfileImage)
        tvUsername.text = tweet.user?.name
        tvTweetBody.text = tweet.body

    }
}