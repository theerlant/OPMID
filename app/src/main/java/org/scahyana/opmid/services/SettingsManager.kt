package org.scahyana.opmid.services

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

enum class SettingsKey {
    THEME,
    LANGUAGE,
    SOUND_ON,
    FEEDBACK_ON
}

enum class ThemeState {
    SYSTEM_DEFAULT,
    LIGHT,
    DARK
}

object SettingsManager {
    private const val PREFS_NAME = "app_settings"

    private lateinit var sharedPreferences: SharedPreferences
    private val _themeSettingData = MutableLiveData<Int>(ThemeState.SYSTEM_DEFAULT.ordinal)
    private val _languageSettingData = MutableLiveData<String>("in")
    private val _soundSettingData = MutableLiveData<Boolean>(true)
    private val _feedbackSettingData = MutableLiveData<Boolean>(true)

    val themeSettingData: LiveData<Int> = _themeSettingData
    val languageSettingData: LiveData<String?> = _languageSettingData
    val soundSettingData: LiveData<Boolean> = _soundSettingData
    val feedbackSettingData: LiveData<Boolean> = _feedbackSettingData

    fun initialize(context: Context) {
        // Get the sharedPreferences
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Create a sharedPreferences listener to react on changes
        sharedPreferences.registerOnSharedPreferenceChangeListener { sharedPrefs, key ->
            when (key) {
                SettingsKey.THEME.name ->
                    _themeSettingData.value = sharedPrefs.getInt(SettingsKey.THEME.name, ThemeState.SYSTEM_DEFAULT.ordinal)
                SettingsKey.LANGUAGE.name ->
                    _languageSettingData.value = sharedPrefs.getString(SettingsKey.LANGUAGE.name, "in")
                SettingsKey.SOUND_ON.name ->
                    _soundSettingData.value = sharedPrefs.getBoolean(SettingsKey.SOUND_ON.name, true)
                SettingsKey.FEEDBACK_ON.name ->
                    _feedbackSettingData.value = sharedPrefs.getBoolean(SettingsKey.FEEDBACK_ON.name, true)
            }
        }

        // Set default value or get from system
        _themeSettingData.value = sharedPreferences.getInt(SettingsKey.THEME.name, ThemeState.SYSTEM_DEFAULT.ordinal)
        _languageSettingData.value = sharedPreferences.getString(SettingsKey.LANGUAGE.name, null)
        _soundSettingData.value = sharedPreferences.getBoolean(SettingsKey.SOUND_ON.name, true)
        _feedbackSettingData.value = sharedPreferences.getBoolean(SettingsKey.FEEDBACK_ON.name, true)
    }

    var themeSetting: Int
        get() = sharedPreferences.getInt(SettingsKey.THEME.name, ThemeState.SYSTEM_DEFAULT.ordinal)
        set(value) {
            sharedPreferences.edit().putInt(SettingsKey.THEME.name, value).apply()
            _themeSettingData.value = value
        }

    var languageSetting: String?
        get() = sharedPreferences.getString(SettingsKey.LANGUAGE.name, "in")
        set(value) {
            sharedPreferences.edit().putString(SettingsKey.LANGUAGE.name, value).apply()
           _languageSettingData.value = value
        }

    var soundSetting: Boolean
        get() = sharedPreferences.getBoolean(SettingsKey.SOUND_ON.name, true)
        set(value) {
            sharedPreferences.edit().putBoolean(SettingsKey.SOUND_ON.name, value).apply()
            _soundSettingData.value = value
        }

    var feedbackSetting: Boolean
        get() = sharedPreferences.getBoolean(SettingsKey.FEEDBACK_ON.name, true)
        set(value) {
            sharedPreferences.edit().putBoolean(SettingsKey.FEEDBACK_ON.name, value).apply()
            _feedbackSettingData.value = value
        }
}