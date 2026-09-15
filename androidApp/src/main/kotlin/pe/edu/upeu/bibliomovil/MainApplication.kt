package pe.edu.upeu.bibliomobil

import android.app.Application

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin { androidContext(this@MainApplication) }
    }
}
