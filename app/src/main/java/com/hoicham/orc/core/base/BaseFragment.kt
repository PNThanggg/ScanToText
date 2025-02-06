package com.hoicham.orc.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.hoicham.orc.core.extension.handleBackPressed

abstract class BaseFragment<VB : ViewBinding> : Fragment(), IBaseView {
    private var _binding: VB? = null
    val binding: VB get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = inflateLayout(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().handleBackPressed {
            onBack()
            return@handleBackPressed
        }

        initView(savedInstanceState)

        initData()

        initListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun inflateLayout(inflater: LayoutInflater, container: ViewGroup?): VB
}