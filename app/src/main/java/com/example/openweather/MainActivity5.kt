package com.example.openweather

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.RequestQueue
import com.android.volley.Request

import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONObject

class MainActivity5 : AppCompatActivity() {

    lateinit var triviaOne : TextView
    lateinit var triviaTwo : TextView
    lateinit var triviaTextField : EditText
    lateinit var triviaButton : Button
    lateinit var logOutButtonWeather5 : Button
    lateinit var weatherButton5 : Button
    lateinit var shakeButton5 : Button
    lateinit var cityCode : String

    val key: String = "dda6b16ec8a316b37d36657a227c4111"

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main5)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        triviaOne = findViewById(R.id.textView2TriviaOne)
        triviaTwo = findViewById(R.id.textView3TriviaTwo)
        triviaTextField = findViewById(R.id.editTextTextTriviaText)
        triviaButton = findViewById(R.id.button2Trivia)

        logOutButtonWeather5 = findViewById(R.id.button2)
        weatherButton5 = findViewById(R.id.button4)
        shakeButton5 = findViewById(R.id.button6)

        triviaButton.setOnClickListener {
            val city = triviaTextField.text.toString()
            if (city.isNotEmpty()) {

                val rq: RequestQueue = Volley.newRequestQueue(this)
                val url = "https://api.openweathermap.org/data/2.5/forecast?q="+city+"&appid="+key+"&units=metric"

                val request = StringRequest(Request.Method.GET, url, { response ->
                    Log.d("WeatherInformation", "Weather Information: " + response)
                    getCountryId(JSONObject(response))
                    triviaOne.text = "5 days weather forecast for -\nCity: " + city + ", " + cityCode
                    showInfoPerDay(JSONObject(response))

                }, {
                    Log.d("WeatherInformation", "Failed to retrieve weather data")
                })

                rq.cache.clear()
                rq.add(request)

                // Clear the input field after searching
                triviaTextField.text.clear()
            }
        }

        logOutButtonWeather5.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }

        weatherButton5.setOnClickListener {
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
        }

        shakeButton5.setOnClickListener {
            val intent = Intent(this, MainActivity4::class.java)
            startActivity(intent)
        }
    }

    private fun getCountryId(response: JSONObject){
        val list = response.getJSONObject("city")
        cityCode = list.getString("country")
    }

    private fun showInfoPerDay(response: JSONObject){
        Log.d("checkResponse", "Yo its here: " + response)
        val list = response.getJSONArray("list")
        val hashMapDates = HashMap<String, MutableList<JSONObject>>()
        val appendItHere = StringBuilder()
        triviaTwo = findViewById(R.id.textView3TriviaTwo)


        // Within textView, you will show (loop all the) only todays per 3 hour weather forecast, when you pressed the next or left button, it will loop to other day.
        for (i in 0 until /*minOf(list.length(), 3)*/ list.length()){
            val perDayDate = list.getJSONObject(i).getString("dt_txt")
            val forLater = list.getJSONObject(i)
            val perDayTemp = list.getJSONObject(i).getJSONObject("main").getString("temp")

            //appendItHere.append(perDayDate).append(": ").append(perDayTemp).append(" °C\n")

            val dateOnItsOwn = perDayDate.split(" ")[0]
            if (!hashMapDates.containsKey(dateOnItsOwn)){
                hashMapDates[dateOnItsOwn] = mutableListOf()
            }

            val weatherList = hashMapDates[dateOnItsOwn]!!
            weatherList.add(forLater)

            // is there any way to loop and then make an array if the dt_txt is the same? and if its not the same it will start create another array? later these arrays will be called accordingly by the buttons.
        }

        for((datess, weatherss) in hashMapDates){
            appendItHere.append(datess + "\n")
            for (weathersss in weatherss){
                val timeInput = weathersss.getString("dt_txt").split(" ")[1].substring(0, 5)
                val tempInput = weathersss.getJSONObject("main").getString("temp")
                appendItHere.append(timeInput +" - " + tempInput + " °C\n")
            }
            appendItHere.append("\n")
        }

        triviaTwo.setText(appendItHere.toString())
        triviaTwo.movementMethod = android.text.method.ScrollingMovementMethod()

    }
}