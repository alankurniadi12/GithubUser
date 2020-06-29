package com.example.githubuserfinalbfaa.setting

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.githubuserfinalbfaa.R
import com.example.githubuserfinalbfaa.setting.alarm.AlarmReceiver
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity: AppCompatActivity() {

    lateinit var alarmReceiver: AlarmReceiver
    companion object {
        const val SHARED_PREFERENCE = "sharedpreference"
        const val BOOLEAN_KEY = "booleankey"
        internal val TAG = SettingActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        supportActionBar?.title = "Setting"

        alarmReceiver = AlarmReceiver()

        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE)

        val getBoolean = sharedPreferences.getBoolean(BOOLEAN_KEY, false)
        cb_reminder.isChecked = getBoolean

        cb_reminder.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val editor = sharedPreferences.edit()
                editor.apply {
                    putBoolean(BOOLEAN_KEY, true)
                }.apply()

                alarmReceiver.setRepeatAlarm(this, AlarmReceiver.EXTRA_TYPE, "09:00")
                Log.d(TAG, "Alarm On")
            } else {
                val editor = sharedPreferences.edit()
                editor.apply {
                    putBoolean(BOOLEAN_KEY, false)
                }.apply()

                alarmReceiver.cancelAlarm(this, AlarmReceiver.TYPE_ALARM_REPEATING)
                Log.d(TAG, "Alarm Off")
            }

        }
    }
}


