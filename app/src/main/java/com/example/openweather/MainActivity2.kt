package com.example.openweather

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity2 : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    lateinit var homeButton: Button
    lateinit var firstNameField: EditText
    lateinit var lastNameField: EditText
    lateinit var emailField: EditText
    lateinit var passwordField: EditText
    lateinit var ageField: EditText
    lateinit var subscribeBox: CheckBox
    lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)

        // Initialize Firestore
        db = FirebaseFirestore.getInstance()

        homeButton = findViewById(R.id.button)

        firstNameField = findViewById(R.id.editTextTextName)
        lastNameField = findViewById(R.id.editTextTextLastName)
        emailField = findViewById(R.id.editTextTextEmail)
        passwordField = findViewById(R.id.editTextTextPassword)
        ageField = findViewById(R.id.editTextTextAge)
        subscribeBox = findViewById(R.id.checkBox)
        registerButton = findViewById(R.id.RegistrateButton)

        homeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        registerButton.setOnClickListener {
            val firstName = firstNameField.text.toString()
            val lastName = lastNameField.text.toString()
            val email = emailField.text.toString()
            val password = passwordField.text.toString()
            val age = ageField.text.toString()
            var ageee:Int = age.toInt()
            val subscribe = subscribeBox.isChecked

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || age.isEmpty()) {
                Toast.makeText(this, "Registration fails. Fill in all the fields", Toast.LENGTH_SHORT).show()
            } else if (!validateEmail(email)) {
                Toast.makeText(this, "Invalid email address.", Toast.LENGTH_SHORT).show()
            } else if (!validateAge(ageee)) {
                Toast.makeText(this, "You have to be at least 18", Toast.LENGTH_SHORT).show()

            }
            else if (!validatePassword(password)) {
                Toast.makeText(this, "Password must contain at least one number.", Toast.LENGTH_SHORT).show()
            } else { write(firstName, lastName, email, password, subscribe)
                Toast.makeText(this, "Registered", Toast.LENGTH_SHORT).show()
                emptyRegistration()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun validateEmail(emaill: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(emaill).matches()
    }

    private fun validateAge(age: Int): Boolean {
        return if (age < 18){
            false
        } else {
            true
        }
    }

    //helping to authenticate password chosen
    private fun validatePassword(passwordd: String): Boolean {
        return passwordd.matches(".*\\d.*".toRegex())
    }

    private fun emptyRegistration() {
        firstNameField.text.clear()
        lastNameField.text.clear()
        emailField.text.clear()
        passwordField.text.clear()
        ageField.text.clear()
        subscribeBox.isChecked = false
    }

    private fun write(firstName: String, lastName: String, email: String, password: String, subscribe: Boolean) {
        // Initialize Firestore instance
        // Create a new user with a HashMap
        val user = HashMap<String, Any>()
        user["first"] = firstName
        user["last"] = lastName
        user["email"] = email
        user["password"] = password
        user["subscription"] = subscribe


        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d("MainActivity2", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("MainActivity2", "Error adding document", e)
            }
    }


}
