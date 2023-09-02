package com.example.imageshering.ui.main.collections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.imageshering.App
import com.example.imageshering.R
import com.example.imageshering.databinding.FragmentCollectionsBinding
import com.example.imageshering.ui.main.CollectionsAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CollectionsFragment : Fragment() {
    private var _binding: FragmentCollectionsBinding? = null
    private val binding get() = _binding!!
    private val collectionViewModelFactory = App.daggerComponent.getCollectionViewModelFactory()
    private val viewModel: CollectionsViewModel by viewModels { collectionViewModelFactory }
    private val adapter = CollectionsAdapter {
        val action = CollectionsFragmentDirections
            .actionCollectionsFragmentToCollectionBlankFragment(it)
        findNavController().navigate(action)
    }
    private var isConnected: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionsBinding.inflate(inflater)

        binding.collectionsRecyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.pagedPhotoCollections.collect {
                    adapter.submitData(it)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.photosState.collect {
                    isConnected = it
                    binding.collectionsRecyclerTitle.text =
                        if (it) getString(R.string.collections_with_api)
                        else getString(R.string.collections_without_api)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                adapter.loadStateFlow.collectLatest { loadStates ->
                    val mediatorIsRefresh = loadStates.mediator?.refresh is LoadState.Loading
                    val sourceIsRefresh = loadStates.source.refresh is LoadState.Loading
                    binding.progressCircular.visibility =
                        if (sourceIsRefresh || mediatorIsRefresh) View.VISIBLE
                        else View.GONE
                }
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            getString(R.string.collections).uppercase()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}