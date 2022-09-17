package com.example.paypalsdksample

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder

object Networking {

    private val gson = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()

    fun fetchAccessToken(callback: (String) -> Unit) {
        Fuel.post("http://10.0.2.2:8080/access_tokens")
            .response { request, response, result ->
                val (bytes, error) = result
                if (bytes != null) {
                    var jsonString = String(bytes)
                    var accessTokenModel = gson.fromJson(jsonString, AccessTokenResult::class.java)
                    callback(accessTokenModel.access_token)
                }
            }
    }

    fun fetchOrderID(orderRequest: OrderRequest, callback: (String) -> Unit) {
        var jsonPostBody = gson.toJson(orderRequest)

        Fuel.post("http://10.0.2.2:8080/orders")
            .header(Headers.CONTENT_TYPE, "application/json")
            .body(jsonPostBody)
            .response { request, response, result ->
                val (bytes, error) = result
                if (bytes != null) {
                    var gson = Gson()

                    var jsonString = String(bytes)
                    var orderModel = gson.fromJson(jsonString, OrderResult::class.java)
                    callback(orderModel.id)
                }
            }
    }
}