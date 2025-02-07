package com.hoicham.orc.ui.intro.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.hoicham.orc.R
import com.hoicham.orc.databinding.FragmentOnBoardingScreen1Binding

class OnBoardingScreen1 : Fragment(R.layout.fragment_on_boarding_screen1) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentOnBoardingScreen1Binding.bind(view)

        val viewPager = requireActivity().findViewById<ViewPager2>(R.id.view_pager)

        binding.apply {

            buttonNext.setOnClickListener {
                viewPager.currentItem = 1
            }
        }
    }

}