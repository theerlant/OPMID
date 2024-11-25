package org.scahyana.opmid.services

import android.app.Application
import android.content.res.Configuration
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.Locale

class LocaleManager(application: Application): AndroidViewModel(application) {
    val currentLocale = MutableLiveData<String>(SettingsManager.languageSetting)

    var configuration = Configuration(application.resources.configuration)


    init {
        viewModelScope.launch {
            SettingsManager.languageSettingData.observeForever {newLocale ->
                val locale = newLocale ?: "in"
                currentLocale.value = locale

                configuration.setLocale(Locale(locale))
            }
        }
    }

    fun getString(resId: Int) : String {
        val localizedContext = getApplication<Application>().createConfigurationContext(configuration)
        val localizedRes = localizedContext.resources

        return localizedRes.getString(resId)
    }
}