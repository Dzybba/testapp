package com.simapp.testapp.auth.data

import com.simapp.testapp.auth.domain.User
import io.reactivex.Maybe

interface IServerDataSource {
    fun requestUser(): Maybe<User>
}