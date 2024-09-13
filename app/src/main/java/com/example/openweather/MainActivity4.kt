package com.example.openweather

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity4 : AppCompatActivity(), SensorEventListener {

    private var sound: MediaPlayer? = null
    private var volume: AudioManager? = null
    private var vibeIt: Vibrator? = null
    private var sensorIt: SensorManager? = null
    private var accelerateIt: Sensor? = null
    private var randomNumberByButton = 0
    private var randomNumberByShaking = 0
    private lateinit var textX: TextView
    private lateinit var textY: TextView
    private lateinit var textZ: TextView
    private var shakingDifference = 6f
    private var nowItsStill = 100f
    private var previousX = 0f
    private var previousY = 0f
    private var previousZ = 0f
    private var recordedTimeBeforeShaken: Long = 0
    private var shaken = false
    private lateinit var main: View
    lateinit var logOutButtonWeather4 : Button
    lateinit var registerButtonWeather4 : Button
    lateinit var weatherButton4 : Button

    override fun onStart() {
        super.onStart()
        Log.i("Salim", "onStart: ")
        sensorIt?.unregisterListener(this)
        sensorIt?.registerListener(this, accelerateIt, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onResume() {
        super.onResume()
        Log.i("Salim", "onResume: ")
        sensorIt?.unregisterListener(this)
        sensorIt?.registerListener(this, accelerateIt, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onRestart() {
        super.onRestart()
        Log.i("Salim", "onRestart: ")
        sensorIt?.unregisterListener(this)
        sensorIt?.registerListener(this, accelerateIt, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        Log.i("Salim", "onPause: ")
        vibeIt?.cancel()
        sound?.release()
        sensorIt?.unregisterListener(this)
    }

    override fun onStop() {
        super.onStop()
        Log.i("Salim", "onStop: ")
        vibeIt?.cancel()
        sound?.release()
        sensorIt?.unregisterListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("Salim", "onDestroy: ")
        vibeIt?.cancel()
        sound?.release()
        sensorIt?.unregisterListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main4)



        vibeIt = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        main = findViewById(R.id.main)

        logOutButtonWeather4 = findViewById(R.id.button4LogOutWeatherr)
        registerButtonWeather4 = findViewById(R.id.button55)
        weatherButton4 = findViewById(R.id.button6Shakee)

        logOutButtonWeather4.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }

        registerButtonWeather4.setOnClickListener {
            val intent = Intent(this, MainActivity5::class.java)
            startActivity(intent)
        }

        weatherButton4.setOnClickListener {
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)

            val soundBar: SeekBar = findViewById(R.id.soundBar)
            volume = getSystemService(Context.AUDIO_SERVICE) as AudioManager

            soundBar.max = volume!!.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            soundBar.progress = volume!!.getStreamVolume(AudioManager.STREAM_MUSIC)

            soundBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                    volume!!.setStreamVolume(AudioManager.STREAM_MUSIC, i, AudioManager.FLAG_VIBRATE)
                    vibeItt()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
            })

            sensorIt = getSystemService(Context.SENSOR_SERVICE) as SensorManager
            accelerateIt = sensorIt?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            sensorIt?.registerListener(this, accelerateIt, SensorManager.SENSOR_DELAY_NORMAL)

            val bThree: Button = findViewById(R.id.button3)
            bThree.setOnClickListener {
                randomNumberByButton = (1..6).random()
                val randomToast = (1..3).random()
                val randomRandomToast = (1..2).random()
                val random: String = when (randomToast) {
                    1 -> if (randomRandomToast == 1) "" else ""
                    2 -> if (randomRandomToast == 1) "" else ""
                    else -> if (randomRandomToast == 1) "" else ""
                }
                val message: String = when {
                    randomNumberByButton % 2 == 0 || randomNumberByButton == 1 -> random + randomNumberByButton.toString()
                    else -> randomNumberByButton.toString() + random
                }
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                Log.d("RandomNumber", "You got $randomNumberByButton")
                vibeItt()
                play(randomNumberByButton)
                showPics(randomNumberByButton)
            }
            insets
        }
    }

    private fun play(randomNumber: Int) {
        val soundIt = when (randomNumber) {
            1 -> R.raw.dice1
            2 -> R.raw.dice2
            3 -> R.raw.dice3
            4 -> R.raw.dice4
            5 -> R.raw.dice5
            else -> R.raw.dice6
        }

        sound?.release()
        sound = MediaPlayer.create(this, soundIt)
        sound?.start()
    }

    private fun vibeItt() {
        if (vibeIt?.hasVibrator() == true) {
            vibeIt?.vibrate(50)
        }
    }

    private fun showPics(randomNumber: Int) {
        val dicePics: ImageView = findViewById(R.id.dicePics)
        dicePics.setImageResource(resources.getIdentifier("dice$randomNumber", "drawable", packageName))
    }

    @SuppressLint("DiscouragedApi")
    override fun onSensorChanged(sensorEvent: SensorEvent) {
        val x = sensorEvent.values[0]
        val y = sensorEvent.values[1]
        val z = sensorEvent.values[2]

        val currentXValue = "X is: $x"
        val currentYValue = "Y is: $y"
        val currentZValue = "Z is: $z"

        textX = findViewById(R.id.textX)
        textY = findViewById(R.id.textY)
        textZ = findViewById(R.id.textZ)

        textX.text = currentXValue
        textY.text = currentYValue
        textZ.text = currentZValue

        Log.d("currentX", currentXValue)
        Log.d("currentY", currentYValue)
        Log.d("currentZ", currentZValue)

        val timeNow = System.currentTimeMillis()
        val changedX = Math.abs(x - previousX)

        if (changedX > shakingDifference) {
            recordedTimeBeforeShaken = timeNow
            if (!shaken) {
                sound?.release()
                shaken = true
                vibeItt()
                main.setBackgroundColor(0xFF90EE90.toInt())
            }
        }

        if (shaken && (timeNow - recordedTimeBeforeShaken >= nowItsStill) && changedX < 2f) {
            shaken = false
            randomNumberByShaking = (1..6).random()
            vibeItt()
            showPics(randomNumberByShaking)
            play(randomNumberByShaking)
            main.setBackgroundColor(0xFFFFFFFF.toInt())
            val number = randomNumberByShaking.toString()
            Toast.makeText(this, number, Toast.LENGTH_SHORT).show()
        }

        previousX = x
        previousY = y
        previousZ = z
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // No-op
    }
}
