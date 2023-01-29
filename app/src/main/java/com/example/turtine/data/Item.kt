package com.example.turtine.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "itemTBL") // SQLite 테이블 이름 지정
data class Item(
    @PrimaryKey(autoGenerate = true) // id를 기본 키로 식별하기 위함
    val id: Int = 0,
    @ColumnInfo(name = "routine") // 열 이름 설정
    val itemRoutine: String,
    @ColumnInfo(name = "min")
    val itemMin: Int,
    @ColumnInfo(name = "sec")
    val itemSec: Int
)

