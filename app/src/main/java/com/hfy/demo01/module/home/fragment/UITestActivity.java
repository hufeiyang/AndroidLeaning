package com.hfy.demo01.module.home.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.hfy.demo01.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UITestActivity extends AppCompatActivity {

    @BindView(R.id.tv_title1)
    TextView tvTitle1;
    @BindView(R.id.tv_left1)
    TextView tvLeft1;
    @BindView(R.id.tv_right1)
    TextView tvRight1;
    @BindView(R.id.tv_title2)
    TextView tvTitle2;
    @BindView(R.id.tv_left2)
    TextView tvLeft2;
    @BindView(R.id.tv_right2)
    TextView tvRight2;
    @BindView(R.id.tv_title3)
    TextView tvTitle3;
    @BindView(R.id.tv_left3)
    TextView tvLeft3;
    @BindView(R.id.tv_right3)
    TextView tvRight3;
    @BindView(R.id.tv_title4)
    TextView tvTitle4;
    @BindView(R.id.tv_left4)
    TextView tvLeft4;
    @BindView(R.id.tv_right4)
    TextView tvRight4;
    @BindView(R.id.tv_title5)
    TextView tvTitle5;
    @BindView(R.id.tv_left5)
    TextView tvLeft5;
    @BindView(R.id.tv_right5)
    TextView tvRight5;
    @BindView(R.id.tv_title6)
    TextView tvTitle6;
    @BindView(R.id.tv_left6)
    TextView tvLeft6;
    @BindView(R.id.tv_right6)
    TextView tvRight6;
    @BindView(R.id.tv_title7)
    TextView tvTitle7;
    @BindView(R.id.tv_left7)
    TextView tvLeft7;
    @BindView(R.id.tv_right7)
    TextView tvRight7;
    @BindView(R.id.tv_title8)
    TextView tvTitle8;
    @BindView(R.id.tv_left8)
    TextView tvLeft8;
    @BindView(R.id.tv_right8)
    TextView tvRight8;
    @BindView(R.id.tv_title9)
    TextView tvTitle9;
    @BindView(R.id.tv_left9)
    TextView tvLeft9;
    @BindView(R.id.tv_right9)
    TextView tvRight9;
    @BindView(R.id.tv_title10)
    TextView tvTitle10;
    @BindView(R.id.tv_left10)
    TextView tvLeft10;
    @BindView(R.id.tv_right10)
    TextView tvRight10;
    @BindView(R.id.tv_title11)
    TextView tvTitle11;
    @BindView(R.id.tv_left11)
    TextView tvLeft11;
    @BindView(R.id.tv_right11)
    TextView tvRight11;


    public static void launch(FragmentActivity activity) {
        Intent intent = new Intent(activity, UITestActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_u_i_test);
        ButterKnife.bind(this);


//        TextView textView = (TextView) view;
//        String lineHeight = "行高:" + textView.getLineHeight();
//        Log.d("test", lineHeight);
//        String textViewHeight = "textView高度:" + textView.getHeight();
//        Log.d("test", textViewHeight);
//        Paint.FontMetrics fontMetrics = textView.getPaint().getFontMetrics();
//        String fontHeight = "字体高度:" + (fontMetrics.descent - fontMetrics.ascent);
//        Log.d("test", fontHeight);
////        Log.d("test","字体缩放--》" + getFontSize());
//
//        Rect rect = new Rect();
//        textView.getPaint().getTextBounds(textView.getText().toString(),0,textView.length(),rect);
//        String textRectHeight = "文字矩形高度:" + rect.height();
//        Log.d("test", textRectHeight);


        tvLeft1.setText(tvLeft1.getLineHeight() + tvLeft1.getText().toString());
        tvLeft2.setText(tvLeft2.getLineHeight() + tvLeft2.getText().toString());
        tvLeft3.setText(tvLeft3.getLineHeight() + tvLeft3.getText().toString());
        tvLeft4.setText(tvLeft4.getLineHeight() + tvLeft4.getText().toString());
        tvLeft5.setText(tvLeft5.getLineHeight() + tvLeft5.getText().toString());
        tvLeft6.setText(tvLeft6.getLineHeight() + tvLeft6.getText().toString());
        tvLeft7.setText(tvLeft7.getLineHeight() + tvLeft7.getText().toString());
        tvLeft8.setText(tvLeft8.getLineHeight() + tvLeft8.getText().toString());
        tvLeft9.setText(tvLeft9.getLineHeight() + tvLeft9.getText().toString());
        tvLeft10.setText(tvLeft10.getLineHeight() + tvLeft10.getText().toString());
        tvLeft11.setText(tvLeft11.getLineHeight() + tvLeft11.getText().toString());

    }


    @OnClick({R.id.tv_title1, R.id.tv_left1, R.id.tv_right1, R.id.tv_title2, R.id.tv_left2, R.id.tv_right2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_title1:
                break;
            case R.id.tv_left1:
                break;
            case R.id.tv_right1:
                break;
            case R.id.tv_title2:
                break;
            case R.id.tv_left2:
                break;
            case R.id.tv_right2:
                break;
        }

        TextView textView = (TextView) view;
        String lineHeight = "行高:" + textView.getLineHeight();
        Log.d("test", lineHeight);
        String textViewHeight = "textView高度:" + textView.getHeight();
        Log.d("test", textViewHeight);
        Paint.FontMetrics fontMetrics = textView.getPaint().getFontMetrics();
        String fontHeight = "字体高度:" + (fontMetrics.descent - fontMetrics.ascent);
        Log.d("test", fontHeight);
//        Log.d("test","字体缩放--》" + getFontSize());

        Rect rect = new Rect();
        textView.getPaint().getTextBounds(textView.getText().toString(), 0, textView.length(), rect);
        String textRectHeight = "文字矩形高度:" + rect.height();
        Log.d("test", textRectHeight);

        Toast.makeText(this, lineHeight + "\n" + textViewHeight + "\n" + fontHeight + "\n" + textRectHeight, Toast.LENGTH_LONG).show();
    }
}
