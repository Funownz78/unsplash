package com.example.imageshering.ui.main.collections

import android.os.Bundle
import android.util.Log
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
import com.example.imageshering.databinding.FragmentCollectionDetailsBinding
import com.example.imageshering.ui.main.PhotosLoadStateAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "CollectionDetailsFragme"

class CollectionDetailsFragment : Fragment() {
    private var _binding: FragmentCollectionDetailsBinding? = null
    private val binding get() = _binding!!
    private val collectionDetailsViewModelFactory =
        App.daggerComponent.getCollectionDetailsViewModelFactory()
    private val viewModel: CollectionDetailsViewModel by viewModels { collectionDetailsViewModelFactory }
    private val args: CollectionDetailsFragmentArgs by navArgs()
    private val setLike = { id: String, undoCallback: () -> Unit ->
        viewModel.setLike(id, undoCallback)
    }
    private val removeLike = { id: String, undoCallback: () -> Unit ->
        viewModel.removeLike(id, undoCallback)
    }
    private val transitionToDetails = { id: String ->
        val action = CollectionDetailsFragmentDirections
            .actionCollectionBlankFragmentToHomeDetailsFragment2(id)
        findNavController().navigate(action)
    }

    private var isConnected: Boolean = false
    private lateinit var adapter: PhotoInCollectionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionDetailsBinding.inflate(inflater)

        adapter = PhotoInCollectionAdapter(
            isConnected = { isConnected },
            setLike = setLike,
            removeLike = removeLike,
            transitionToDetails = transitionToDetails
        )
        binding.collectionDetailsRecyclerView.adapter =
            adapter.withLoadStateFooter(PhotosLoadStateAdapter { adapter.retry() })

        Log.d("imageshering", "onCreateView: ${args.collectionId}")
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.photosState.collect {
                    isConnected = it
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.collectionDetails.collect { collectionDetailsOrNull ->
                    collectionDetailsOrNull?.let { collectionDetails ->
                        binding.apply {
                            collectionDetailsTitle.text = collectionDetails.title
                            collectionDetailsTags.text = collectionDetails.collectionTags
                            collectionDetailsAbout.text = collectionDetails.description
                            collectionDetailsSize.text = getString(
                                R.string.collection_size_and_username,
                                collectionDetails.totalPhotos,
                                collectionDetails.username
                            )
                        }
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                Log.d(TAG, "onCreateView: ")
                while (!viewModel.isInitialized())
                    delay(100)
                Log.d(TAG, "onCreateView: inited")
                viewModel.photoInCollection.collect { photoInCollection ->
                    Log.d(TAG, "onCreateView: $photoInCollection")
                    adapter.submitData(photoInCollection)
                }
            }
        }

        viewModel.getCollectionDetails(args.collectionId)

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

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            getString(R.string.collections).uppercase()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}