package com.hoicham.orc.ui.intro

import com.hoicham.orc.ui.intro.screens.OnBoardingScreen3
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.hoicham.orc.R
import com.hoicham.orc.databinding.FragmentViewPagerBinding
import com.hoicham.orc.ui.intro.screens.OnBoardingScreen1
import com.hoicham.orc.ui.intro.screens.OnBoardingScreen2

/**
 * A simple [Fragment] subclass.
 * Use the [ViewPagerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewPagerFragment : Fragment(R.layout.fragment_view_pager) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentViewPagerBinding.bind(view)

        val fragmentList = arrayListOf(
            OnBoardingScreen1(),
            OnBoardingScreen2(),
            OnBoardingScreen3()
        )

        /*
        Use childFragmentManager instead of activity.supportFragmentManager
        to preserve state when configuration change happens.

        This is because in this case we are making nested navigation, using a Fragment to host
        onBoarding Fragments.
         */
        val adapter = ViewPagerAdapter(
            fragmentList,
            childFragmentManager,
            viewLifecycleOwner.lifecycle
        )

        binding.apply {
            viewPager.adapter = adapter
        }
    }
}