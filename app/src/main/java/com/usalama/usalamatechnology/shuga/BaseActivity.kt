package com.usalama.usalamatechnology.shuga

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.usalama.usalamatechnology.shuga.utils.color
import com.usalama.usalamatechnology.shuga.utils.lightStatusBar
import io.github.inflationx.viewpump.ViewPumpContextWrapper

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lightStatusBar(color(R.color.white))
    }
/*
     fun setToolBar(s: String) {
        ivBack.onClick {
            onBackPressed()
        }
        toolBarTitle.text=s
    }
*/

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }
}