package com.codepath.apps.restclienttemplate.models

import android.os.Parcelable
import org.json.JSONObject
import kotlinx.parcelize.Parcelize

@Parcelize
class User(var name: String = "", var screenName: String = "", var publicImgUrl: String = "") :
    Parcelable {
    companion object {
        fun fromJson(jsonObject: JSONObject) : User {
            val user = User()
            user.name = jsonObject.getString("name")
            user.screenName = jsonObject.getString("screen_name")
            user.publicImgUrl = jsonObject.getString("profile_image_url_https")
            return user
        }
    }
}