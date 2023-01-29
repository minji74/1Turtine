package com.example.turtine

import android.app.Application
import com.example.turtine.data.ItemRoomDatabase

// Application 클래스에서 데이터베이스 인스턴스를 인스턴스화
class InventoryApplication : Application(){
    val database: ItemRoomDatabase by lazy { ItemRoomDatabase.getDatabase(this) }
}