package com.haykor.fridge.auth.presentation.screens.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haykor.fridge.auth.data.AuthRepository
import com.haykor.fridge.core.data.remote.models.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RegistrationState())
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<RegistrationEvent>()
    val events = _events.asSharedFlow()

    fun onUsernameChange(username: String) {
        _state.value = _state.value.copy(error = null, username = username)
    }
    fun onEmailChange(email: String) {
        _state.value = _state.value.copy(error = null, email = email)
    }

    fun onPasswordChange(password: String) {
        _state.value = _state.value.copy(error = null, password = password)
    }

    fun onPasswordRepeatChange(passwordRepeat: String) {
        _state.value = _state.value.copy(error = null, passwordRepeat = passwordRepeat)
    }

    fun register() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            val result = authRepository.register(
                username = _state.value.username,
                email = _state.value.email,
                password = _state.value.password
            )
            when (result) {
                is Result.Success -> {
                    _events.emit(RegistrationEvent.RegistrationSuccess)
                }

                is Result.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.msg
                    )
                }
            }
        }
    }
}

data class RegistrationState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val passwordRepeat: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class RegistrationEvent {
    object RegistrationSuccess : RegistrationEvent()
}