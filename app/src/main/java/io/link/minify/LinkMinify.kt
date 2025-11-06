package io.link.minify

import android.app.Application
import io.link.minify.di.dataDi
import io.link.minify.di.domainDi
import io.link.minify.di.uiDi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class LinkMinify : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@LinkMinify)
            modules(
                uiDi,
                domainDi,
                dataDi,
            )
        }
    }
}
