package com.simapp.testapp.ui.main

import android.content.Intent
import io.reactivex.Flowable


data class ActivityResult(val requestCode: Int, val resultCode: Int, val intent: Intent?)

interface IRxActivity {
    val rxActivityResult: Flowable<ActivityResult>
}