package com.example.imageshering.data

import android.app.PendingIntent
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import com.example.imageshering.BuildConfig
import com.example.imageshering.ui.main.MainActivity
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.openid.appauth.*

private const val TAG = "RDA"
class AppAuthComponent (private val context: Context) {
    private val scope = CoroutineScope(Dispatchers.IO + CoroutineName(SCOPE_NAME))
    private val authorizationServiceConfiguration = AuthorizationServiceConfiguration(
        AUTHORIZATION_ENDPOINT,
        TOKEN_ENDPOINT,
        null,
        LOGOUT_ENDPOINT
    )
    var authState = AuthState(authorizationServiceConfiguration)
    private val authorizationRequest = AuthorizationRequest.Builder(
        authorizationServiceConfiguration,
        BuildConfig.UNSPLASH_ID,
        ResponseTypeValues.CODE,
        POST_AUTHORIZATION_REDIRECT
    )
        .setScope(REQUIRED_SCOPES)
        .setPrompt(PROMPT_POLICE)
        .build()
    private var authorizationService: AuthorizationService = AuthorizationService(context)
    var accessToken: String? = readAccessTokenState()
        private set(value) {
            field = value
            Log.d(TAG, "setter value: $value")
            if (value != null) {
                val intent = Intent(context, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(
                    context,
                    intent,
                    bundleOf()
                )
            }
        }
    val isAuthorized get() = accessToken != null
    private var pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
        PendingIntent.FLAG_MUTABLE else 0

    fun initAuthorization() {
        scope.launch {
            authorizationService.performAuthorizationRequest(
                authorizationRequest,
                PendingIntent.getActivity(
                    context,
                    0,
                    Intent(context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_TASK
                    },
                    pendingIntentFlags
                )
            )
        }
    }

    private fun readAccessTokenState(): String? {
        val sP = context.getSharedPreferences("auth", MODE_PRIVATE)
        val str = sP.getString("accessToken", null)
        Log.d("RDA", "readAccessTokenState: $str")
        return str
    }
//    private fun readAccessTokenState(): String? =
//        context.getSharedPreferences("auth", MODE_PRIVATE).getString("accessToken", null).also {
//            Log.d("RDA", "readAccessTokenState: $it")
//        }

    private fun writeAccessTokenState(accessToken: String?) {
        Log.d("RDA", "writeAccessTokenState: $accessToken")
        val authPrefs: SharedPreferences = context.getSharedPreferences("auth", MODE_PRIVATE)
        val editor = authPrefs.edit()
        editor.putString("accessToken", accessToken)
        editor.apply()
        this.accessToken = accessToken
    }

    private fun update(response: AuthorizationResponse?, ex: AuthorizationException?) {
        authState.update(response, ex)
        writeAccessTokenState(authState.accessToken)
    }
    private fun update(response: TokenResponse?, ex: AuthorizationException?) {
        authState.update(response, ex)
        writeAccessTokenState(authState.accessToken)
    }

    fun getLogoutRequest(): Intent {
        val endSessionRequest = EndSessionRequest.Builder(authorizationServiceConfiguration)
            .setPostLogoutRedirectUri(POST_LOGOUT_REDIRECT)
            .build()
        val endSessionIntent = authorizationService.getEndSessionRequestIntent(endSessionRequest)
//        authState = AuthState(authorizationServiceConfiguration)
        Log.d("RDA", "getLogoutRequest: ${authState.accessToken}")
//        writeAccessTokenState(authState.accessToken)
        accessToken = null
        return endSessionIntent
    }

    fun performAccessTokenRequest(
        authorizationResponse: AuthorizationResponse,
        authorizationException: AuthorizationException?,
        failureCallback: ()->Unit
    ) {
        update(authorizationResponse, authorizationException)
        authorizationService.performTokenRequest(
            authorizationResponse.createTokenExchangeRequest(),
            ClientSecretPost(BuildConfig.UNSPLASH_SECRET)
        ) { response, ex ->
            if (response != null) {
                update(response, ex)
                Log.d("RDA", "performAccessTokenRequest: ${authState.accessToken}")
            } else {
                failureCallback.invoke()
            }
        }
    }

    companion object {
        private const val SCOPE_NAME = "AppAuthScope"
        private val AUTHORIZATION_ENDPOINT = "https://unsplash.com/oauth/authorize".toUri()
        private val TOKEN_ENDPOINT = "https://unsplash.com/oauth/token".toUri()
        private val LOGOUT_ENDPOINT = "https://unsplash.com/logout".toUri()
        private val POST_AUTHORIZATION_REDIRECT = "com.example.imageshering:/oauth2redirect".toUri()
        private val POST_LOGOUT_REDIRECT = "com.example.imageshering:/logout".toUri()
        private const val REQUIRED_SCOPES = "public read_user write_user read_photos write_photos write_likes write_followers read_collections write_collections"
        private const val PROMPT_POLICE = "select_account"
    }
}