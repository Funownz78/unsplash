package com.example.imageshering.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.imageshering.databinding.FragmentSecondOnBoardingBinding

class SecondOnBoardingFragment : Fragment() {
    private var _binding: FragmentSecondOnBoardingBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondOnBoardingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.secondToNextScreen.setOnClickListener {
            (activity as ViewPagerHandler).nextPage()
        }
        binding.secondToPrevScreen.setOnClickListener {
            (activity as ViewPagerHandler).prevPage()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}