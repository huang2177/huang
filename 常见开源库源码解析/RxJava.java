package com.oktaojin.ok.api;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class RxJava {
    public void test() {
        Disposable subscribe = Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) {
                        Log.e("=======create: " , Thread.currentThread().getName());
                        emitter.onNext("-----");
                    }
                })

                // subscribeOn 多次调用只有第一个生效
                // 因为【subscribe】事件是从下往上的，前面的都会被覆盖掉；
                .subscribeOn(Schedulers.computation())

                // observeOn每次调用都会生效；
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        Log.e("======doOnSubscribe11: " , Thread.currentThread().getName());
                    }
                })
                .observeOn(Schedulers.computation())

                //返回ObservableMap
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) {
                        Log.e("======map: " , Thread.currentThread().getName());
                        return s + "====";
                    }
                })

                // 上面map()返回的ObservableMap会传入编程source；
                // 当调用subscribeActual()的时候就会调用ObservableMap的subscribeActual()的方法，一层层网上调用；

                // 同时该方法里面会创建一个MapObserver，也就是对真实的Observer（actual）的包装；
                // 并在上游回调OnNext的时候先在自己的OnNext里面进行Predicate的处理，之后在把处理之后的结果回调给actual；
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) {
                        Log.e("======filter: " , Thread.currentThread().getName());
                        return true;
                    }
                })


                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e("=======subscribe: " , Thread.currentThread().getName());
                    }
                });
    }
}
