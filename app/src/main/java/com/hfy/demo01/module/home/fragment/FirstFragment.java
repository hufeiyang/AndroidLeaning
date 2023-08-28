package com.hfy.demo01.module.home.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.hfy.demo01.R;
import com.hfy.demo01.common.customview.AppendViewAfterTextView;
import com.hfy.demo01.common.util.SizeUtil;
import com.hfy.demo01.kotlin.KotlinTestActivity;
import com.hfy.demo01.module.home.animation.AnimationTestActivity;
import com.hfy.demo01.module.home.bitmap.BitmapTestActivity;
import com.hfy.demo01.module.home.designsupportlibrarytest.MaterialDesignWidgetActivity;
import com.hfy.demo01.module.home.designsupportlibrarytest.NotificationActivity;
import com.hfy.demo01.module.home.designsupportlibrarytest.ViewEventTestActivity;
import com.hfy.demo01.module.home.leaktest.LeakTestActivity;
import com.hfy.demo01.module.home.touchevent.TouchEventTestEnterActivity;
import com.hfy.demo01.module.mvp.view.MvpActivity;
import com.hfy.simpleimageloader.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class FirstFragment extends Fragment {

    private static final int PERMISSIOINS_REQUEST_CODE_CALL = 1000;
    private static final String TAG = "FirstFragment";
    @BindView(R.id.btn_mvvm_data_binding_test)
    Button mButton;

    @BindView(R.id.btn_go_to_notification_activity)
    Button mBtnGoToNotificationPage;

    @BindView(R.id.iv_card_view)
    ImageView mImageView;

    @BindView(R.id.autoverticalscrolltextview)
    AutoVerticalScrollTextView autoVerticalScrollTextView;

    @BindView(R.id.autoSwitchTextView)
    AutoSwitchTextView autoSwitchTextView;


    @BindView(R.id.btn_test_leak)
    Button btnTestLeak;

    @BindView(R.id.btn_animation_test)
    Button btnTestAnimation;

    @BindView(R.id.btn_bitmap_test)
    Button btnTestBitmap;

    private Unbinder mUnbind;
    private int tempProgress = 0;
    private AppendViewAfterTextView appendViewAfterTextView;
    private PopupWindow mTipPopupWindow;
    private View tv_anchor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_first, null);

        appendViewAfterTextView = view.findViewById(R.id.appendViewAfterTextView);
        tv_anchor = view.findViewById(R.id.tv_anchor);

        mUnbind = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String s = "这个游戏真好玩呀！这个游戏真好玩呀！这个游戏真好玩呀！这个游戏真好玩呀！这个游戏真好玩呀！";
        appendViewAfterTextView.setText(s);
    }


    private void showCopyPopupWindow(View anchorView) {
        if (mTipPopupWindow == null) {
            View tipView = LayoutInflater.from(getContext()).inflate(R.layout.layout_publish_image_tips, null);
            mTipPopupWindow = new PopupWindow(tipView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tipView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTipPopupWindow.dismiss();
                }
            });
            mTipPopupWindow.setOutsideTouchable(true);
        }
        if (!mTipPopupWindow.isShowing()) {
            //调整popupwindow位置
            int screenWidth = getActivity().getResources().getDisplayMetrics().widthPixels;
            int[] locations = new int[2];
            anchorView.getLocationInWindow(locations);
            //保证popUp 与 ImageView 右边对齐
            int xOffset = screenWidth - locations[0] - anchorView.getWidth() - SizeUtil.dp2px(8f);
            //保证PopUpWindow 出现在 ImageView 的下方，并留出8dp的间距
            int yOffset = locations[1] + anchorView.getHeight() + SizeUtil.dp2px(8f);
            mTipPopupWindow.showAtLocation(anchorView, Gravity.END | Gravity.TOP, xOffset, yOffset);
            anchorView.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    mTipPopupWindow.dismiss();
                }
            }, 3000);
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        tv_anchor.postDelayed(new Runnable() {
            @Override
            public void run() {
                showCopyPopupWindow(tv_anchor);
            }
        }, 1000);

        //test自定义的ImageLoader
        String url = "http://a1.att.hudong.com/05/00/01300000194285122188000535877.jpg";
        ViewGroup.LayoutParams layoutParams = mImageView.getLayoutParams();
        ImageLoader.with(this.getContext()).loadBitmapAsync(url,mImageView, layoutParams.width, layoutParams.height);

        ArrayList<String> scrollMessageList = new ArrayList<>();
        scrollMessageList.add("hehe撒打发斯蒂芬");
        scrollMessageList.add("aaa沙发上发生的发放");
        scrollMessageList.add("hjjk阿斯蒂芬");
        scrollMessageList.add("hjjk阿斯是单方事多个");

        autoVerticalScrollTextView.setData(scrollMessageList);
        autoVerticalScrollTextView.start();
    }

    @OnClick({R.id.btn_mvvm_data_binding_test,
            R.id.btn_go_to_notification_activity,
            R.id.btn_go_to_call_activity,
            R.id.btn_go_to_material_design_activity,
            R.id.btn_go_to_view_test_activity,
            R.id.btn_go_to_SwitchTextView,
            R.id.btn_test_touch_event,
            R.id.btn_test_leak,
            R.id.btn_animation_test,
            R.id.btn_bitmap_test,
            R.id.btn_kotlin_test
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_mvvm_data_binding_test:
                MvpActivity.launch(getActivity());
                break;
            case R.id.btn_go_to_notification_activity:
                NotificationActivity.launch(getActivity());
                break;
            case R.id.btn_go_to_call_activity:
                call();
                break;
            case R.id.btn_go_to_material_design_activity:
                MaterialDesignWidgetActivity.launch(getActivity());
                break;
            case R.id.btn_go_to_view_test_activity:
                ViewEventTestActivity.launch(getActivity());
                break;
            case R.id.btn_go_to_SwitchTextView:
                ArrayList<String> scrollMessageList = new ArrayList<>();
                scrollMessageList.add("hehe撒打发斯蒂芬");
                scrollMessageList.add("aaa沙发上发生的发放");
                scrollMessageList.add("hjjk阿斯蒂芬");
                autoSwitchTextView.setData(scrollMessageList);
                break;
            case R.id.btn_test_touch_event:
                TouchEventTestEnterActivity.launch(getActivity());
                break;
            case R.id.btn_test_leak:
                LeakTestActivity.launch(getActivity());
                break;
            case R.id.btn_animation_test:
                AnimationTestActivity.launch(getActivity());
                break;
            case R.id.btn_bitmap_test:
                BitmapTestActivity.launch(getActivity());
                break;
            case R.id.btn_kotlin_test:
                KotlinTestActivity.launch(getActivity());
                break;
            default:
                break;
        }
    }

    private void call() {

        //检查权限
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            //没有就去申请
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE)) {
                // 用户拒绝过这个权限了，应该提示用户，为什么需要这个权限。
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setMessage("解释一下，因为你要打电话，所以需要打电话权限。")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //继续请求权限
                                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSIOINS_REQUEST_CODE_CALL);
                            }
                        }).create();
                dialog.show();
            } else {
                //请求权限，fragment中运行时权限的特殊处理、在Fragment中申请权限、不要使用ActivityCompat.requestPermissions、直接使用Fragment的requestPermissions方法、否则会回调到Activity的 onRequestPermissionsResult
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSIOINS_REQUEST_CODE_CALL);
            }
        } else {
            callPhone();
        }

    }

    /**
     * 申请权限的回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIOINS_REQUEST_CODE_CALL) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhone();
            }
            //拒绝后，再次弹框提示申请权限 ，若勾选“不再询问”后点击拒绝，会走此逻辑（此时shouldShowRequestPermissionRationale返回false）。
            //因为已经拒绝且不在提示，需要告诉用户如何打开权限。
            else if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE)) {
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setMessage("该功能需要访问电话的权限，当前未打开权限！ 打开权限的方法：设置-应用—权限。")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
            } else {
                Toast.makeText(getContext(), "被拒绝了", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 打电话.
     * <p>
     * 注意：
     * android.intent.action.CALL需要在配置文件中添加拨号权限 且 点击后直接拨号
     * android.intent.action.DIAL只是调用拨号键盘，不用在文件中添加权限
     */
    private void callPhone() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + "10086"));
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbind.unbind();
    }

}
