package com.example.bookrent.base.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity: AppCompatActivity(), BaseView{

    protected val disposableBag = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        attachView()
        onViewReady()
        if (savedInstanceState == null) {
            onInit()
        }
    }

    override fun onDestroy() {
        disposableBag.dispose()
        detachView()
        super.onDestroy()
    }

    fun replaceFragment(fragment: Fragment, frameLayout: FrameLayout) {
        val backStateName = fragment.javaClass.name

        val manager = supportFragmentManager
        val fragmentPopped = manager.popBackStackImmediate(backStateName, 0)

        if (!fragmentPopped && manager.findFragmentByTag(backStateName) == null) { //fragment not in back stack, create it.
            val ft = manager.beginTransaction()
            ft.replace(frameLayout.id, fragment, backStateName)
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            ft.addToBackStack(backStateName)
            ft.commit()
        }
    }

    override fun onInit() {}

}