package com.cypoem.retrofit.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cypoem.retrofit.R;
import com.cypoem.retrofit.bean.BaseBean;
import com.cypoem.retrofit.net.BaseObserver;
import com.cypoem.retrofit.net.RetrofitUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity {
    private Button btn;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initView();
    }

    private void initView() {
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }


    public void getData() {
        /*RetrofitUtils.getApiService()
                .getMezi()
                .compose(this.<BaseBean<List<MeiZi>>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseBean<List<MeiZi>>>(this) {
                    @Override
                    public void onSuccess(BaseBean<List<MeiZi>> response) {
                        List<MeiZi> results = response.getResults();
                        showToast("请求成功，妹子个数为"+results.size());
                    }
                });*/

        RetrofitUtils.getApiService().get("https://snail-stg1.zysnail.com/snail/queryIdentities.do?sid=1&userId=123456")
                .compose(this.<BaseBean<String>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseBean<String>>(this){
                    @Override
                    public void onSuccess(BaseBean<String> baseBean) {
                        System.out.println(baseBean.getCode()+" / "+baseBean.getMessage());
                    }
                });

    }

}
