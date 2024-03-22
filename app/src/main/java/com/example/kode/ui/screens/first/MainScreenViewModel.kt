package com.example.kode.ui.screens.first

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kode.data.remote.Item
import com.example.kode.data.remote.RetrofitClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(val retrofitСlient: RetrofitClient): ViewModel() {
    // Backing property to avoid state updates from other classes
    private val _inputText = MutableStateFlow("")
    private val _uiState = MutableStateFlow( getList() )
    private val _error = MutableStateFlow(false)
    private val _selectedTab = MutableStateFlow(0)
    private val _currentItem = MutableStateFlow( getItem() )
    private val _isLoading = MutableStateFlow(false)

    // The UI collects from this StateFlow to get its state updates
    val inputText = _inputText
    val uiState = _uiState
    val error = _error
    val selectedTab = _selectedTab
    val currentItem = _currentItem
    val isLoading = _isLoading

    fun changeString(str: String) {
        _inputText.value = str
    }

    init {
        if (_uiState.value[0].second.isEmpty()) {
            getItems()
        }
    }

    fun getItems() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val items = retrofitСlient.getItems()

                // логика подгоовки серверных данных к отправке на экран
                val list = getList()

                items.items?.forEach {
                    when (it.department) {
                        "android" -> { list[5].second.add(it)
                            list[0].second.add(it) }
                        "ios" -> { list[4].second.add(it)
                            list[0].second.add(it) }
                        "design" -> { list[1].second.add(it)
                            list[0].second.add(it) }
                        "management" -> { list[3].second.add(it)
                            list[0].second.add(it) }
                        "qa" -> { list[11].second.add(it)
                            list[0].second.add(it) }
                        "back_office" -> { list[6].second.add(it)
                            list[0].second.add(it) }
                        "frontend" -> { list[7].second.add(it)
                            list[0].second.add(it) }
                        "hr" -> { list[9].second.add(it)
                            list[0].second.add(it) }
                        "pr" -> { list[10].second.add(it)
                            list[0].second.add(it) }
                        "backend" -> { list[8].second.add(it)
                            list[0].second.add(it) }
                        "support" -> { list[12].second.add(it)
                            list[0].second.add(it) }
                        "analytics" -> { list[2].second.add(it)
                            list[0].second.add(it) }
                    }
                }

                _uiState.value = list
                _error.value = false
            } catch (e: Exception) {
                _error.value = true
            }
        }
        _isLoading.value = false
    }
    private fun getList(): MutableList<Pair<String, MutableList<Item>>> {
        val list = mutableListOf<Pair<String, MutableList<Item>>>()
        list.add(0, "Все" to mutableListOf())
        list.add(1, "Дизайн" to mutableListOf())
        list.add(2, "Аналитика" to mutableListOf())
        list.add(3, "Менеджмент" to mutableListOf())
        list.add(4, "iOS" to mutableListOf())
        list.add(5, "Android" to mutableListOf())
        list.add(6, "Бэк-офис" to mutableListOf())
        list.add(7, "Frontend" to mutableListOf())
        list.add(8, "Backend" to mutableListOf())
        list.add(9, "HR" to mutableListOf())
        list.add(10, "PR" to mutableListOf())
        list.add(11, "QA" to mutableListOf())
        list.add(12, "Техподдержка" to mutableListOf())
        return list
    }

    fun setSelectedTab(num: Int) {
        _selectedTab.value = num
    }

    private fun getItem(): Item {
        return Item(
            id = "e0fceffa-cef3-45f7-97c6-6be2e3705927",
            phone ="802-623-1785",
            userTag = "LK",
            birthday = "2004-08-02",
            lastName = "Reichert",
            position = "Technician",
            avatarUrl = "https://i.pravatar.cc/150?img=1",
            firstName = "Dee",
            department = "back_office")
    }

    fun setItem(item: Item) {
        _currentItem.value = item
    }
}