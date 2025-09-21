package meow.softer.yuyuan.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

// At the top level of your kotlin file:
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

data class YuyuanSetting (
    var isSetup: Boolean = false,
    var currentBookId: Int = 1,
    var currentSpeed: Int = 3,
)