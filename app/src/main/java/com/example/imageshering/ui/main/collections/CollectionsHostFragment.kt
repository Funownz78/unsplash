package com.example.imageshering.ui.main.collections

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.imageshering.R
import com.example.imageshering.databinding.FragmentCollectionsHostBinding

class CollectionsHostFragment : Fragment() {
    private var _binding: FragmentCollectionsHostBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionsHostBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("CollectionsHostFragment", "onViewCreated: CollectionsHostFragment")
        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            getString(R.string.collections).uppercase()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}