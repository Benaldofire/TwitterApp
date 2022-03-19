package com.codepath.apps.restclienttemplate

import android.content.Intent
import android.graphics.Color
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.android.material.snackbar.Snackbar
import okhttp3.Headers


class ComposeActivity : AppCompatActivity() {

    lateinit var etCompose: EditText
    lateinit var tvCount: TextView
    lateinit var btnTweet: Button

    lateinit var client: TwitterClient

    val TAG = "ComposeActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etCompose = findViewById(R.id.etTweetCompose)
        btnTweet = findViewById(R.id.btnTweet)
        tvCount = findViewById(R.id.tvCount)

        client = TwitterApplication.getRestClient(this)

        //character counter and display text
        etCompose.addTextChangedListener( object : TextWatcher {


            override fun onTextChanged(text: CharSequence, start: Int, end: Int, count: Int) {
                tvCount.text = "characters: "+text.length.toString()

                //disable tweet button upon exceeding limit
                if (text.length > 280 ) {
                    btnTweet.setEnabled(false)
                    tvCount.setTextColor(Color.RED)
                } else {
                    tvCount.setTextColor(Color.BLACK)
                    btnTweet.setEnabled(true)
                }
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, end: Int, count: Int) {
                //Auto Generated
            }
            override fun afterTextChanged(p0: Editable?) {
                //Auto Generated
            }

        })

        //Handling user's click on tweet button
        btnTweet.setOnClickListener {
            //Grab the content of editText
            val tweetContent = etCompose.text.toString()

            //1. tweet isn't empty
            if (tweetContent.isEmpty()) {
                Toast.makeText(this, "Empty Tweets not allwoed", Toast.LENGTH_SHORT).show()
                // Look into displaying SnackBar message
            } else {
                //Make an api calL to Twitter to publish tweet
                client.publishTweet(tweetContent, object : JsonHttpResponseHandler(){
                    override fun onFailure(
                        statusCode: Int,
                        headers: Headers?,
                        response: String?,
                        throwable: Throwable?
                    ) {
                        Log.i(TAG, "Error posting tweet", throwable)
                    }

                    override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                        Log.i(TAG, "Successfully publiushed Tweet!")
                        //Send tweet back to TimelineActivity to show, by handling response and adding to tweetslist

                        val tweet = Tweet.fromJson(json.jsonObject)
                        val intent = Intent()
                        intent.putExtra("tweet", tweet)
                        setResult(RESULT_OK, intent)
                        finish()
                    }

                })
            }




        }
    }
}