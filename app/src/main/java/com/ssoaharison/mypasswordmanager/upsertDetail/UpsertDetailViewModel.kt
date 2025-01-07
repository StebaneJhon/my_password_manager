package com.ssoaharison.mypasswordmanager.upsertDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssoaharison.mypasswordmanager.MyPasswordManagerDestinationsArgs
import com.ssoaharison.mypasswordmanager.R
import com.ssoaharison.mypasswordmanager.data.DetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UpsertDetailUiState(
    val appName: String = "",
    val link: String = "",
    val username: String = "",
    val password: String = "",
    val viewCount: Int = 0,
    val isLoading: Boolean = false,
    val userMessage: Int? = null,
    val isDetailSaved: Boolean = false
)

@HiltViewModel
class UpsertDetailViewModel @Inject constructor(
    private val detailsRepository: DetailsRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val detailId: String? = savedStateHandle[MyPasswordManagerDestinationsArgs.DETAIL_ID_ARG]
    private val _uiState = MutableStateFlow(UpsertDetailUiState())
    val uiState: StateFlow<UpsertDetailUiState> = _uiState.asStateFlow()

    init {
        if (detailId != null) {
            loadDetail(detailId)
        }
    }

    fun saveDetail() {
        if (uiState.value.appName.isEmpty() || uiState.value.username.isEmpty() || uiState.value.password.isEmpty()) {
            _uiState.update {
                it.copy(userMessage = R.string.error_missing_detail_content)
            }
            return
        }
        if (detailId == null) {
            createNewDetail()
        } else {
            updateDetail()
        }
    }

    fun snackbarMessageShown() {
        _uiState.update {
            it.copy(userMessage = null)
        }
    }

    fun updatePassword(newPassword: String) {
        _uiState.update {
            it.copy(password = newPassword)
        }
    }

    fun updateUsername(newUsername: String) {
        _uiState.update {
            it.copy(username = newUsername)
        }
    }

    fun updateLink(newLink: String) {
        _uiState.update {
            it.copy(link = newLink)
        }
    }

    fun updateAppName(newAppName: String) {
        _uiState.update {
            it.copy(appName = newAppName)
        }
    }

    private fun updateDetail() {
        if (detailId == null) {
            throw RuntimeException("updateDetail() was called but detail is new.")
        }
        viewModelScope.launch {
            detailsRepository.updateCredential(
                detailId,
                appName = uiState.value.appName,
                link = uiState.value.link,
                username = uiState.value.username,
                password = uiState.value.password,
                viewCount = uiState.value.viewCount
            )
            _uiState.update {
                it.copy(isDetailSaved = true)
            }
        }
    }

    private fun createNewDetail() = viewModelScope.launch {
        detailsRepository.createCredential(uiState.value.appName, uiState.value.link, uiState.value.username, uiState.value.password)
        _uiState.update {
            it.copy(isDetailSaved = true)
        }
    }

    private fun loadDetail(detailId: String) {
        _uiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            detailsRepository.getCredential(detailId).let { credential ->
                if (credential != null) {
                    _uiState.update {
                        it.copy(
                            appName = credential.appName,
                            link = credential.link,
                            username = credential.username,
                            password = credential.password,
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(isLoading = false)
                    }
                }
            }
        }
    }

}