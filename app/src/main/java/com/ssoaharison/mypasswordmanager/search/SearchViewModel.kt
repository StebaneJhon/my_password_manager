package com.ssoaharison.mypasswordmanager.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class SearchUiState(
    val items: List<ExternalCredential> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val userMessage: Int? = null
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val detailsRepository: DetailsRepository
): ViewModel() {

    private val _searchQuery: MutableStateFlow<String> = MutableStateFlow("")
    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val _isLoading = MutableStateFlow(false)
    private val _foundDetailsAsync = detailsRepository.observeCredentialsByQuery(_searchQuery.value)
        .map { Async.Success(it) }
        .catch<Async<List<ExternalCredential>>> { emit(Async.Error(R.string.error_no_detail)) }

    val uiState: StateFlow<SearchUiState> = combine(
        _searchQuery, _userMessage, _isLoading, _foundDetailsAsync
    ) { searchQuery, userMessage, isLoading, foundDetailsAsync ->
        when (foundDetailsAsync) {
            Async.Loading -> {
                SearchUiState(isLoading = true)
            }
            is Async.Error -> {
                SearchUiState(userMessage = foundDetailsAsync.errorMessage)
            }
            is Async.Success -> {
                SearchUiState(
                    items = foundDetailsAsync.data,
                    searchQuery = searchQuery,
                    isLoading = isLoading,
                    userMessage = userMessage
                )
            }
        }
    } .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = SearchUiState(isLoading = false)
    )

    fun onSearchQueryChange(newSearchQuery: String) {
        _searchQuery.update {
            newSearchQuery
        }
    }

    fun snackbarMessageShown() {
        _userMessage.value = null
    }

    private fun showSnackbarMessage(message: Int) {
        _userMessage.value = message
    }

}

















