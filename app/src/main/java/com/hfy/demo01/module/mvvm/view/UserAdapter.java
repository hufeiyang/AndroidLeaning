package com.hfy.demo01.module.mvvm.view;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hfy.demo01.R;
import com.hfy.demo01.module.mvvm.model.User;

/**
 * @author hufeiyang
 * @data 2021/1/24
 * @Description:
 */
class UserAdapter extends BaseQuickAdapter<User, BaseViewHolder> {
    public UserAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, User user) {
        viewHolder.setText(R.id.tv_user_name, user.getName());
        viewHolder.setText(R.id.tv_user_age, String.valueOf(user.getAge()));
    }
}
