package org.kvj.habtproxy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
    }

    private fun scheduleForegroundScan() {
        val workRequest = PeriodicWorkRequestBuilder<ScanWorker>(15, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "foregroundScan",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }

    override fun onResume() {
        super.onResume()
        requestRelevantRuntimePermissions()
        scheduleForegroundScan()
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        private fun updateProxySection(enabled: Boolean) {
            findPreference<Preference>(getString(R.string.settings_optimize_background))?.isEnabled = enabled
            findPreference<Preference>(getString(R.string.settings_webhook))?.isEnabled = enabled
            findPreference<PreferenceCategory>("cat_intervals")?.isEnabled = enabled
        }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)
            findPreference<SwitchPreferenceCompat>(getString(R.string.settings_enabled))?.let {
                it.setOnPreferenceChangeListener { _, newValue ->
                    val enabled = newValue as Boolean
                    updateProxySection(enabled)
                    true
                }
                updateProxySection(it.isChecked)
            }
        }
    }
}
