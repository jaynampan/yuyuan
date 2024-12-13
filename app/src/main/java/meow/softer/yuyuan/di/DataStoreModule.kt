package meow.softer.yuyuan.di

import android.content.Context
import androidx.datastore.core.DataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import meow.softer.yuyuan.YuyuanSetting
import meow.softer.yuyuan.data.local.datastore.yuyuanSettingDataStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Singleton
    @Provides
    fun provideYuyuanSettingDataStore(@ApplicationContext context: Context): DataStore<YuyuanSetting> {
        return context.yuyuanSettingDataStore
    }
}