package com.cypoem.retrofit.fragment;

import android.os.Bundle;

import com.cypoem.retrofit.R;
import com.cypoem.retrofit.bean.BaseBean;
import com.cypoem.retrofit.bean.MeiZi;
import com.cypoem.retrofit.net.BaseObserver;
import com.cypoem.retrofit.net.RetrofitUtils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhpan on 2017/9/30.
 * Fragment没有运行 内容仅供参考
 */

public class TestFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        getData();
    }

    public void getData() {

        RetrofitUtils.getApiService()
                .getMezi()
                .compose(this.<BaseBean<List<MeiZi>>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseBean<List<MeiZi>>>(getActivity()) {
                    @Override
                    public void onSuccess(BaseBean<List<MeiZi>> response) {
                        List<MeiZi> results = response.getResults();
                        showToast("请求成功，妹子个数为"+results.size());
                    }
                });
    }
}
