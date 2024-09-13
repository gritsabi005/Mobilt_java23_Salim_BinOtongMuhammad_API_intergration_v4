package com.example.openweather

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
//import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject
import coil.load
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date


class MainActivity3 : AppCompatActivity() {
    lateinit var logOutButtonWeather : Button
    lateinit var registerButtonWeather : Button
    lateinit var shakeButton : Button
    lateinit var inputCityText : EditText
    lateinit var searchButton : Button
    lateinit var weatherIconPNG : ImageView
    lateinit var todayInfoPer3Hour : TextView


    var key:String = "dda6b16ec8a316b37d36657a227c4111"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main3)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        inputCityText = findViewById(R.id.editTextTextInputCity)
        searchButton = findViewById(R.id.button2Search)


        searchButton.setOnClickListener({
            var city:String = inputCityText.text.toString()
            var rq: RequestQueue = Volley.newRequestQueue(this)
            var url = "https://api.openweathermap.org/data/2.5/forecast?q="+city+"&appid="+key+"&units=metric"

            var request: StringRequest = StringRequest (Request.Method.GET, url, { response ->
                Log.d("Weatherinformation", "Weather Information " + response)
                var iconCode = JSONObject(response).getJSONArray("list").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("icon")
                Log.d("iconcode", "icon is: " + iconCode)
                val currentTime = JSONObject(response).getJSONArray("list").getJSONObject(0).getString("dt_txt")
                val weatherInfoPer3Hour = JSONObject(response).getJSONArray("list").getJSONObject(0).getJSONObject("main").getString("temp")
                showIcon(city)
                showInfoPer3Hour(city, currentTime, weatherInfoPer3Hour)

                inputCityText.text.clear()
            }, {
                Log.d("Weatherinformation", "fail")
            })
            rq.cache.clear()
            rq.add(request)
        })


        logOutButtonWeather = findViewById(R.id.button4LogOutWeather)
        registerButtonWeather = findViewById(R.id.button5)
        shakeButton = findViewById(R.id.button6Shake)

        logOutButtonWeather.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }

        registerButtonWeather.setOnClickListener {
            val intent = Intent(this, MainActivity5::class.java)
            startActivity(intent)
        }

        shakeButton.setOnClickListener {
            val intent = Intent(this, MainActivity4::class.java)
            startActivity(intent)
        }

    }
    private fun showInfoPer3Hour(cityName: String, currentTime: String, currentWeather: String){
        todayInfoPer3Hour = findViewById(R.id.textView2InfoPer3Hour)
        todayInfoPer3Hour.setText(cityName + "'s weather \n\n" + currentTime + "\n" + currentWeather + "°C")

    }

    private fun showIcon(cityName: String){
        var rq: RequestQueue = Volley.newRequestQueue(this)
        var url = "https://api.weatherapi.com/v1/current.json?key=0e61e15f7f7e44b797a93849241209&q="+cityName+"&aqi=no"
        weatherIconPNG = findViewById(R.id.imageView2BigPng)

        var request: StringRequest = StringRequest(Request.Method.GET, url, { response ->

            Log.i("salim", "success" + response)
            val weatherArray: JSONObject = JSONObject(response).getJSONObject("current")
            val jsnObjMain: JSONObject = weatherArray.getJSONObject("condition")
            val icon: String = jsnObjMain.getString("icon")  // Get the icon URL from the API response
            weatherIconPNG.load("https://" + icon)

        }, {
            Log.i("salim", "failed to get weather data")
        })
        // Add request to the request queue
        rq.add(request)
    }

    /*
    weatherIconPNG = findViewById(R.id.imageView2BigPng)
        //Log.d("code", "code retrieved")
        //.setImageResource(getResource().getIdentifier("https://openweathermap.org/img/wn/"+pngCode+"@24.png"))
        //Picasso.get().load("https://cdn.pixabay.com/photo/2024/02/28/07/42/european-shorthair-8601492_640.jpg"/*"https://openweathermap.org/img/wn/"+pngCode+"@4x.png"*/).into(weatherIconPNG)
        Log.d("code", "code retrieved")
        //Glide.with(this).load("https://openweathermap.org/img/wn/" + pngCode + "@2x.png").into(weatherIconPNG)
        //weatherIconPNG.load("https://cdn.pixabay.com/photo/2024/02/28/07/42/european-shorthair-8601492_640.jpg"/*"https://openweathermap.org/img/wn/" + pngCode +"@2x.png"*/)
    */

    /*var rq: RequestQueue = Volley.newRequestQueue(this)
    var url = "https://api.weatherapi.com/v1/current.json?key=0e61e15f7f7e44b797a93849241209&q=malmö&aqi=no"

    var request: StringRequest = StringRequest (Request.Method.GET, url, { response ->
        Log.d("Weatherinformation", "Weather Information " + response)

        Log.i("comment", "success" + response)
        /*Array*/val weatherArray:JSONObject = JSONObject(response).getJSONObject("current")
        val temp:String = weatherArray.getString("temp_c")
        /*temp*/Log.i("comment", "onCreate: " + temp + weatherArray)
        val jsnObjMain:JSONObject = weatherArray.getJSONObject("condition")
        val text:String = jsnObjMain.getString("text")
        val icon:String = jsnObjMain.getString("icon")
        val humidity:String = weatherArray.getString("humidity")

            val iconCode = JSONObject(response).getJSONArray("list").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("icon")
            Log.d("iconcode", "icon is: " + iconCode)
            val currentTime = JSONObject(response).getJSONArray("list").getJSONObject(0).getString("dt_txt")
            val weatherInfoPer3Hour = JSONObject(response).getJSONArray("list").getJSONObject(0).getJSONObject("main").getString("temp")
            showIcon(iconCode)
            showInfoPer3Hour(city, currentTime, weatherInfoPer3Hour)
            showInfoPerDay(JSONObject(response))
            inputCityText.text.clear()
        }, {
            Log.d("Weatherinformation", "fail")
        })
        rq.cache.clear()
        rq.add(request)*/
    /*private fun showAPI(){

        var rq: RequestQueue = Volley.newRequestQueue(this)
        var url = "https://api.thecatapi.com/v1/images/search"

        var request: StringRequest = StringRequest (Request.Method.GET, url, { response ->
            //Log.d("Weatherinformation", "Weather Information " + response)
            val iconCode = JSONObject(response).getJSONArray("list").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("icon")
            //Log.d("iconcode", "icon is: " + iconCode)
            //val currentTime = JSONObject(response).getJSONArray("list").getJSONObject(0).getString("dt_txt")
            //val weatherInfoPer3Hour = JSONObject(response).getJSONArray("list").getJSONObject(0).getJSONObject("main").getString("temp")
            //showIcon(iconCode)
            //showInfoPer3Hour(city, currentTime, weatherInfoPer3Hour)
            //showInfoPerDay(JSONObject(response))
            inputCityText.text.clear()
        }, {
            Log.d("Weatherinformation", "fail")
        })
        rq.cache.clear()
        rq.add(request)
    }*/
}

/*
public void showPics(int randomNumber){
        ImageView dicePics = findViewById(R.id.dicePics);
        dicePics.setImageResource(getResources().getIdentifier("dice" + randomNumber, "drawable", getPackageName()));

    }

setImageIcon()*/