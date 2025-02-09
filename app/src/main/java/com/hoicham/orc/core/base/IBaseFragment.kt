package com.hoicham.orc.core.base

import android.os.Bundle
import android.view.View

interface IBaseFragment {
    fun initView(view: View, savedInstanceState: Bundle?)

    fun initData()

    fun onBack()

    fun initListener()
}