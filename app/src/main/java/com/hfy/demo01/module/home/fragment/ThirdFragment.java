package com.hfy.demo01.module.home.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.Utils;
import com.hfy.demo01.R;
import com.hfy.demo01.hook.TestHookActivity;
import com.hfy.demo01.performance.fps.PerformanceLearningActivity;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ThirdFragment extends Fragment {

    private Unbinder mUnbind;

    private String TAG = "hufeiyang-mmy";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_third, null);

        mUnbind = ButterKnife.bind(this, view);

        String isSupportDYShare = getMetaDataInMMYApp("is_support_dy_Share");
        String isInMMY = getMetaDataInMMYApp("is_run_in_mmy");

        String msg = "onClick: isSupportDYShare=" + isSupportDYShare + "，isInMMY=" + isInMMY;
        Log.i(TAG, msg);

        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

        return view;
    }



    @OnClick({
            R.id.btn_learn_hook,
            R.id.btn_share_dy1,
            R.id.btn_share_dy2,
            R.id.btn_performance_learning
    })
    public void onClick(View view) {
        String filePath;

        switch (view.getId()) {
            case R.id.btn_learn_hook:
                TestHookActivity.launch(getActivity());
                break;
            case R.id.btn_share_dy1:
                filePath = "/storage/emulated/0/Android/data/com.hfy.demo01/files/666.mp4";
                shareToDY(filePath);
                break;
            case R.id.btn_share_dy2:
                filePath = "/storage/emulated/0/DCIM/mmy_records/org_screen_record_1630406673942.mp4";
                shareToDY(filePath);
                break;
            case R.id.btn_performance_learning:
                PerformanceLearningActivity.launch(getActivity());
                break;
            default:
                break;
        }
    }

    private void shareToDY(String filePath) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N){
            filePath = getFileUri(filePath);
        }

        Intent intent = new Intent();
        intent.setData(Uri.parse("mmy://vplatform/share?type=8"));
        intent.putExtra("gamePkgName", "com.hfy.androidlearning");
        intent.putExtra("dyVideo", filePath);
        this.startActivityForResult(intent, 0);
    }


    /**
     * FileProvider适配
     */
    public String getFileUri(String filePath) {
        File file = new File(filePath);
        // 使用contentPath作为文件路径进行分享
        // 要与`AndroidManifest.xml`里配置的`authorities`一致，假设你的应用包名为com.example.app
        Context context = getContext();
        Uri contentUri = FileProvider.getUriForFile(context, "com.hfy.androidlearning.fileprovider",file);
        // 授权给抖音访问路径,这里填抖音包名
        context.grantUriPermission("com.ss.android.ugc.aweme", contentUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return contentUri.toString();
    }

    /**
     * 获取mmy的meta-data
     *
     * @param key
     * @return
     */
    public static String getMetaDataInMMYApp(@NonNull final String key) {
        String value = "";
        PackageManager pm = Utils.getApp().getPackageManager();
        try {
            ApplicationInfo ai = pm.getApplicationInfo("com.playgame.havefun", PackageManager.GET_META_DATA);
            value = String.valueOf(ai.metaData.get(key));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

}