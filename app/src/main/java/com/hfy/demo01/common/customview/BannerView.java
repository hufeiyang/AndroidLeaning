package com.hfy.demo01.common.customview;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenjianjiang
 * email:  chenjianjiang@bytedance.com
 * date:   2021/5/17
 */
public class BannerView extends FrameLayout {

    private static final String TAG = "BannerView";

    private static final long DEFAULT_LOOP_INTERVAL = 2000L;

    private ViewPager mViewPager;    private PagerScroller mPagerScroller;

    private SegmentProgressBar mSegmentProgressBar;

    private BannerPagerAdapter mBannerPagerAdapter;
    private BannerAdapter mBannerAdapter;

    // 指示器位置，可设置左中右，但统一靠底部
    private int mIndicatorGravity = Gravity.CENTER;
    // 指示器底部间距
    private int mIndicatorBottomMargin = 20;
    // 指示器水平间距，距左或右，与mIndicatorGravity有关
    private int mIndicatorHorizontalMargin = 20;
    private int mCurrentItem;


    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener;


    public BannerView(@NonNull Context context) {
        this(context, null);
    }

    public BannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public BannerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        initViewPager();
        addView(mViewPager, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //初始化分段进度条
        mSegmentProgressBar = new SegmentProgressBar(getContext());
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = mIndicatorGravity | Gravity.BOTTOM;
        layoutParams.bottomMargin = mIndicatorBottomMargin;
        layoutParams.leftMargin = mIndicatorHorizontalMargin;
        layoutParams.rightMargin = mIndicatorHorizontalMargin;
        mSegmentProgressBar.setLayoutParams(layoutParams);
        addView(mSegmentProgressBar);
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        ViewPager viewPager = new NestedViewPager(getContext());
        try {
            Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            mPagerScroller = new PagerScroller(getContext(), PagerScroller.sInterpolator);
            scrollerField.set(viewPager, mPagerScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 切换指示器标示
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            boolean isPositionChanged = false;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (mBannerPagerAdapter != null) {

                    mCurrentItem = position;
                    if (mBannerAdapter != null && !isPositionChanged) {
                        mBannerAdapter.onBannerItemChange(position, mBannerPagerAdapter.mData.get(position));
                        //如果手动切换viewPager，则改变进度条
                        mSegmentProgressBar.setCurrentSelectedSegment(position);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    isPositionChanged = false;
                } else if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    mSegmentProgressBar.closeAnimationMode();
                }
            }
        });
        mViewPager = viewPager;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPagerScroller.setPageWidth(mViewPager.getMeasuredWidth());
    }

    @Override
    protected void onDetachedFromWindow() {
        removeOnGlobalLayoutListener();
        super.onDetachedFromWindow();
    }
    public void setCurrentItem(int i, boolean smoothScroll) {
        mCurrentItem = i;
        mViewPager.setCurrentItem(mCurrentItem, smoothScroll);
    }

    public BannerAdapter getBannerAdapter() {
        return mBannerAdapter;
    }

    /**
     * 设置轮播适配器，一定要设置此适配器，轮播组件会从适配器获取数据及进行展示
     *
     * @param bannerAdapter 适配器
     */
    public void setBannerAdapter(BannerAdapter bannerAdapter) {
        int size;
        if (bannerAdapter != null && bannerAdapter.getDataList() != null) {
            size = bannerAdapter.getDataList().size();
            if (size > 1) {
                initSegmentProgressBar(size);
            }
        }
        mBannerAdapter = bannerAdapter;
        mBannerPagerAdapter = new BannerPagerAdapter(bannerAdapter);
        mViewPager.setAdapter(mBannerPagerAdapter);
        mViewPager.setCurrentItem(mCurrentItem);

        handleBannerShow();
    }

    private void initSegmentProgressBar(int size) {
        mSegmentProgressBar.initialize(size, DEFAULT_LOOP_INTERVAL, true, new SegmentProgressBar.ISegmentProgressBarListener() {

            @Override
            public void onAutoSelectedSegment(int segmentSize, int currentPosition) {
                setCurrentItem(currentPosition, true);
            }

            @Override
            public void onClickSegment(int segmentSize, int currentPosition, int clickPosition) {
                setCurrentItem(clickPosition, false);
            }
        });
    }

    private void handleBannerShow() {
        if (mOnGlobalLayoutListener != null) {
            mViewPager.getViewTreeObserver().removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
        }

        if (mBannerPagerAdapter != null && mBannerPagerAdapter.getCount() == 1) {
            //只有一页并且不可滑动时不会回调onPageSelected，在此回调可见事件
            if (mOnGlobalLayoutListener == null) {
                mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (mBannerAdapter != null && mBannerPagerAdapter != null && mBannerPagerAdapter.getCount() == 1) {
                            mBannerAdapter.onBannerItemChange(0, mBannerPagerAdapter.mData.get(0));
                        }
                        removeOnGlobalLayoutListener();
                    }
                };
            }
            mViewPager.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
        }
    }

    private void removeOnGlobalLayoutListener() {
        if (mOnGlobalLayoutListener != null) {
            mViewPager.getViewTreeObserver().removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
            mOnGlobalLayoutListener = null;
        }
    }

    /**
     * 开启轮播，默认自动开启
     */
    public void startLoop() {
        if (mSegmentProgressBar != null) {
            mSegmentProgressBar.startAutoPlay(0, 0f);
        }
    }


    public BannerPagerAdapter getBannerPagerAdapter() {
        return mBannerPagerAdapter;
    }

    public int getCurrentItem() {
        return mCurrentItem;
    }


    /**
     * 轮播适配器
     */
    public interface BannerAdapter<Item> {

        /**
         * 获取轮播数据
         *
         * @return 数据列表
         */
        List<Item> getDataList();

        /**
         * 获取轮播View，此View会被添加进轮播器
         *
         * @see PagerAdapter#instantiateItem(ViewGroup, int)
         */
        View getBannerItemView(@NonNull @NotNull ViewGroup container, int position);

        /**
         * 绑定数据
         */
        void bindBannerItemView(int position, View itemView, Item itemData);

        /**
         * 轮播item点击回调
         */
        void onBannerItemClick(int position, Item itemData);

        void onBannerItemChange(int position, Item itemData);
    }

    public static class BannerPagerAdapter extends PagerAdapter {

        private final BannerAdapter mBannerAdapter;
        private final List mData;

        public BannerPagerAdapter(BannerAdapter bannerAdapter) {
            mData = new ArrayList<>(bannerAdapter.getDataList());
            mBannerAdapter = bannerAdapter;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @NonNull
        @NotNull
        @Override
        public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {
            View item = mBannerAdapter.getBannerItemView(container, position);
            final int finalPosition = position;
            item.setOnClickListener(v -> mBannerAdapter.onBannerItemClick(finalPosition, mData.get(finalPosition)));
            mBannerAdapter.bindBannerItemView(finalPosition, item, mData.get(finalPosition));
            container.addView(item);
            return item;
        }

        @Override
        public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object o) {
            return view == o;
        }

        @Override
        public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
            View item = (View) object;
            container.removeView(item);
        }

        public List getData() {
            return mData;
        }
    }

    /**
     * 用于解决ViewPager切换过快问题
     * <p>
     * 使用setCurrentItem(int item, boolean smoothScroll)切换时动画时长过小，这里从新指定时长
     */
    private static class PagerScroller extends Scroller {

        private static final Interpolator sInterpolator = new Interpolator() {
            public float getInterpolation(float t) {
                --t;
                return t * t * t * t * t + 1.0F;
            }
        };

        private static final int sDuration = 1200;
        private int mPageWidth;

        public PagerScroller(Context context) {
            super(context);
        }

        public PagerScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public PagerScroller(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            int d = mPageWidth == dx ? sDuration : duration;
            super.startScroll(startX, startY, dx, dy, d);
        }

        private void setPageWidth(int pageWidth) {
            mPageWidth = pageWidth;
        }
    }

    private static class NestedViewPager extends ViewPager {

        int lastX, lastY;
        //是否正在水平滑动
        boolean isHorizonSliding;

        public NestedViewPager(@NonNull Context context) {
            this(context, null);
        }

        public NestedViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            int x = (int) ev.getX();
            int y = (int) ev.getY();
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isHorizonSliding = false;
                    getParent().requestDisallowInterceptTouchEvent(true);
                    break;
                case MotionEvent.ACTION_MOVE:
                    int dx = Math.abs(x - lastX);
                    int dy = Math.abs(y - lastY);
                    //竖划——交由父容器处理  水平滑动时拦截竖直滑动
                    if (dy > dx && !isHorizonSliding) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    } else if (dx - dy > ViewConfiguration.getTouchSlop()) {
                        isHorizonSliding = true;
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    isHorizonSliding = false;
                    getParent().requestDisallowInterceptTouchEvent(false);
                    break;
            }
            lastX = x;
            lastY = y;
            return super.dispatchTouchEvent(ev);
        }
    }
}