package com.example.turtine

import androidx.lifecycle.*
import com.example.turtine.data.Item
import com.example.turtine.data.ItemDao
import kotlinx.coroutines.launch

class InventoryViewModel(private val itemDao: ItemDao) : ViewModel() {

    // ++ getItems() 함수는 Flow를 반환하므로 LiveData로
    val allItems: LiveData<List<Item>> = itemDao.getItems().asLiveData()

    // ** Item 객체를 가져오고 비차단 방식으로 데이터를 데이터베이스에 추가하는 함수
    private fun insertItem(item: Item) {
        // 기본 스레드 밖에서 데이터베이스와 상호작용하려면 코루틴을 시작하고 그 안에서 DAO 메서드를 호출
        viewModelScope.launch {
            itemDao.insert(item)
        }
    }

    // ** 문자열 세 개를 가져오고, Item 인스턴스를 반환하는 함수
    private fun getNewItemEntry(itemRoutine: String, itemMin: String, itemSec: String): Item {
        return Item(
            itemRoutine = itemRoutine,
            itemMin = itemMin.toInt(),
            itemSec = itemSec.toInt()
        )
    }

    // ** 항목 세부정보 문자열을 세 개 가져오는 함수
    fun addNewItem(itemRoutine: String, itemMin: String, itemSec: String) {
        val newItem = getNewItemEntry(itemRoutine, itemMin, itemSec)
        insertItem(newItem)
    }


    // ** TextFields의 텍스트가 비어 있지 않은지 확인하는 함수
    fun isEntryValid(itemRoutine: String, itemMin: String, itemSec: String): Boolean {
        if (itemRoutine.isBlank() || itemMin.isBlank() || itemSec.isBlank()) {
            return false
        }
        return true
    }


    // ++ id를 통해 검색하기
    fun retrieveItem(id: Int): LiveData<Item> {
        return itemDao.getItem(id).asLiveData()
    }

}

class InventoryViewModelFactory(private val itemDao: ItemDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create( modelClass: Class<T> ): T {

        // modelClass가 InventoryViewModel 클래스와 같은지 확인하고 그 인스턴스를 반환.
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class") // 같지 않으면 예외 발생
    }
}