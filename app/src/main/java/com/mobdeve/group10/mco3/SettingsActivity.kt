package com.mobdeve.group10.mco3

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    // Shared preferences to store user preferences
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewSwitch: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Initialize shared preferences
        sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE)

        // Initialize the switch
        viewSwitch = findViewById(R.id.viewSwitch)

        // Load user preferences and update UI
        loadPreferences()

        // Set listener for the switch
        viewSwitch.setOnCheckedChangeListener { _, isChecked ->
            // Save user preference when switch state changes
            savePreference(isChecked)
        }
    }

    // Function to load user preferences
    private fun loadPreferences() {
        // Retrieve saved preference for the switch
        val isSwitchChecked = sharedPreferences.getBoolean("isSwitchChecked", true)

        // Update switch state based on loaded preference
        viewSwitch.isChecked = isSwitchChecked
    }

    // Function to save user preference
    private fun savePreference(isChecked: Boolean) {
        // Get editor for shared preferences
        val editor = sharedPreferences.edit()

        // Save switch state to shared preferences
        editor.putBoolean("isSwitchChecked", isChecked)

        // Apply changes
        editor.apply()
    }

    // Save settings when activity is paused or destroyed
    private fun saveSettings() {
        savePreference(viewSwitch.isChecked)
    }

    override fun onDestroy() {
        super.onDestroy()
        saveSettings()
    }

    override fun onPause() {
        super.onPause()
        saveSettings()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Set result to indicate that settings have been changed
        val resultIntent = Intent()
        setResult(RESULT_OK, resultIntent)
    }
}
