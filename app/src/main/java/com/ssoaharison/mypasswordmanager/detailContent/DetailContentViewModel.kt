package com.ssoaharison.mypasswordmanager.detailContent

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssoaharison.mypasswordmanager.MyPasswordManagerDestinationsArgs
import com.ssoaharison.mypasswordmanager.R
import com.ssoaharison.mypasswordmanager.data.DetailsRepository
import com.ssoaharison.mypasswordmanager.data.ExternalCredential
import com.ssoaharison.mypasswordmanager.util.Async
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailContentUiState(
    val detail: ExternalCredential? = null,
    val isLoading: Boolean = false,
    val userMessage: Int? = null,
    val isDetailDeleted: Boolean = false
)

@HiltViewModel
class DetailContentViewModel @Inject constructor(
    private val detailsRepository: DetailsRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    val detailId: String = savedStateHandle[MyPasswordManagerDestinationsArgs.DETAIL_ID_ARG]!!

    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val _isLoading = MutableStateFlow(false)
    private val _isDetailDeleted = MutableStateFlow(false)
    private val _detailAsync = detailsRepository.getCredentialStream(detailId)
        .map { handleDetail(it) }
        .catch { emit(Async.Error(R.string.error_loading_detail)) }

    val uiState: StateFlow<DetailContentUiState> = combine(
        _userMessage, _isLoading, _isDetailDeleted, _detailAsync
    ) { userMessage, isLoading, isDetailDeleted, detailAsync ->
        when (detailAsync) {
            Async.Loading -> {
                DetailContentUiState(isLoading = true)
            }
            is Async.Error -> {
                DetailContentUiState(
                    userMessage = detailAsync.errorMessage,
                    isDetailDeleted = isDetailDeleted
                )
            }
            is Async.Success -> {
                DetailContentUiState(
                    detail = detailAsync.data,
                    isLoading = isLoading,
                    userMessage = userMessage,
                    isDetailDeleted = isDetailDeleted
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = DetailContentUiState(isLoading = true)
    )

    fun deleteDetail() = viewModelScope.launch {
        detailsRepository.deleteCredential(detailId)
        _isDetailDeleted.value = true
    }

    fun snackbarMessageShown() {
        _userMessage.value = null
    }

    private fun showSnackbarMessage(message: Int) {
        _userMessage.value = message
    }

    private fun handleDetail(detail: ExternalCredential?): Async<ExternalCredential> {
        if (detail == null) {
            return Async.Error(R.string.error_detail_not_found)
        }
        return Async.Success(detail)
    }

}