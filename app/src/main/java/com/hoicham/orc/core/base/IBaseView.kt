package com.hoicham.orc.core.base

import android.os.Bundle

interface IBaseView {
    fun initView(savedInstanceState: Bundle?)

    fun initData()

    fun onBack()

    fun initListener()
}