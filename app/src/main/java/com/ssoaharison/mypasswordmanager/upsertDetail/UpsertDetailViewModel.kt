package com.ssoaharison.mypasswordmanager.upsertDetail

import androidx.lifecycle.SavedStateHandle
import com.ssoaharison.mypasswordmanager.data.DetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class UpsertTaskUiState(
    val appName: String = "",
    val link: String = "",
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val userMessage: Int? = null,
    val isDetailSaved: Boolean = false
)

@HiltViewModel
class UpsertDetailViewModel @Inject constructor(
    private val detailsRepository: DetailsRepository,
    savedStateHandle: SavedStateHandle
) {
}