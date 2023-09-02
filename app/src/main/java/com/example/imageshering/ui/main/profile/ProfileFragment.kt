package com.example.imageshering.ui.main.profile

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.imageshering.App
import com.example.imageshering.R
import com.example.imageshering.data.AppAuthComponent
import com.example.imageshering.databinding.FragmentProfileBinding
import com.example.imageshering.ui.FlatPhotosAdapter
import com.example.imageshering.ui.onboarding.OnBoardingActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val profileViewModelFactory = App.daggerComponent.getProfileViewModelFactory()
    private val viewModel: ProfileViewModel by viewModels { profileViewModelFactory }
    private lateinit var appAuthComponent: AppAuthComponent
    private val setLike = { id: String, undoCallback: () -> Unit ->
        viewModel.setLike(id, undoCallback)
    }
    private val removeLike = { id: String, undoCallback: () -> Unit ->
        viewModel.removeLike(id, undoCallback)
    }

    private lateinit var menuHost: MenuHost
    private lateinit var adapter: FlatPhotosAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        appAuthComponent = App.daggerComponent.getAppAuthComponent()
        if (!appAuthComponent.isAuthorized) {
            startActivity(Intent(activity, OnBoardingActivity::class.java))
        }
        _binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FlatPhotosAdapter(
            isConnected = { true },
            setLike = setLike,
            removeLike = removeLike,
            transitionToDetails = { }
        )
        binding.profileRecyclerView.adapter = adapter


        menuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.profile_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_logout -> {

                        val listener = DialogInterface.OnClickListener { _, which ->
                            when (which) {
                                DialogInterface.BUTTON_POSITIVE -> {
                                    val endSessionIntent = appAuthComponent.getLogoutRequest()
                                    startActivity(endSessionIntent)
                                }
                                else -> {}
                            }
                        }
                        val dialog = AlertDialog.Builder(requireContext())
                            .setCancelable(true)
                            .setTitle(R.string.logout_alert_title)
                            .setMessage(R.string.logout_alert_message)
                            .setPositiveButton(R.string.action_yes, listener)
                            .setNegativeButton(R.string.action_no, listener)
                            .create()

                        dialog.show()

                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.me.collect { self ->
                    binding.apply {
                        val avatarUrl = self?.profileImageUrl
                        if (avatarUrl != null) {
                            Glide.with(requireContext())
                                .load(avatarUrl)
                                .centerCrop()
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .error(R.drawable.baseline_error_24_day)
                                .into(profileAvatarView)
                        } else {
                            profileAvatarView.visibility = View.GONE
                        }
                        profileName.text = self?.name ?: ""
                        profileUsername.text = self?.username ?: ""
                        profileAbout.text = self?.bio ?: ""
                        profileLocation.text =
                            getString(R.string.profile_padding_string, self?.location)
                        self?.location.let {
                            profileLocation.setOnClickListener {
                                val geoUri =
                                    "geo:0,0?q=$it"
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(geoUri))
                                startActivity(intent)
                            }
                        }
                        profileEmail.text = getString(R.string.profile_padding_string, self?.email)
                        profileDownloads.text =
                            getString(R.string.profile_padding_string, self?.downloads.toString())

                    }
                }

            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.likedPhotos.collect {
                    adapter.submitList(it)
                }
            }
        }

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
                    val sourceIsRefresh = loadStates == ProfileViewModel.LoadingState.Loading
                    binding.progressCircular.visibility =
                        if (sourceIsRefresh) View.VISIBLE
                        else View.GONE
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            getString(R.string.profile).uppercase()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshLikedPhoto()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}