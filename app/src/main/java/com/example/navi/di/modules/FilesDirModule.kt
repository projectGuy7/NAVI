package com.example.navi.di.modules

import android.app.Application
import com.example.navi.di.qualifiers.FilesDir
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.dgis.sdk.Context
import java.io.File

@Module
@InstallIn(SingletonComponent::class)
class FilesDirModule {

    @FilesDir
    @Provides
    fun providesFilesDir(application: Application): File {
        return application.filesDir;
    }

}