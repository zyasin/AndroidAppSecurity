package com.zyasin.sharedprefssecurity

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey


class MainActivity : AppCompatActivity() {


    private lateinit var keyEditText: EditText
    private lateinit var valueEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var retrieveButton: Button
    private lateinit var resultTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up the Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Set the title
        supportActionBar?.title = "  SharedPreferences Data Security"


        keyEditText = findViewById(R.id.keyEditText)
        valueEditText = findViewById(R.id.valueEditText)
        saveButton = findViewById(R.id.saveButton)
        retrieveButton = findViewById(R.id.retrieveButton)
        resultTextView = findViewById(R.id.resultTextView)

        saveButton.setOnClickListener { saveData() }
        retrieveButton.setOnClickListener { retrieveData() }
    }

    private fun getEncryptedSharedPreferences(): SharedPreferences {
        val masterKey = MasterKey.Builder(applicationContext)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            applicationContext,
            "encrypted_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private fun saveData() {
        val key = keyEditText.text.toString()
        val value = valueEditText.text.toString()

        if (key.isNotEmpty() && value.isNotEmpty()) {
            val sharedPreferences = getEncryptedSharedPreferences()
            val editor = sharedPreferences.edit()
            editor.putString(key, value)
            editor.apply()
            resultTextView.text = "Data saved successfully!"
        } else {
            resultTextView.text = "Please enter both key and value."
        }
    }

    private fun retrieveData() {
        val key = keyEditText.text.toString()

        if (key.isNotEmpty()) {
            val sharedPreferences = getEncryptedSharedPreferences()
            val value = sharedPreferences.getString(key, "Key not found")
            resultTextView.text = "Retrieved value: $value"
        } else {
            resultTextView.text = "Please enter the key to retrieve data."
        }
    }
}

