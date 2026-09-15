package pe.edu.upeu.bibliomovil

import android.app.Application

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin { androidContext(this@MainApplication) }
    }
}