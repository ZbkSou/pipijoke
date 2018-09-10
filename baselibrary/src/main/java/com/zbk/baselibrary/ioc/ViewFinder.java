package com.zbk.baselibrary.ioc;

import android.app.Activity;
import android.view.View;

/**
 * Created by ZBK on 2018-09-09.
 * <p>
 * View 的 finder的属性类
 *
 * @function
 */

public class ViewFinder {
    private View mView;
    private Activity mActivity;

    public ViewFinder(Activity activity) {
        this.mActivity = activity;
    }

    public ViewFinder(View view) {
        this.mView = view;
    }
    public  View  findViewById(int viewId){
        return mActivity!=null? mActivity.findViewById(viewId):mView.findViewById(viewId);
    }
}
