package com.hfy.demo01.bsdiff

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.*
import com.hfy.demo01.R
import com.hfy.demo01.fileselector.SelectFileContract
//import com.playgame.havefun.hfybsdiff.BsDiffUtil
import java.io.File

class BsDiffTestActivity : AppCompatActivity() {

    private val TAG: String = "BsDiffTestActivity"
    private val suffix = "apk"
    private val oldApkFile = File(PathUtils.getExternalAppFilesPath(), "v_release_0203-1618_MuBao_monkey_opt_hfy_1.39.40_25778_final.apk")
    private val newApkFile = File(PathUtils.getExternalAppFilesPath(), "v_release_0210-1213_MuBao_monkey_1.39.50_25888_dcc_final.apk")
    private val patchFile = File(PathUtils.getExternalAppFilesPath(), "patch.${suffix}")
    private val combineFile = File(PathUtils.getExternalAppFilesPath(), "combine.${suffix}")

    val uiHandler: Handler = Handler(Looper.getMainLooper())

    companion object {
        @JvmStatic
        fun launch(activity: FragmentActivity) {
            Log.i("BsDiffTestActivity", "launch: ")
            val intent = Intent(activity, BsDiffTestActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bs_diff_test)

        val launcher1 = registerForActivityResult(SelectFileContract()) { uri: Uri? ->
                if (uri != null) {
                    // 返回的选择的图片uri
                    findViewById<TextView>(R.id.tv_old_apk_path).let {
//                        oldAPkPath = uri.path.toString()
//                        it.text = it.text.toString().plus(oldAPkPath)
                        LogUtils.i(TAG,"oldAPkPath:${oldApkFile.absolutePath}")
                    }
                } else {
                    Toast.makeText(this, "您没有选择任何文件", Toast.LENGTH_SHORT).show()
                }
            }

        val launcher2 = registerForActivityResult(SelectFileContract()) { uri: Uri? ->
            if (uri != null) {
                // 返回的选择的图片uri
                findViewById<TextView>(R.id.tv_new_apk_path).let {
//                    newAPkPath = uri.path.toString()
//                    it.text = it.text.toString().plus(uri.path)
                    LogUtils.i(TAG,"newAPkPath:${newApkFile.absolutePath}")
                }
            } else {
                Toast.makeText(this, "您没有选择任何文件", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.btn_get_old_apk).setOnClickListener {
            launcher1.launch(null)
       }


        findViewById<Button>(R.id.btn_get_new_apk).setOnClickListener {
            launcher2.launch(null)
        }


        findViewById<Button>(R.id.btn_bsdiff).setOnClickListener {
            //权限申请
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                if (PermissionUtils.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    bsdiffForPatch()
                } else {
                    PermissionUtils.permission(PermissionConstants.STORAGE).callback(object:PermissionUtils.SimpleCallback{
                        override fun onGranted() {
                            bsdiffForPatch()
                        }

                        override fun onDenied() {
                            ToastUtils.showShort("写权限被拒绝了！")
                        }
                    }).request()
                }
            }

        }

        findViewById<Button>(R.id.btn_bspatch).setOnClickListener {
//            oldAPkPath?.let { it1 ->
                patchFile.exists().let {
                    Thread{
//                        val patch = BigNews.make(oldApkFile.absolutePath, combineFile.absolutePath, patchFile.absolutePath)
//                        val patch = BsDiffUtil.patch(oldApkFile.absolutePath, patchFile.absolutePath, combineFile.absolutePath)
//                        LogUtils.i(TAG,"patch result = $patch")
                        uiHandler.post{
                            findViewById<TextView>(R.id.tv_combine_apk_path).text = combineFile.absolutePath
                            LogUtils.i(TAG,"newFile MD5:${FileUtils.getFileMD5ToString(newApkFile)}")
                            LogUtils.i(TAG,"combineFile MD5:${FileUtils.getFileMD5ToString(combineFile)}")
                        }
                    }.start()

                }
//            }
        }

        findViewById<Button>(R.id.btn_install_combine_apk).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            val uri = FileProvider.getUriForFile(this, this.packageName.plus(".fileprovider"), combineFile);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            startActivity(intent);
        }

    }

    private fun bsdiffForPatch() {
        LogUtils.i(TAG, "now is diffing. oldAPkPath:${oldApkFile.absolutePath}，newAPkPath:${newApkFile.absolutePath}")

//        oldAPkPath.let { it1 ->
//            newAPkPath.let { it2 ->
                Thread {
                    val patchFilePath = patchFile.absolutePath
                    LogUtils.i(TAG, "patch begin. dest path:$patchFilePath")
//                    val diff = BigNews.diff(oldApkFile.absolutePath, newApkFile.absolutePath, patchFilePath)
//                    val diff = BsDiffUtil.diff(newApkFile.absolutePath,  oldApkFile.absolutePath, patchFilePath)
//                    LogUtils.i(TAG, "diff end. result = $diff")
                    uiHandler.post {
                        findViewById<TextView>(R.id.tv_patch_path).text = patchFile.absolutePath
                    }
                }.start()

//            }
//        }
    }


}