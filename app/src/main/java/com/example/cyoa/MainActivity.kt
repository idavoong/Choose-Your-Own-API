package com.example.chooseyourownapi

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class MainActivity : AppCompatActivity() {
    var counter = 0
    var imageURL = ""
    var camera = ""
    var rover = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val image = findViewById<ImageView>(R.id.image)
        val info2 = findViewById<TextView>(R.id.info2)
        val info3 = findViewById<TextView>(R.id.info3)
        val button = findViewById<Button>(R.id.button)

        getNextInfo(button, image)
    }

    private fun getImage() {
        val client = AsyncHttpClient()

        client["https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=1000&api_key=4K9ULGEcA56SlVmf8IVLh9Cc7aF3yPvcohvGxd0c", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                val nasaData = json.jsonObject.getJSONArray("photos")

                val oneData = nasaData.getJSONObject(counter)
                imageURL = oneData.getString("img_src")
                camera = oneData.getJSONObject("camera").getString("full_name")
                rover = oneData.getJSONObject("rover").getString("name")

                counter += 1

                Log.d("json", imageURL)
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("App Error", errorResponse)
            }
        }]
    }

    private fun getNextInfo(button: Button, imageView: ImageView) {
        button.setOnClickListener {
            getImage()

            Glide.with(this)
                .load(imageURL)
                .fitCenter()
                .into(imageView)
        }
    }
}