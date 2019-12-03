package org.beahugs.common.weiget.loadingview;
/**
 * @Author: wangyibo
 * @Version: 1.0
 */
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;

import org.beahugs.common.R;
import java.util.HashMap;
import java.util.Map;


public class XLoadingView extends FrameLayout {

    private int mEmptyViewResId;
    private int mErrorViewResId;
    private int mLoadingViewResId;
    private int mNoNetworkViewResId;
    private int mContentViewResId;
    private LayoutInflater mInflater;
    private OnClickListener mOnRetryClickListener;
    private Map<Integer, View> mResId = new HashMap<>();

    public static XLoadingViewConfig config=new XLoadingViewConfig();

    public static XLoadingViewConfig init() {
        return config;
    }

    public static XLoadingView wrap(Activity activity) {
        return wrap(((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0));
    }

    public static XLoadingView wrap(Fragment fragment) {
        return wrap(fragment.getView());
    }

    public static XLoadingView wrap(View view) {
        if (view == null) {
            throw new RuntimeException("content view can not be null");
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (view == null) {
            throw new RuntimeException("parent view can not be null");
        }
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        int index = parent.indexOfChild(view);
        parent.removeView(view);

        XLoadingView xLoadingView = new XLoadingView(view.getContext());
        parent.addView(xLoadingView, index, lp);
        xLoadingView.addView(view);
        xLoadingView.setContentView(view);
        return xLoadingView;
    }

    public XLoadingView(Context context) {
        this(context, null);
    }

    public XLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(context);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.XLoadingView, defStyleAttr, 0);
        mEmptyViewResId = a.getResourceId(R.styleable.XLoadingView_emptyView, config.getEmptyViewResId());
        mErrorViewResId = a.getResourceId(R.styleable.XLoadingView_errorView, config.getErrorViewResId());
        mLoadingViewResId = a.getResourceId(R.styleable.XLoadingView_loadingView, config.getLoadingViewResId());
        mNoNetworkViewResId = a.getResourceId(R.styleable.XLoadingView_noNetworkView, config.getNoNetworkViewResId());
        a.recycle();
    }

    private void setContentView(View view) {
        mContentViewResId = view.getId();
        mResId.put(mContentViewResId, view);
    }




    public final void hideLayout(){
        if (isShowStatue){
            for (View view : mResId.values()) {
                view.setVisibility(GONE);
            }
        }
    }



    public final void showEmpty() {
        show(mEmptyViewResId);
    }

    public final void showError() {
        show(mErrorViewResId);
    }

    public final void showLoading() {
        show(mLoadingViewResId);
    }

    public final void showNoNetwork() {
        show(mNoNetworkViewResId);
    }

    public final void showContent() {
        show(mContentViewResId);
    }

    private boolean isShowStatue;

    private void show(int resId) {

        for (View view : mResId.values()) {
            view.setVisibility(GONE);
        }
        layout(resId).setVisibility(VISIBLE);
        isShowStatue = true;
    }

    private View layout(int resId) {
        if (mResId.containsKey(resId)) {
            return mResId.get(resId);
        }
        View view = mInflater.inflate(resId, this, false);
        view.setVisibility(GONE);
        addView(view);
        mResId.put(resId, view);
        if (resId == mErrorViewResId||resId == mNoNetworkViewResId) {
            View v=view.findViewById(R.id.loading_view);
            if (mOnRetryClickListener != null) {
                if (v != null)
                    v.setOnClickListener(mOnRetryClickListener);
                else
                    view.setOnClickListener(mOnRetryClickListener);
            }
        }
        return view;
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() == 0) {
            return;
        }
        if (getChildCount() > 1) {
            removeViews(1, getChildCount() - 1);
        }
        View view = getChildAt(0);
        setContentView(view);
        showLoading();
    }
    /**
     * 设置重试点击事件
     *
     * @param onRetryClickListener 重试点击事件
     */
    public void setOnRetryClickListener(OnClickListener onRetryClickListener) {
        this.mOnRetryClickListener = onRetryClickListener;
    }
}
