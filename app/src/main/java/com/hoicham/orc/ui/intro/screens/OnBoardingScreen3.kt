package com.hoicham.orc.ui.intro.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.hoicham.orc.R
import com.hoicham.orc.databinding.FragmentOnBoardingScreen3Binding

class OnBoardingScreen3 : Fragment(R.layout.fragment_on_boarding_screen3) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentOnBoardingScreen3Binding.bind(view)

        val viewPager = requireActivity().findViewById<ViewPager2>(R.id.view_pager)
//        val datastore = AppPreferences(requireContext().datastore)


        binding.apply {
            animationView.repeatCount = 2

            buttonFinish.setOnClickListener {
//                lifecycleScope.launch { datastore.firstLaunchComplete() }
                findNavController().navigateUp()
            }

            buttonPrevious.setOnClickListener {
                viewPager.currentItem = 1
            }
        }
    }
}