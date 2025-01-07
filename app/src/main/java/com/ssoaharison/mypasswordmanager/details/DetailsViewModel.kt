package com.ssoaharison.mypasswordmanager.details


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssoaharison.mypasswordmanager.DELETE_RESULT_OK
import com.ssoaharison.mypasswordmanager.EDIT_RESULT_OK
import com.ssoaharison.mypasswordmanager.R
import com.ssoaharison.mypasswordmanager.UPSERT_RESULT_OK
import com.ssoaharison.mypasswordmanager.data.DetailsRepository
import com.ssoaharison.mypasswordmanager.data.ExternalCredential
import com.ssoaharison.mypasswordmanager.util.Async
import com.ssoaharison.mypasswordmanager.util.DetailFilterModel
import com.ssoaharison.mypasswordmanager.util.DetailType
import com.ssoaharison.mypasswordmanager.util.DetailType.ADDRESS
import com.ssoaharison.mypasswordmanager.util.DetailType.BIRTHDAY
import com.ssoaharison.mypasswordmanager.util.DetailType.CREDENTIALS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class DetailsUiState(
    val items: List<ExternalCredential> = emptyList(),
    val isLoading: Boolean = false,
    val filteringUiInfo: List<DetailFilterModel> = emptyList(),
    val userMessage: Int? = null
)

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val detailsRepository: DetailsRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _savedFilterType = savedStateHandle.getStateFlow(
        DETAILS_FILTER_SAVED_STATE_KEY,
        DetailType.CREDENTIALS
    )

    private val _filterUiInfo = _savedFilterType.map { generateFilterModels(it) }.distinctUntilChanged()
    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val _isLoading = MutableStateFlow(false)
    private val _filteredDetailsAsync = detailsRepository.getCredentialsStream()
        .map { Async.Success(it) }
        .catch<Async<List<ExternalCredential>>> { emit(Async.Error(R.string.error_loading_detail)) }

    val uiState: StateFlow<DetailsUiState> = combine(
        _filterUiInfo, _isLoading, _userMessage, _filteredDetailsAsync
    ) { filterUiInfo, isLoading, userMessage, detailsAsync ->
        when (detailsAsync) {
            Async.Loading -> {
                DetailsUiState(isLoading = true)
            }
            is Async.Error -> {
                DetailsUiState(userMessage = detailsAsync.errorMessage)
            }
            is Async.Success -> {
                DetailsUiState(
                    items = detailsAsync.data,
                    filteringUiInfo = filterUiInfo,
                    isLoading = isLoading,
                    userMessage = userMessage
                )
            }
        }
    } .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = DetailsUiState(isLoading = true)
    )

    fun setFiltering(requestType: DetailType) {
        savedStateHandle[DETAILS_FILTER_SAVED_STATE_KEY] = requestType
    }

    fun showUpsertResultMessage(result: Int) {
        when(result) {
            EDIT_RESULT_OK -> showSnackbarMessage(R.string.message_detail_saved_successfully)
            UPSERT_RESULT_OK -> showSnackbarMessage(R.string.message_detail_added_successfully)
            DELETE_RESULT_OK -> showSnackbarMessage(R.string.message_detail_deleted_successfully)
        }
    }

    fun snackbarMessageShown() {
        _userMessage.value = null
    }

    private fun showSnackbarMessage(message: Int) {
        _userMessage.value = message
    }

    private fun generateFilterModels(requestType: DetailType): List<DetailFilterModel> {
        val result = mutableListOf<DetailFilterModel>()
        getDetailTypes().forEach { type ->
            val isTypeSelected = type == requestType
            result.add(DetailFilterModel(type, type.getTypeName(), isTypeSelected))
        }
        return result
    }

}

const val DETAILS_FILTER_SAVED_STATE_KEY = "DETAILS_FILTER_SAVED_STATE_KEY"

fun getDetailTypes() = listOf(CREDENTIALS, ADDRESS, BIRTHDAY)














