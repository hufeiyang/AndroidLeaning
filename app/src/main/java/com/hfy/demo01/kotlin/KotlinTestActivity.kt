package com.hfy.demo01.kotlin

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hfy.demo01.R

/**
 * kotlin学习
 */
class KotlinTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_test)

        
    }

    companion object {
        @JvmStatic
        fun launch(activity: Activity) {

        }
    }
}