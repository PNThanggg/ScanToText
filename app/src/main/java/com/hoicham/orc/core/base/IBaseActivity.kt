package com.hoicham.orc.core.base

import android.os.Bundle

interface IBaseActivity {
    fun initView(savedInstanceState: Bundle?)

    fun initData()

    fun onBack()

    fun initListener()
}