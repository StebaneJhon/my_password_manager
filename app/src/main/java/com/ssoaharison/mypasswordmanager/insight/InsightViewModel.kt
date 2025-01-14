package com.ssoaharison.mypasswordmanager.insight

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssoaharison.mypasswordmanager.R
import com.ssoaharison.mypasswordmanager.data.DetailsRepository
import com.ssoaharison.mypasswordmanager.data.ExternalCredential
import com.ssoaharison.mypasswordmanager.details.DetailsUiState
import com.ssoaharison.mypasswordmanager.util.Async
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class InsightUiState(
    val strongPasswords: List<ExternalCredential> = emptyList(),
    val weakPasswords: List<ExternalCredential> = emptyList(),
    val duplicatedPasswords: List<ExternalCredential> = emptyList(),
    val isLoading: Boolean = false,
    val userMessage: Int? = null
)

@HiltViewModel
class InsightViewModel @Inject constructor(
    private val detailsRepository: DetailsRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val _weakPasswordsAsync = detailsRepository.getCredentialsWithWeakPasswordStream()
        .map { Async.Success(it) }
        .catch<Async<List<ExternalCredential>>> { emit(Async.Error(R.string.error_loading_detail)) }
    private val _strongPasswordsAsync = detailsRepository.getCredentialsWithStrongPasswordStream()
        .map { Async.Success(it) }
        .catch<Async<List<ExternalCredential>>> { emit(Async.Error(R.string.error_loading_detail)) }

    val uiState: StateFlow<InsightUiState> = combine(
        _isLoading, _userMessage, _strongPasswordsAsync, _weakPasswordsAsync
    ) {isLoading, userMessage, weakPassword, strongPassword ->
        if (weakPassword is Async.Loading || strongPassword is Async.Loading) {
            InsightUiState(isLoading = true)
        } else if (weakPassword is Async.Error || strongPassword is Async.Error) {
            InsightUiState(userMessage = (strongPassword as Async.Error).errorMessage)
        } else {
            InsightUiState(
                strongPasswords = (strongPassword as Async.Success).data,
                weakPasswords = (weakPassword as Async.Success).data,
                isLoading = isLoading,
                userMessage = userMessage
            )
        }
    } .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = InsightUiState(isLoading = true)
    )

}