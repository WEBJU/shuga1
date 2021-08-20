package com.usalama.usalamatechnology.shuga;

import android.app.Application
import com.usalama.usalamatechnology.shuga.activity.SplashActivity
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump

open class MainActivity : Application() {

    override fun onCreate() {
        super.onCreate()
        app = SplashActivity()

        ViewPump.init(
            ViewPump.builder().addInterceptor(
                CalligraphyInterceptor(
                    CalligraphyConfig.Builder()
                        .setDefaultFontPath(getString(R.string.da_font_regular))
                        .setFontAttrId(R.attr.fontPath).build()
                )
            ).build()
        )
    }

    companion object {

        lateinit var app: SplashActivity

        fun getAppInstance(): SplashActivity {
            return app
        }
    }
}