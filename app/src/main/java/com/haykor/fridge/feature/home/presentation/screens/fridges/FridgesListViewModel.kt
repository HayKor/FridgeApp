package com.haykor.fridge.feature.home.presentation.screens.fridges

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haykor.fridge.core.data.remote.models.Result
import com.haykor.fridge.feature.home.data.FridgesRepository
import com.haykor.fridge.feature.home.domain.FridgesUi
import com.haykor.fridge.feature.home.domain.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FridgesListViewModel @Inject constructor(
    private val fridgesRepository: FridgesRepository
) : ViewModel() {

    private val _state = MutableStateFlow<FridgesListState>(FridgesListState.Loading)
    val state = _state.asStateFlow()
    private val _events = MutableSharedFlow<FridgesEvent>()
    val events = _events.asSharedFlow()

    init {
        fetch()
    }

    // TODO: make it return boolean so UI can обработать сучк
    fun onFridgeDelete(fridge: FridgesUi) {
        viewModelScope.launch {
            val result = fridgesRepository.deleteFridge(fridgeId = fridge.id)
            when (result) {
                is Result.Error -> {
                    _events.emit(FridgesEvent.Error)
                }

                is Result.Success -> {
                    updateList(fridge)
                }
            }
        }
    }

    private fun updateList(fridge: FridgesUi) {
        val newList =
            (_state.value as FridgesListState.Displaying).fridgesList.filter { it != fridge }
        if (newList.isEmpty()) {
            _state.value = FridgesListState.DisplayingEmpty
        } else {
            _state.update {
                FridgesListState.Displaying(
                    fridgesList = newList
                )
            }
        }
    }

    fun fetch() {
        viewModelScope.launch {
            _state.value = FridgesListState.Loading

            when (val result = fridgesRepository.getFridges()) {
                is Result.Error -> {
                    _state.value = FridgesListState.Error("Что-то пошло не так")
                }

                is Result.Success -> {
                    if (result.data.isEmpty()) {
                        _state.value = FridgesListState.DisplayingEmpty
                    } else {
                        _state.value = FridgesListState.Displaying(
                            fridgesList = result.data.map { it.toUi() }
                        )
                    }
                }
            }
        }
    }
}

sealed class FridgesListState {
    object Loading : FridgesListState()

    data class Error(val msg: String) : FridgesListState()

    data class Displaying(
        val fridgesList: List<FridgesUi> = listOf()
    ) : FridgesListState()

    object DisplayingEmpty : FridgesListState()
}

sealed class FridgesEvent {
    object Error : FridgesEvent()
}