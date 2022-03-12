package com.codepath.apps.restclienttemplate

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException

class TimelineActivity : AppCompatActivity() {

    lateinit var client: TwitterClient

    lateinit var rvTweets: RecyclerView

    lateinit var adapter: TweetsAdapter

    val tweets = ArrayList<Tweet>()

    lateinit var swipeContainer: SwipeRefreshLayout

    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    val TAG = "TimelineActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        client = TwitterApplication.getRestClient(this)

        swipeContainer = findViewById(R.id.swipeContainer)

        swipeContainer.setOnRefreshListener {
            Log.i(TAG,"Refreshing Timeline")
            populateHomeTimeline()
        }

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        );


        rvTweets = findViewById(R.id.rvTweets)
        adapter = TweetsAdapter(tweets)

        val linearLayoutManager = LinearLayoutManager(this)

        rvTweets.layoutManager = linearLayoutManager
        rvTweets.adapter = adapter

        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page)
            }
        }
        // Adds the scroll listener to RecyclerView
        // Adds the scroll listener to RecyclerView
        rvTweets.addOnScrollListener(scrollListener)

        populateHomeTimeline()

    }

    fun populateHomeTimeline() {
        client.getHomeTimeline(object: JsonHttpResponseHandler(){

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.i(TAG, "onFailure $response")
            }

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.i(TAG, "Successful $json")

                val jsonArray = json.jsonArray

                try {
                    //clear current tweets
                    adapter.clear()
                    //fromJsonArray method parses a whole json array into a list of tweets
                    val listOfNewTweets = Tweet.fromJsonArray(jsonArray)
                    tweets.addAll(listOfNewTweets)
                    adapter.notifyDataSetChanged()

                    // Now we call setRefreshing(false) to signal refresh has finished
                    swipeContainer.setRefreshing(false)

                } catch (e: JSONException) {
                    Log.e(TAG, "JSON Exception $e")
                }
            }

        })
    }

    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    fun loadNextDataFromApi(offset: Int) {
        Log.i(TAG,"New data to be appended")
        // Send an API request to retrieve appropriate paginated data
        var maxID:Long = tweets.get(tweets.size -1).id
        //get id of the last tweet on page
        //val maxID = tweets.get(tweets.size -1).id
        Log.i(TAG,"Max_ID $maxID")

        client.getNextTimeline(object: JsonHttpResponseHandler(){
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.i(TAG, "Next Page fetch Failure $response")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                val jsonArray = json.jsonArray
                Log.i(TAG, "Successful $json")

                try {
                    //fromJsonArray method parses a whole json array into a list of tweets
                    val listOfNewTweets = Tweet.fromJsonArray(jsonArray)
                    tweets.addAll(listOfNewTweets)
                    adapter.notifyDataSetChanged()

                } catch (e: JSONException) {
                    Log.e(TAG, "JSON Exception $e")
                }
            }

        }, maxID)
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
    }
}