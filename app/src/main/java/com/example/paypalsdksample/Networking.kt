package com.example.paypalsdksample

import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson

object Networking {
    fun fetchAccessToken(callback: (String) -> Unit) {
        Fuel.post("http://10.0.2.2:8080/access_tokens")
            .response { request, response, result ->
                val (bytes, error) = result
                if (bytes != null) {
                    var gson = Gson()

                    var jsonString = String(bytes)
                    var accessTokenModel = gson.fromJson(jsonString, AccessToken::class.java)
                    callback(accessTokenModel.access_token)
                }
            }
    }
}