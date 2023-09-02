package com.example.imageshering.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.imageshering.databinding.FragmentFirstOnBoardingBinding

class FirstOnBoardingFragment : Fragment() {
    private var _binding: FragmentFirstOnBoardingBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstOnBoardingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.firstToNextScreen.setOnClickListener {
            (activity as ViewPagerHandler).nextPage()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}