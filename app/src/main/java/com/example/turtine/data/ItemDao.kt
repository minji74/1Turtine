package com.example.turtine.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    // 데이터베이스 작업은 실행에 시간이 오래 걸릴 수 있으므로 별도의 스레드에서 실행해야 함.

    // OnConflict 인수 : 충돌이 발생하는 경우 Room에 실행할 작업을 알려줌
    // OnConflictStrategy.IGNORE 전략은 기본 키가 이미 데이터베이스에 있으면 새 항목을 무시

    // ** 삽입  : Kotlin 코드에서 insert()를 호출하면 Room은 SQL 쿼리를 실행하여 데이터베이스에 항목을 삽입
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item) // 함수를 정지함수로 만들어서 코루틴에서 실행할 수 있도록 함

    // ** 수정 : 항목의 다른 속성 일부나 전부를 업데이트할 수 있음
    @Update
    suspend fun update(item: Item)

    // ** 삭제 : 한 항목이나 항목 목록을 삭제
    @Delete
    suspend fun delete(item: Item)

    // 나머지 기능에는 편의 주석이 없으므로 @Query 주석을 사용하여 SQLite 쿼리를 제공해야 함

    // ** 검색 : 주어진 id에 기반하여 항목 테이블에서 특정 항목을 검색
    @Query("SELECT * from itemTBL WHERE id = :id")
    fun getItem(id: Int): Flow<Item>

    // ** kotlinx.coroutines.flow.Flow에서 Flow를 가져오는 함수
    @Query("SELECT * from itemTBL ORDER BY routine ASC")
    fun getItems(): Flow<List<Item>>
}