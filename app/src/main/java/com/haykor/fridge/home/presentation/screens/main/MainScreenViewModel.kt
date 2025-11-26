package com.haykor.fridge.home.presentation.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haykor.fridge.core.data.remote.models.FridgeProductsResponse
import com.haykor.fridge.core.data.remote.models.Result
import com.haykor.fridge.home.data.FridgeProductsRepository
import com.haykor.fridge.home.domain.FridgeProductUi
import com.haykor.fridge.home.domain.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val fridgeProductsRepository: FridgeProductsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MainScreenState())
    val state = _state.asStateFlow()

    fun onProductNameFilterChange(value: String) {
        _state.value = _state.value.copy(
            productNameFilter = value
        )
    }

    fun onFilterTypeChange(value: FilterType) {
        _state.value = _state.value.copy(
            filterType = value,
            items = _state.value.items.sortFridgeProducts(value)
        )
    }

    fun onFridgeProductDelete(item: FridgeProductUi) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                items = _state.value.items.filter { it != item }
            )
        }
    }

    fun fetchFridgeProducts() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true
            )
//            val response = fridgeProductsRepository.getFridgeProducts(
//                FridgeProductsFilters(productName = _state.value.productNameFilter)
//            )
            val response =
                Result.Success(data = FridgeProductsResponse(fridgeProductsListStub(), 1, 1))
            when (response) {
                is Result.Success -> {
                    _state.value = _state.value.copy(
                        items = response.data.items.map { it.toUi() }
                            .sortFridgeProducts(_state.value.filterType),
                        isLoading = false
                    )
                }

                is Result.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "При загрузке данных что-то пошло не так"
                    )
                }
            }
        }
    }

    private fun List<FridgeProductUi>.sortFridgeProducts(filterType: FilterType): List<FridgeProductUi> {
        return when (filterType) {
            FilterType.NAME -> this.sortedBy { it.name }
            FilterType.EXPIRY -> this.sortedBy { it.daysLeft }
        }
    }
}

data class MainScreenState(
    val items: List<FridgeProductUi> = emptyList(),
    val productNameFilter: String = "",
    val filterType: FilterType = FilterType.NAME,
    val isLoading: Boolean = false,
    val error: String? = null
)

enum class FilterType(val displayName: String) {
    NAME("Название"),
    EXPIRY("Срок годности");

    override fun toString() = displayName
}
