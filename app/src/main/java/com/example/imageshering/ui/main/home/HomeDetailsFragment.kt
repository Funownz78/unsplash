package com.example.imageshering.ui.main.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.imageshering.App
import com.example.imageshering.R
import com.example.imageshering.databinding.FragmentHomeDetailsBinding
import com.example.imageshering.domain.PhotoDetailed
import com.example.imageshering.works.downloadImage
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeDetailsFragment : Fragment() {
    private var _binding: FragmentHomeDetailsBinding? = null
    private val binding get() = _binding!!
    val args: HomeDetailsFragmentArgs by navArgs()
    private val viewModelFactory = App.daggerComponent.getHomeDetailsViewModelFactory()
    private val viewModel: HomeDetailsViewModel by viewModels { viewModelFactory }
    private var isConnected: Boolean = false
    private val setLike = { id: String, undoCallback: () -> Unit ->
        viewModel.setLike(id, undoCallback)
    }
    private val removeLike = { id: String, undoCallback: () -> Unit ->
        viewModel.removeLike(id, undoCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeDetailsBinding.inflate(inflater)
        binding.back.setOnClickListener { findNavController().navigateUp() }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.photosState.collect {
                    isConnected = it
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.homedetails_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_share -> {
                        val url = Uri.parse(
                            "https://unsplash.com/photos/${args.id}"
                        )
                        val browserIntent = Intent(Intent.ACTION_VIEW, url)
                        startActivity(browserIntent)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.photo.collect { photo ->
                    photo?.let {
                        binding.run {
                            Glide.with(requireContext())
                                .load(photo.urlRegular)
                                .centerCrop()
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .error(R.drawable.baseline_error_24_day)
                                .into(photoView)
                            Glide.with(requireContext())
                                .load(photo.profileImage)
                                .centerCrop()
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .error(R.drawable.baseline_error_24_day)
                                .into(avatarView)
                            usernameView.text = photo.username
                            nameView.text = photo.name
                            likesView.text = photo.likes.toString()
                            location.text = photo.locationName
                            if (photo.locationLatitude != null && photo.locationLongitude != null) {
                                location.setOnClickListener {
                                    val geoUri =
                                        "geo:${photo.locationLatitude},${photo.locationLongitude}"
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(geoUri))
                                    startActivity(intent)
                                }
                            } else if (photo.locationName.isNotEmpty()) {
                                location.setOnClickListener {
                                    val geoUri =
                                        "geo:0,0?q=${photo.locationName}"
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(geoUri))
                                    startActivity(intent)
                                }
                            }
                            tags.text = photo.imageTags
                            brend.text = getString(R.string.brend, photo.exifName)
                            model.text = getString(R.string.model, photo.exifModel)
                            exposure.text = getString(R.string.exposure, photo.exifExposure)
                            aperture.text = getString(R.string.aperture, photo.exifAperture)
                            length.text = getString(R.string.length, photo.exifLength)
                            iso.text = getString(R.string.iso, photo.exifIso)
                            authorName.text = getString(R.string.about, photo.username)
                            authorDescr.text = photo.about
                            downloadCounts.text =
                                getString(R.string.download_with_counts, photo.downloads)
                            downloadCounts.setOnClickListener {

                                viewModel.startDownload(photo.downloadEventUrl)

                                downloadImage(
                                    context = requireContext(),
                                    viewLifecycleOwner = viewLifecycleOwner,
                                    view = binding.root,
                                    url = photo.urlRaw,
                                    id = photo.id
                                )

                            }
                            setLikeState(photo.likedByUser, photo, selfLikeView)
                        }
                    }
                }
            }
        }
        viewModel.getPhoto(args.id)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.ioExceptionChannel.collect {
                    Snackbar.make(
                        binding.root,
                        it,
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.loadingState.collectLatest { loadStates ->
                    val sourceIsRefresh = loadStates == HomeDetailsViewModel.LoadingState.Loading
                    binding.progressCircular.visibility =
                        if (sourceIsRefresh) View.VISIBLE
                        else View.GONE
                }
            }
        }

    }

    private fun setLikeState(state: Boolean, photo: PhotoDetailed, selfLikeView: ImageButton) {
        if (state) {
            selfLikeView.setImageResource(R.drawable.heart_filled)
            selfLikeView.setOnClickListener {
                if (isConnected) {
                    selfLikeView.setImageResource(R.drawable.heart_unfilled)
                    setLikeState(false, photo, selfLikeView)
                    removeLike(photo.id) {
                        selfLikeView.setImageResource(R.drawable.heart_filled)
                        setLikeState(true, photo, selfLikeView)
                    }
                }
            }
        } else {
            selfLikeView.setImageResource(R.drawable.heart_unfilled)
            selfLikeView.setOnClickListener {
                if (isConnected) {
                    selfLikeView.setImageResource(R.drawable.heart_filled)
                    setLikeState(true, photo, selfLikeView)
                    setLike(photo.id) {
                        selfLikeView.setImageResource(R.drawable.heart_unfilled)
                        setLikeState(false, photo, selfLikeView)
                    }
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