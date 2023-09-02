package com.example.imageshering.ui.onboarding

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.imageshering.App
import com.example.imageshering.data.AppAuthComponent
import com.example.imageshering.databinding.FragmentLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val CLIENT_ID = "w_Ow_7HSlr-d3NDDN2LSAG2pi-hzbMQ5TGaFviEFOIw"
private const val CLIENT_SECRET = "R2UXAzS84cEezaOnGZ3-F96xA0iqmh5xU9BVdoyirR4"

class LoginFragment() : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private lateinit var appAuthComponent: AppAuthComponent
    private val binding get() = _binding!!
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater)
        appAuthComponent = App.daggerComponent.getAppAuthComponent()
        val authState = appAuthComponent.authState
        binding.loginButton.setOnClickListener {
            scope.launch {
                activity?.application?.let {
                    Log.d("RDA", "accessToken: ${authState.accessToken}")
                    Log.d("RDA", "idToken: ${authState.idToken}")
                    Log.d(
                        "RDA",
                        "accessTokenExpirationTime: ${authState.accessTokenExpirationTime}"
                    )
                    Log.d("RDA", "isAuthorized: ${authState.isAuthorized}")
                    Log.d("RDA", "needsTokenRefresh: ${authState.needsTokenRefresh}")
                    appAuthComponent.initAuthorization()
                }
//                val authorizationServiceConfiguration = AuthorizationServiceConfiguration(
//                    Uri.parse("https://unsplash.com/oauth/authorize"),
//                    Uri.parse("https://unsplash.com/oauth/token")
//                )
////                val authState = AuthState(authorizationServiceConfiguration)
//                val authorizationRequest = AuthorizationRequest.Builder(
//                    authorizationServiceConfiguration,
//                    CLIENT_ID,
//                    ResponseTypeValues.CODE,
//                    Uri.parse("com.example.imageshering:/oauth2redirect")
//                )
//                    .setScope("public read_user write_user read_photos write_photos write_likes write_followers read_collections write_collections")
//                    .setPrompt("select_account")
//                    .build()
//                val authorizationService = AuthorizationService(requireContext())
//                var pendingIntentFlags = 0
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//                    pendingIntentFlags = PendingIntent.FLAG_MUTABLE
//                }
//                authorizationService.performAuthorizationRequest(
//                    authorizationRequest,
//                    PendingIntent.getActivity(
//                        requireContext(),
//                        0,
//                        Intent(requireContext(), MainActivity::class.java).apply {
//                            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_TASK
//                        },
//                        pendingIntentFlags
//                    )
//                )
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}