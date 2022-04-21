package com.example.chat_2022_eleves

import android.os.Bundle

/*
import android.support.v7.preference.PreferenceFragmentCompat

class SettingsActivity : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, p1: String?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)
    }
}
 */

import android.preference.PreferenceActivity

class SettingsActivity : PreferenceActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)
    }
}