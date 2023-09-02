package com.example.imageshering.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.imageshering.databinding.FragmentThirdOnBoardingBinding

class ThirdOnBoardingFragment : Fragment() {
    private var _binding: FragmentThirdOnBoardingBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThirdOnBoardingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.thirdToNextScreen.setOnClickListener {
            (activity as ViewPagerHandler).nextPage()
        }
        binding.thirdToPrevScreen.setOnClickListener {
            (activity as ViewPagerHandler).prevPage()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}