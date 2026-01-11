package com.haykor.fridge.feature.home.presentation.screens.fridges

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haykor.fridge.core.data.remote.models.FridgesDto
import com.haykor.fridge.core.data.remote.models.Result
import com.haykor.fridge.feature.home.data.FridgesRepository
import com.haykor.fridge.feature.home.domain.FridgesUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FridgesListViewModel @Inject constructor(
    private val fridgesRepository: FridgesRepository
) : ViewModel() {

    private val _state = MutableStateFlow<FridgesListState>(FridgesListState.Loading)
    val state = _state.asStateFlow()

    init {
        fetch()
    }

    fun fetch() {
        viewModelScope.launch {
            _state.value = FridgesListState.Loading

            delay(3000L)

            val result = fridgesRepository.getFridges()
            when (result) {
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


fun FridgesDto.toUi(): FridgesUi {
    return FridgesUi(
        id = id,
        name = name
    )
}