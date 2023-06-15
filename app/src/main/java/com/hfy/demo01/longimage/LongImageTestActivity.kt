package com.hfy.demo01.longimage

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hfy.demo01.R
import com.hfy.demo01.common.customview.BannerView
import com.hfy.demo01.common.customview.SegmentProgressBar
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener


class LongImageTestActivity : AppCompatActivity() {

    var imageContainer:FrameLayout? = null
    var selectImage:Button? = null

    val TAG = "LongImageTestActivity"

    companion object {
        @JvmStatic
        fun launch(activity: FragmentActivity) {
            Log.i("LongImageTestActivity", "launch: ")
            val intent = Intent(activity, LongImageTestActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_long_image_test)

        imageContainer = findViewById(R.id.fl_image_container)
        selectImage = findViewById(R.id.btn_select_image)

        selectImage?.setOnClickListener {
            PictureSelector.create(this)
                .openSystemGallery(SelectMimeType.ofImage())
                .forSystemResult(object : OnResultCallbackListener<LocalMedia?> {
                    override fun onResult(imageList: ArrayList<LocalMedia?>?) {
                        //0.是否含长图
                        var hasLongImage = false
                        imageList?.forEach {
                            val path = it?.realPath
                            val isLongImage = isLongImage(path)
                            if (isLongImage){
                                hasLongImage = true
                                return@forEach
                            }
                        }
                        Log.i("LongImageTestActivity", "hasLongImage:$hasLongImage ")

                        //1.不含长图，就是横向轮播
                        if (!hasLongImage){
                            addBannerView(imageList)
                            return
                        }

                        //2.含长图，就是竖向拼接，自动上滑
                        addLongImageView(imageList)
                    }
                    override fun onCancel() {}
                })
        }
    }

    /**
     * 长图
     */
    private fun addLongImageView(imageList: ArrayList<LocalMedia?>?) {
        //初始化RecyclerView
        val recyclerView = RecyclerView(this)
        recyclerView.adapter = object :BaseQuickAdapter<LocalMedia?, BaseViewHolder>(R.layout.item_imageview, imageList){
            override fun convert(holder: BaseViewHolder, item: LocalMedia?) {
                val imageView = holder.getView<ImageView>(R.id.iv_image)
                Glide.with(imageView.context).load(item?.realPath).into(imageView)
            }
        }
        imageContainer?.addView(recyclerView, ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT))
    }

    private fun addBannerView(imageList: ArrayList<LocalMedia?>?) {
        //初始化BannerView
        val bannerView = BannerView(this)
        bannerView.setScrollWhenOnePage(false)

        bannerView.bannerAdapter = object: BannerView.BannerAdapter<LocalMedia> {

            override fun getDataList(): MutableList<LocalMedia?>? {
                return imageList
            }

            override fun getBannerItemView(container: ViewGroup, position: Int): View {
                return LayoutInflater.from(container.context).inflate(R.layout.item_banner, container, false)
            }

            override fun bindBannerItemView(position: Int, itemView: View?, itemData: LocalMedia?) {
                val imageView = itemView?.findViewById<ImageView>(R.id.iv_image)
                if (imageView is ImageView) {
                    Glide.with(itemView.context).load(itemData?.realPath).into(imageView)
                }
            }

            override fun onBannerItemClick(position: Int, itemData: LocalMedia?) {

            }

            override fun onBannerItemChange(position: Int, itemData: LocalMedia?) {
            }
        }


        //添加banner
        imageContainer?.addView(bannerView, ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT))

        bannerView.startLoop()
    }

    /**
     * 是长图
     */
    private fun isLongImage(imagePath: String?): Boolean {
        //1、inJustDecodeBounds设为true，并加载图片
        val options = BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);

        //2、是否是长图
        val outWidth = options.outWidth;
        val outHeight = options.outHeight;
        val imageRatio = outHeight / outWidth
        val screenRatio = ScreenUtils.getAppScreenHeight() / ScreenUtils.getAppScreenWidth()
        val isLongImage = imageRatio > screenRatio
        return isLongImage
    }
}