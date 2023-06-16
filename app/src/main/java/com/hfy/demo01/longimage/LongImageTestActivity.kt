package com.hfy.demo01.longimage

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapRegionDecoder
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ScreenUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hfy.demo01.R
import com.hfy.demo01.common.customview.BannerView
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import java.io.FileInputStream


class LongImageTestActivity : AppCompatActivity() {

    private val mRect: Rect = Rect()

    var imageContainer: FrameLayout? = null
    var selectImage: Button? = null

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
                            val isLongImage = isLongImage(path, 2.5f)
                            if (isLongImage) {
                                hasLongImage = true
                                return@forEach
                            }
                        }
                        Log.i("LongImageTestActivity", "hasLongImage:$hasLongImage ")

                        //1.不含长图，就是横向轮播
                        if (!hasLongImage) {
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

        if (imageContainer == null) {
            return
        }

        //1.准备分段后的图片列表
        val finalImageList = mutableListOf<Bitmap>()

        imageList?.forEach {
            //分段，先分段，在插入
            imageContainer?.let { it1 ->
                splitLongImage(finalImageList, it1, it?.realPath!!)
            }
        }

        //初始化RecyclerView
        val recyclerView = RecyclerView(this)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = object :
            BaseQuickAdapter<Bitmap, BaseViewHolder>(R.layout.item_imageview, finalImageList) {
            override fun convert(holder: BaseViewHolder, item: Bitmap) {
                val imageView = holder.getView<ImageView>(R.id.iv_image)
                imageView.layoutParams = imageView.layoutParams.also {
                    it.width = imageContainer!!.measuredWidth
                    it.height = (imageContainer!!.measuredWidth * (item.height.toFloat()/item.width.toFloat()) ).toInt()
                }
                imageView.setImageBitmap(item)
            }
        }
        imageContainer?.addView(recyclerView, ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT))
    }

    /**
     * 这里是先把长图分好块了，还是占用了内容，todo 使用时才获取
     */
    private fun splitLongImage(finalImageList:MutableList<Bitmap>, imageContainer: FrameLayout, imagePath: String) {

        val imageContainerHeight = imageContainer.measuredHeight
        val imageContainerWidth = imageContainer.measuredWidth

        val screenRatio = ScreenUtils.getAppScreenHeight().toFloat() / ScreenUtils.getAppScreenWidth().toFloat()
        if (isLongImage(imagePath, screenRatio)) {

            val tmpOptions = BitmapFactory.Options()
            tmpOptions.inJustDecodeBounds = true
            BitmapFactory.decodeFile(imagePath, tmpOptions)
            val longBitmapWidth = tmpOptions.outWidth
            val longBitmapHeight = tmpOptions.outHeight

            val radioContainerHeight = (imageContainerHeight * (longBitmapWidth.toFloat() / imageContainerWidth.toFloat())).toInt()

            var itemCount = longBitmapHeight / radioContainerHeight
            val lastItemHeight = longBitmapHeight % radioContainerHeight
            if (lastItemHeight > 0) {
                itemCount += 1
            }

            mRect.left = 0
            mRect.right = longBitmapWidth
            mRect.top = 0
            mRect.bottom = 0

            for (i in 0 until itemCount) {
                mRect.top = radioContainerHeight * i
                if (i == itemCount - 1 ){
                    mRect.bottom = longBitmapHeight
                }else{
                    mRect.bottom = radioContainerHeight * (i + 1)
                }

                val bitmapRegionDecoder = BitmapRegionDecoder.newInstance(FileInputStream(imagePath), false);
                val options = BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565
                val rectBitmap = bitmapRegionDecoder.decodeRegion(mRect, options)
                finalImageList.add(rectBitmap)
            }
        }else{
            finalImageList.add(BitmapFactory.decodeFile(imagePath))
        }
    }

    private fun addBannerView(imageList: ArrayList<LocalMedia?>?) {
        //初始化BannerView
        val bannerView = BannerView(this)

        bannerView.bannerAdapter = object : BannerView.BannerAdapter<LocalMedia> {

            override fun getDataList(): MutableList<LocalMedia?>? {
                return imageList
            }

            override fun getBannerItemView(container: ViewGroup, position: Int): View {
                return LayoutInflater.from(container.context)
                    .inflate(R.layout.item_banner, container, false)
            }

            override fun bindBannerItemView(position: Int, itemView: View?, itemData: LocalMedia?) {
                val imageView = itemView?.findViewById<ImageView>(R.id.iv_image)
                if (imageView is ImageView) {
                    Glide.with(itemView.context).load(itemData?.realPath).addListener(object :
                        RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            imageView.post {
                                val viewRadio =
                                    imageView.measuredHeight.toFloat() / imageView.measuredWidth.toFloat()

                                Log.i(
                                    TAG,
                                    "onResourceReady: imageView layoutParams.height=${imageView.measuredHeight.toFloat()}, layoutParams.width=${imageView.measuredWidth.toFloat()} "
                                )

                                resource?.let {
                                    val resourceRadio =
                                        resource.intrinsicHeight.toFloat() / resource.intrinsicWidth.toFloat()

                                    Log.i(
                                        TAG,
                                        "onResourceReady: viewRadio=$viewRadio, resourceRadio=$resourceRadio, "
                                    )

                                    var scaleType = ImageView.ScaleType.FIT_CENTER
                                    //图的长宽比 大于 view长宽比
                                    if (resourceRadio > viewRadio) {
                                        scaleType = ImageView.ScaleType.CENTER_CROP
                                    }
                                    imageView.scaleType = scaleType
                                    imageView.setImageDrawable(resource)
                                }
                            }

                            return false
                        }
                    }).submit()
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

//    /**
//     * 是长图
//     */
//    private fun isLongImage(bitmap: Bitmap?): Boolean {
//        var isLongImage = false
//        bitmap?.let {
//            val height = bitmap.height.toFloat()
//            val width = bitmap.width.toFloat()
//
//            val imageRatio = height / width
////            val screenRatio = ScreenUtils.getAppScreenHeight() / ScreenUtils.getAppScreenWidth()
//            isLongImage = imageRatio > 2.5f
//        }
//
//        return isLongImage
//    }

    /**
     * 是长图
     */
    private fun isLongImage(imagePath: String?, radio: Float): Boolean {
        //1、inJustDecodeBounds设为true，并加载图片
        val options = BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);

        //2、是否是长图
        val outWidth = options.outWidth;
        val outHeight = options.outHeight;
        val imageRatio = outHeight / outWidth
        val isLongImage = imageRatio > radio
        return isLongImage
    }
}