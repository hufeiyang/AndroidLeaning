package com.hfy.demo01.fileselector

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.CallSuper

/**
 * 选择文件的协定
 * Input type  : Unit? 不需要传值
 * Output type : Uri?  选择完成后的 image uri
 */
class SelectFileContract  : ActivityResultContract<Unit?, Uri?>() {

    private val TAG: String = "SelectPhotoContract"

    @CallSuper
    override fun createIntent(context: Context, input: Unit?): Intent {
        return Intent(Intent.ACTION_PICK).setType("*/*")
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        Log.d(TAG, "pick photo result: ${intent?.data}")
        return intent?.data
    }
}