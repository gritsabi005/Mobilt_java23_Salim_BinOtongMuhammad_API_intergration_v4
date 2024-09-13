package com.example.openweather

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    lateinit var logInButton: Button
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var registerButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        logInButton = findViewById(R.id.logInButton)
        email = findViewById(R.id.editTextTextLogInAdress)
        password = findViewById(R.id.editTextText2LogInPassword)
        registerButton = findViewById(R.id.registrationButton)

        // Initialize Firestore
        db = FirebaseFirestore.getInstance()

        logInButton.setOnClickListener({

            var userEmail : String = email.getText().toString()
            var userPassword : String = password.getText().toString()
            if (userEmail == "s" && userPassword == "a") {

                var i = Intent(this, MainActivity3::class.java)
                startActivity(i)
            } else {
                getUser(userEmail,userPassword)
            }
        })
        registerButton.setOnClickListener({
            var i2 = Intent(this, MainActivity2::class.java)
            startActivity(i2)
        })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun getUser(email: String, password: String) {
        db.collection("users").whereEqualTo("email", email).get()
            .addOnSuccessListener { document: QuerySnapshot ->
                if (document != null && !document.isEmpty) {
                    Log.d("success", "Success to database")
                    for (everyEmail in document){
                        val theUser = everyEmail.data
                        Log.d(TAG, "DocumentSnapshot data: $theUser")

                        if(theUser["password"] == password){
                            var i = Intent(this, MainActivity3::class.java)
                            startActivity(i)
                            Toast.makeText(this, "Welcome!", Toast.LENGTH_SHORT).show()
                            Log.d(TAG, "User is welcomed")
                        } else {
                            Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show()
                            Log.d(TAG, "User is not found")
                        }
                    }
                } else {
                    Toast.makeText(this, "No user is found. Register today!", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "User is not found")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }

    }

}