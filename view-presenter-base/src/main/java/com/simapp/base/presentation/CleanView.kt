package com.simapp.base.presentation

import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager

interface CleanView {
    fun getActivity(): FragmentActivity?
    fun getSupportFragmentManager(): FragmentManager?
}