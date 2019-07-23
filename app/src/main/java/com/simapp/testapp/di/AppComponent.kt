package com.simapp.testapp.di

import android.app.Application
import com.simapp.testapp.TestAppApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by denisdobryshkin
 * on 29.01.2018.
 */

@Singleton
@Component(modules = [
    (AppModule::class),
    (ActivityBindingModule::class),
    (AndroidSupportInjectionModule::class)])
interface AppComponent : AndroidInjector<TestAppApplication> {

    // Gives us syntactic sugar. we can then do DaggerAppComponent.builder().application(this).build().inject(this);
    // never having to instantiate any modules or say which module we are passing the application to.
    // Application will just be provided into our app graph now.

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

}