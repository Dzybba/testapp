package com.simapp.testapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.simapp.testapp.R
import com.simapp.testapp.auth.domain.AuthTypes
import com.simapp.testapp.auth.domain.IAuthUseCases
import com.simapp.testapp.auth.navigator.IAuthNavigator
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.PublishProcessor
import javax.inject.Inject


class MainActivity : AppCompatActivity(), HasSupportFragmentInjector, IRxActivity {
    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var authUseCases: IAuthUseCases

    @Inject
    lateinit var authNavigator: IAuthNavigator


    private var onStartSubscriptions = CompositeDisposable()
    private var onCreateSubscriptions = CompositeDisposable()

    private val activityResult = PublishProcessor.create<ActivityResult>()
    override val rxActivityResult: Flowable<ActivityResult>
        get() = activityResult


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        onCreateSubscriptions.add(authNavigator
                .runAuth(this, AuthTypes.VK)
                .subscribe(
                        {
                            Log.e("DD", "get result $it")
                        },
                        {

                        })
        )
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        activityResult.onNext(ActivityResult(requestCode, resultCode, data))
    }
}
