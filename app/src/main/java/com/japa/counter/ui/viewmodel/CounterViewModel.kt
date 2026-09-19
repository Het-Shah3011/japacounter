package com.japa.counter.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.japa.counter.data.entity.DeityCounterEntity
import com.japa.counter.data.repository.DeityRepository
import com.japa.counter.utils.VibrationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CounterViewModel(
    private val repository: DeityRepository,
    private val vibrationHelper: VibrationHelper
) : ViewModel() {

    private val _activeDeity = MutableStateFlow<DeityCounterEntity?>(null)
    val activeDeity: StateFlow<DeityCounterEntity?> = _activeDeity.asStateFlow()

    private val _allDeities = MutableStateFlow<List<DeityCounterEntity>>(emptyList())
    val allDeities: StateFlow<List<DeityCounterEntity>> = _allDeities.asStateFlow()

    private val _showResetDialog = MutableStateFlow(false)
    val showResetDialog: StateFlow<Boolean> = _showResetDialog.asStateFlow()

    private val _showDeleteDialog = MutableStateFlow<DeityCounterEntity?>(null)
    val showDeleteDialog: StateFlow<DeityCounterEntity?> = _showDeleteDialog.asStateFlow()

    private val _showAddEditDialog = MutableStateFlow<DeityCounterEntity?>(null)
    val showAddEditDialog: StateFlow<DeityCounterEntity?> = _showAddEditDialog.asStateFlow()

    private val _showEditCountDialog = MutableStateFlow<DeityCounterEntity?>(null)
    val showEditCountDialog: StateFlow<DeityCounterEntity?> = _showEditCountDialog.asStateFlow()

    init {
        viewModelScope.launch {
            repository.activeDeity.collect { deity ->
                _activeDeity.value = deity
            }
        }
        viewModelScope.launch {
            repository.allDeities.collect { deities ->
                _allDeities.value = deities
            }
        }
    }

    fun incrementCount() {
        viewModelScope.launch {
            val deity = _activeDeity.value ?: return@launch
            val newCount = deity.currentCount + 1
            val newMalas = newCount / 108
            val updatedDeity = deity.copy(
                currentCount = newCount,
                completedMalas = newMalas
            )
            repository.updateDeity(updatedDeity)
            _activeDeity.value = updatedDeity

            // Trigger vibration only on exact multiples of 108
            if (newCount > 0 && newCount % 108 == 0) {
                vibrationHelper.triggerMalaCompletionVibration()
            } else {
                vibrationHelper.triggerLightTapFeedback()
            }
        }
    }

    fun decrementCount() {
        viewModelScope.launch {
            val deity = _activeDeity.value ?: return@launch
            if (deity.currentCount <= 0) return@launch
            val newCount = deity.currentCount - 1
            val newMalas = newCount / 108
            val updatedDeity = deity.copy(
                currentCount = newCount,
                completedMalas = newMalas
            )
            repository.updateDeity(updatedDeity)
            _activeDeity.value = updatedDeity
        }
    }

    fun resetCount() {
        viewModelScope.launch {
            val deity = _activeDeity.value ?: return@launch
            val updatedDeity = deity.copy(currentCount = 0, completedMalas = 0)
            repository.updateDeity(updatedDeity)
            _activeDeity.value = updatedDeity
            _showResetDialog.value = false
        }
    }

    fun setShowResetDialog(show: Boolean) {
        _showResetDialog.value = show
    }

    fun setActiveDeity(id: Long) {
        viewModelScope.launch {
            repository.setActiveDeity(id)
        }
    }

    fun addDeity(name: String) {
        viewModelScope.launch {
            if (name.isBlank()) return@launch
            val newDeity = DeityCounterEntity(name = name.trim())
            repository.insertDeity(newDeity)
            _showAddEditDialog.value = null
        }
    }

    fun updateDeityName(id: Long, newName: String) {
        viewModelScope.launch {
            if (newName.isBlank()) return@launch
            val deity = repository.getDeityById(id) ?: return@launch
            val updated = deity.copy(name = newName.trim())
            repository.updateDeity(updated)
            _showAddEditDialog.value = null
        }
    }

    fun updateDeityCount(id: Long, newCount: Int) {
        viewModelScope.launch {
            if (newCount < 0) return@launch
            val deity = repository.getDeityById(id) ?: return@launch
            val updated = deity.copy(
                currentCount = newCount,
                completedMalas = newCount / 108
            )
            repository.updateDeity(updated)
            _showEditCountDialog.value = null
        }
    }

    fun deleteDeity(deity: DeityCounterEntity) {
        viewModelScope.launch {
            repository.deleteDeity(deity)
            _showDeleteDialog.value = null
        }
    }

    fun setShowDeleteDialog(deity: DeityCounterEntity?) {
        _showDeleteDialog.value = deity
    }

    fun setShowAddEditDialog(deity: DeityCounterEntity?) {
        _showAddEditDialog.value = deity
    }

    fun setShowEditCountDialog(deity: DeityCounterEntity?) {
        _showEditCountDialog.value = deity
    }

    class Factory(
        private val repository: DeityRepository,
        private val vibrationHelper: VibrationHelper
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CounterViewModel::class.java)) {
                return CounterViewModel(repository, vibrationHelper) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
