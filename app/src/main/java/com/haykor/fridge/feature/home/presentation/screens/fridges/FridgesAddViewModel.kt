package com.haykor.fridge.feature.home.presentation.screens.fridges

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haykor.fridge.core.data.remote.models.CreateFridgeRequest
import com.haykor.fridge.core.data.remote.models.Result
import com.haykor.fridge.feature.home.data.FridgesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FridgesAddViewModel @Inject constructor(
    private val fridgesRepository: FridgesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(FridgesAddState())
    val state = _state.asStateFlow()

    private val _channel = Channel<FridgesAddEvent>()
    val channel = _channel.receiveAsFlow()

    fun onNameChange(name: String) {
        _state.value = _state.value.copy(name = name)
    }

    fun createFridge() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            val result = fridgesRepository.createFridge(
                CreateFridgeRequest(
                    name = _state.value.name
                )
            )

            when (result) {
                is Result.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                    )
                    _channel.send(FridgesAddEvent.AddError("Какай-та ошибк"))
                }

                is Result.Success -> {
                    _channel.send(FridgesAddEvent.AddSuccess)
                }
            }
        }
    }
}

data class FridgesAddState(
    val name: String = "",
    val isLoading: Boolean = false
)

sealed class FridgesAddEvent {
    object AddSuccess : FridgesAddEvent()
    data class AddError(val msg: String) : FridgesAddEvent()
}