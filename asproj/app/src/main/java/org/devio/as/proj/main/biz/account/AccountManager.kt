package org.devio.`as`.proj.main.biz.account

import android.app.Application
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import org.devio.`as`.proj.common.utils.SPUtil
import org.devio.`as`.proj.main.http.ApiFactory
import org.devio.`as`.proj.main.http.api.AccountApi
import org.devio.`as`.proj.main.model.UserProfile
import org.devio.hi.library.cache.HiStorage
import org.devio.hi.library.executor.HiExecutor
import org.devio.hi.library.restful.HiCallback
import org.devio.hi.library.restful.HiResponse
import org.devio.hi.library.util.AppGlobals
import java.lang.IllegalStateException

object AccountManager {

    private var userProfile: UserProfile? = null
    private val KEY_USER_PROFILE: String = "user_profile"
    private var boardingPass: String? = null
    private val KEY_BOARDING_PASS = "board_pass"
    private val loginLiveData = MutableLiveData<Boolean>()
    private val loginForeverObservers = mutableListOf<Observer<Boolean>>()

    private val profileLiveData = MutableLiveData<UserProfile>()
    private val profileForeverObservers = mutableListOf<Observer<UserProfile?>>()

    @Volatile
    private var isFetching = false

    fun login(context: Context? = AppGlobals.get(), observer: Observer<Boolean>) {
        if (context is LifecycleOwner) {
            loginLiveData.observe(context, observer)
        } else {
            loginLiveData.observeForever(observer)
        }

        val intent = Intent(context, LoginActivity::class.java)
        if (context is Application) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        if (context == null) {
            throw  IllegalStateException("context must not be null")
        }
        context.startActivity(intent)
    }

    fun loginSuccess(boardingPass: String) {
        SPUtil.putString(KEY_BOARDING_PASS, boardingPass)
        this.boardingPass = boardingPass
        loginLiveData.value = true
        clearLoginForeverObserver()
    }

    private fun clearLoginForeverObserver() {
        for (observer in loginForeverObservers) {
            loginLiveData.removeObserver(observer)
        }
        loginForeverObservers.clear()
    }

    @Synchronized
    fun getUserProfile(
        lifecycleOwner: LifecycleOwner?,
        observer: Observer<UserProfile?>,
        onlyCache: Boolean
    ) {
        if (lifecycleOwner == null) {
            profileLiveData.observeForever(observer)
            profileForeverObservers.add(observer)
        } else {
            profileLiveData.observe(lifecycleOwner, observer)
        }

        if (userProfile != null && onlyCache) {
            profileLiveData.postValue(userProfile)
            return
        }

        if (isFetching) {
            return
        } else {
            isFetching = true
        }

        ApiFactory.create(AccountApi::class.java).profile()
            .enqueue(object : HiCallback<UserProfile> {
                override fun onSuccess(response: HiResponse<UserProfile>) {
                    userProfile = response.data
                    if (response.code == HiResponse.SUCCESS && userProfile != null) {
                        HiExecutor.execute(runnable = Runnable {
                            HiStorage.saveCache(KEY_USER_PROFILE, userProfile)
                            isFetching = false
                        })

                        profileLiveData.value = userProfile
                    } else {
                        profileLiveData.value = null
                    }
                    clearProfileForeverObservers()
                }

                override fun onFailed(throwable: Throwable) {
                    isFetching = false
                    profileLiveData.postValue(null)
                }
            })
    }

    private fun clearProfileForeverObservers() {
        for (observer in profileForeverObservers) {
            profileLiveData.removeObserver(observer)
        }
        profileForeverObservers.clear()
    }

    fun getBoardingPass(): String {
        if (TextUtils.isEmpty(boardingPass)) {
            boardingPass = SPUtil.getString(KEY_BOARDING_PASS)
        }
        return boardingPass ?: ""
    }

    fun isLogin(): Boolean {
        return !TextUtils.isEmpty(getBoardingPass())
    }
}