package com.hfy.demo01.module.home.notification

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.hfy.demo01.R
import com.hfy.secondfloor.SecondFloorTest1Activity
import com.hfy.secondfloor.util.StatusBarUtil

class AppInnerNotificationActivity : AppCompatActivity() {

    companion object {
        fun launch(activity: Activity) {
            val intent = Intent(activity, AppInnerNotificationActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_inner_notification)

//        StatusBarUtil.immersive(this)

        findViewById<Button>(R.id.btnShowNotification).setOnClickListener {
            AppInnerNotification.Builder(this@AppInnerNotificationActivity)
                .setTitle("title")
                .setContent("content")
                .setIconUrl("http://p3-momoyu-sign.byteimg.com/tos-cn-i-1jbyxo7mmq/4d2ddd4d651a4cd8977afde18b4ef716~tplv-1jbyxo7mmq-obj.webp?x-expires=4837299174&x-signature=L9xGMog38%2BVmntV%2FAlJavLx5ZmE%3D")
                .setButtonText("去领取")
                .setOpenUrl("RouterMap.ME.ACTIVITY_ME_MSG_CENTER")
//                .setDark(true)
                .build()
                .show()
        }

        findViewById<Button>(R.id.btnShowNotificationDark).setOnClickListener {
            AppInnerNotification.Builder(this@AppInnerNotificationActivity)
                .setTitle("dark")
                .setContent("dark_content")
                .setIconUrl("http://p3-momoyu-sign.byteimg.com/tos-cn-i-1jbyxo7mmq/4d2ddd4d651a4cd8977afde18b4ef716~tplv-1jbyxo7mmq-obj.webp?x-expires=4837299174&x-signature=L9xGMog38%2BVmntV%2FAlJavLx5ZmE%3D")
                .setButtonText("去领取")
                .setOpenUrl("RouterMap.ME.ACTIVITY_ME_MSG_CENTER")
                .setDark(true)
                .build()
                .show()
        }

        findViewById<Button>(R.id.btnShowNotification_no_icon).setOnClickListener {
            AppInnerNotification.Builder(this@AppInnerNotificationActivity)
                .setTitle("dark")
                .setContent("dark_content")
//                .setIconUrl("http://p3-momoyu-sign.byteimg.com/tos-cn-i-1jbyxo7mmq/4d2ddd4d651a4cd8977afde18b4ef716~tplv-1jbyxo7mmq-obj.webp?x-expires=4837299174&x-signature=L9xGMog38%2BVmntV%2FAlJavLx5ZmE%3D")
                .setButtonText("去领取")
                .setOpenUrl("RouterMap.ME.ACTIVITY_ME_MSG_CENTER")
//                .setDark(true)
                .build()
                .show()
        }

        findViewById<Button>(R.id.btnShowNotification_dark_no_icon).setOnClickListener {
            AppInnerNotification.Builder(this@AppInnerNotificationActivity)
                .setTitle("dark")
                .setContent("dark_content")
//                .setIconUrl("http://p3-momoyu-sign.byteimg.com/tos-cn-i-1jbyxo7mmq/4d2ddd4d651a4cd8977afde18b4ef716~tplv-1jbyxo7mmq-obj.webp?x-expires=4837299174&x-signature=L9xGMog38%2BVmntV%2FAlJavLx5ZmE%3D")
                .setButtonText("去领取")
                .setOpenUrl("RouterMap.ME.ACTIVITY_ME_MSG_CENTER")
                .setDark(true)
                .build()
                .show()
        }

        findViewById<Button>(R.id.btnShowNotification_no_open_url).setOnClickListener {
            AppInnerNotification.Builder(this@AppInnerNotificationActivity)
                .setTitle("dark")
                .setContent("dark_content")
                .setIconUrl("http://p3-momoyu-sign.byteimg.com/tos-cn-i-1jbyxo7mmq/4d2ddd4d651a4cd8977afde18b4ef716~tplv-1jbyxo7mmq-obj.webp?x-expires=4837299174&x-signature=L9xGMog38%2BVmntV%2FAlJavLx5ZmE%3D")
                .setButtonText("去领取")
//                .setOpenUrl(RouterMap.ME.ACTIVITY_ME_MSG_CENTER)
//                .setDark(true)
                .build()
                .show()
        }

        findViewById<Button>(R.id.btnShowNotification_dark_no_open_url).setOnClickListener {
            AppInnerNotification.Builder(this@AppInnerNotificationActivity)
                .setTitle("dark")
                .setContent("dark_content")
                .setIconUrl("http://p3-momoyu-sign.byteimg.com/tos-cn-i-1jbyxo7mmq/4d2ddd4d651a4cd8977afde18b4ef716~tplv-1jbyxo7mmq-obj.webp?x-expires=4837299174&x-signature=L9xGMog38%2BVmntV%2FAlJavLx5ZmE%3D")
                .setButtonText("去领取")
//                .setOpenUrl(RouterMap.ME.ACTIVITY_ME_MSG_CENTER)
                .setDark(true)
                .build()
                .show()
        }
    }
}