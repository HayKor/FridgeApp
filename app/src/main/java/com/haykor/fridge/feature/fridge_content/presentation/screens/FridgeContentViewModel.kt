package com.haykor.fridge.feature.fridge_content.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haykor.fridge.core.data.remote.models.FridgeProductsFilters
import com.haykor.fridge.core.data.remote.models.Result
import com.haykor.fridge.feature.fridge_content.data.FridgeProductsRepository
import com.haykor.fridge.feature.fridge_content.domain.FridgeProductUi
import com.haykor.fridge.feature.fridge_content.domain.toUi
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = FridgeContentViewModel.Factory::class)
class FridgeContentViewModel @AssistedInject constructor(
    private val fridgeProductsRepository: FridgeProductsRepository,
    @Assisted private val fridgeId: Int
) : ViewModel() {

    private val _state = MutableStateFlow(FridgeContentState())
    val state = _state.asStateFlow()

    init {
        fetchFridgeProducts()
    }

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
            val result = fridgeProductsRepository.deleteFridgeProduct(item.id)
            when (result) {
                is Result.Error -> {
                    _state.update { it.copy(error = "Couldn't delete product") }
                }

                is Result.Success -> {
                    _state.update {
                        it.copy(
                            items = _state.value.items.filter { product -> product != item }
                        )
                    }
                }
            }
        }
    }

    fun fetchFridgeProducts() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true
            )

            val response =
                fridgeProductsRepository.getFridgeProducts(FridgeProductsFilters(fridgeId = fridgeId))
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

    @AssistedFactory
    interface Factory {
        fun create(fridgeId: Int): FridgeContentViewModel
    }
}

data class FridgeContentState(
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
