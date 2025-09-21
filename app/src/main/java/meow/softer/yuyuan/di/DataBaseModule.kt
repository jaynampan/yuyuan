package meow.softer.yuyuan.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import meow.softer.yuyuan.data.local.YuyuanDatabase
import meow.softer.yuyuan.data.local.dao.BookDao
import meow.softer.yuyuan.data.local.dao.SentenceDao
import meow.softer.yuyuan.data.local.dao.WordDao
import meow.softer.yuyuan.data.local.dao.WordStatusDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): YuyuanDatabase {
        return YuyuanDatabase.getDatabase(context)
    }


    @Provides
    fun provideBookDao(appDatabase: YuyuanDatabase): BookDao {
        return appDatabase.bookDao()
    }

    @Provides
    fun provideWordDao(appDatabase: YuyuanDatabase): WordDao {
        return appDatabase.wordDao()
    }

    @Provides
    fun provideWordStatusDao(appDatabase: YuyuanDatabase):WordStatusDao{
        return appDatabase.wordStatusDao()
    }

    @Provides
    fun provideSentenceDao(appDatabase: YuyuanDatabase):SentenceDao{
        return appDatabase.sentenceDao()
    }
}