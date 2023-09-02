package com.example.imageshering.ui.main.home

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
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.example.imageshering.App
import com.example.imageshering.R
import com.example.imageshering.databinding.FragmentSearchPhotoBinding
import com.example.imageshering.ui.main.GridSpacingItemDecoration
import com.example.imageshering.ui.main.PhotosAdapter
import com.example.imageshering.ui.main.PhotosLoadStateAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchPhotoFragment : Fragment() {
    private var _binding: FragmentSearchPhotoBinding? = null
    private val binding get() = _binding!!
    private val args: SearchPhotoFragmentArgs by navArgs()
    private val searchPhotoViewModelFactory = App.daggerComponent.getSearchPhotoViewModelFactory()
    private val viewModel: SearchPhotoViewModel by viewModels { searchPhotoViewModelFactory }
    private val transitionToDetails = { id: String ->
        val action = SearchPhotoFragmentDirections
            .actionSearchPhotoFragmentToHomeDetailsFragment(id)
        findNavController().navigate(action)
    }
    private val setLike = { id: String, undoCallback: () -> Unit ->
        viewModel.setLike(id, undoCallback)
    }
    private val removeLike = { id: String, undoCallback: () -> Unit ->
        viewModel.removeLike(id, undoCallback)
    }
    private lateinit var adapter: PhotosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setPagingDataWithQuery(args.query)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchPhotoBinding.inflate(inflater)
        binding.cancelSearchButton.setOnClickListener {
            findNavController().navigateUp()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.grid_spacing)
        binding.photosSearchRecyclerView.addItemDecoration(GridSpacingItemDecoration(spacingInPixels))

        adapter = PhotosAdapter(
            isConnected = { true },
            setLike = setLike,
            removeLike = removeLike,
            transitionToDetails = transitionToDetails
        )
        binding.photosSearchRecyclerView.adapter =
            adapter.withLoadStateFooter(PhotosLoadStateAdapter { adapter.retry() })

        binding.queryText.text = args.query

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                while (!viewModel.isInitialized())
                    delay(100)
                viewModel.searchedPhotos.collect {
                    adapter.submitData(it)
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

    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            getString(R.string.home).uppercase()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}