package com.crash.alpaca.setting

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.crash.alpaca.R

class SettingFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting, rootKey)
    }
}