package com.hoicham.orc.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.hoicham.orc.core.base.BaseFragment
import com.hoicham.orc.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override fun inflateLayout(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun initData() {
    }

    override fun onBack() {
    }

    override fun initListener() {
    }
}