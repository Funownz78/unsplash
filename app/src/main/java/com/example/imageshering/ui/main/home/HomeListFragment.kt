package com.example.imageshering.ui.main.home

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.imageshering.App
import com.example.imageshering.R
import com.example.imageshering.databinding.FragmentHomeListBinding
import com.example.imageshering.ui.main.GridSpacingItemDecoration
import com.example.imageshering.ui.main.MainActivity
import com.example.imageshering.ui.main.PhotosAdapter
import com.example.imageshering.ui.main.PhotosLoadStateAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "HomeListFragment"

class HomeListFragment : Fragment() {
    private var _binding: FragmentHomeListBinding? = null
    private val binding get() = _binding!!
    private val homeViewModelFactory = App.daggerComponent.getHomeViewModelFactory()
    private val viewModel: HomeListViewModel by viewModels { homeViewModelFactory }
    private val transitionToDetails = { id: String ->
        val action = HomeListFragmentDirections
            .actionHomeListFragmentToHomeDetailsFragment(id)
        findNavController().navigate(action)
    }
    private val setLike = { id: String, undoCallback: () -> Unit ->
        viewModel.setLike(id, undoCallback)
    }
    private val removeLike = { id: String, undoCallback: () -> Unit ->
        viewModel.removeLike(id, undoCallback)
    }
    private var isConnected: Boolean = false
    private lateinit var adapter: PhotosAdapter
    private var menuHost: MenuHost? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeListBinding.inflate(inflater)
        val viewIntentId = (requireActivity() as MainActivity).viewIntentId
        viewIntentId?.let {
            val action = HomeListFragmentDirections
                .actionHomeListFragmentToHomeDetailsFragment(it)
            findNavController().navigate(action)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.grid_spacing)
        binding.photosRecyclerView.addItemDecoration(GridSpacingItemDecoration(spacingInPixels))

        adapter = PhotosAdapter(
            isConnected = { isConnected },
            setLike = setLike,
            removeLike = removeLike,
            transitionToDetails = transitionToDetails
        )
        binding.photosRecyclerView.adapter =
            adapter.withLoadStateFooter(PhotosLoadStateAdapter { adapter.retry() })

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.pagedPhotos.collect {
                    adapter.submitData(it)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.photosState.collect {
                    isConnected = it
                    binding.recyclerTitle.text = if (it) getString(R.string.today_with_api)
                    else getString(R.string.today_without_api)
                    attachMenu()
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

    private fun attachMenu() {
        Log.d(TAG, "attachMenu , isConnectred: $isConnected")
        Log.d(TAG, "attachMenu , ::menuHost.isInitialized: ${menuHost == null}")
        if (isConnected && menuHost == null) {
            menuHost = requireActivity()
            menuHost?.addMenuProvider(object : MenuProvider {
                private lateinit var searchItem: MenuItem
                private lateinit var searchView: SearchView
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.homelist_menu, menu)

                    searchItem = menu.findItem(R.id.action_search)
                    searchView = searchItem.actionView as SearchView
                    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            query?.let {
                                searchView.clearFocus()
                                val action = HomeListFragmentDirections
                                    .actionHomeListFragmentToSearchPhotoFragment(it)
                                findNavController().navigate(action)
                            }
                            return true
                        }

                        override fun onQueryTextChange(newText: String?): Boolean = true
                    })
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean = true
            }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        }
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            getString(R.string.home).uppercase()
        menuHost = null
        attachMenu()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}