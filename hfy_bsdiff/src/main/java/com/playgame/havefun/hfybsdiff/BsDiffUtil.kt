package com.playgame.havefun.hfybsdiff

import android.widget.Toast
import com.getkeepsafe.relinker.ReLinker

/**
 * 手把手教你在Android中使用bsdiff实现文件增量更新 (超详细)
 * https://blog.csdn.net/yuzhiqiang_1993/article/details/121317195
 */
object BsDiffUtil {


    // Used to load the 'hfybsdiff' library on application startup.
    init {
        System.loadLibrary("hfy_bsdiff")

//        ReLinker.loadLibrary(this, "hfy_bsdiff", object : ReLinker.LoadListener{
//            override fun success() {
//                runOnUiThread {
//                    Toast.makeText(this@BsDiffTestActivity, "loadLibrary success", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun failure(t: Throwable?) {
//                runOnUiThread {
//                    Toast.makeText(this@BsDiffTestActivity, "loadLibrary failure", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//        })
    }

    /**
     * 生成补丁包
     * @param newFilePath String 新文件的地址
     * @param oldFilePath String  旧文件的地址
     * @param patchFilePath String  生成的补丁文件地址
     * @return Int
     */
    external fun diff(newFilePath: String, oldFilePath: String, patchFilePath: String): Int
    /**
     * 合并差分包
     * @param oldFilePath String 旧文件地址
     * @param patchFilePath String 补丁文件地址
     * @param combineFilePath String 合并后的新文件地址
     * @return Int
     */
    external fun patch(oldFilePath: String, patchFilePath: String, combineFilePath: String): Int
}