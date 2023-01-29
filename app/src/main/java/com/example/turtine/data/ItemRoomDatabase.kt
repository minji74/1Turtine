package com.example.turtine.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class ItemRoomDatabase : RoomDatabase() {

    // 데이터베이스는 DAO를 알아야 함 -> ItemDao를 반환하는 추상 함수를 선언 (DAO는 여러 개가 있을 수 있음)
    abstract fun itemDao(): ItemDao

    // 컴패니언 객체를 통해 클래스 이름을 한정자로 사용하여
    // 데이터베이스를 만들거나 가져오는 메서드에 액세스할 수 있음.
    companion object {

        // @Volatile : 휘발성 변수의 값은 캐시되지 않고 모든 쓰기와 읽기는 기본 메모리에서 실행됨
        // -> INSTANCE 값이 항상 최신 상태로 유지되고 모든 실행 스레드에서 같은지 확인할 수 있음
        // 즉, 한 스레드에서 INSTANCE를 변경하면 다른 모든 스레드에 즉시 표시됨.

        // INSTANCE 변수 :  데이터베이스가 만들어지면 데이터베이스 참조를 유지
        @Volatile
        private var INSTANCE: ItemRoomDatabase? = null


        //synchronized 블록 내에 데이터베이스를 가져오면 한 번에 한 실행 스레드만 이 코드 블록에 들어갈 수 있으므로 데이터베이스가 한 번만 초기화
        fun getDatabase(context: Context): ItemRoomDatabase {
            return INSTANCE ?: synchronized(this){
                // 데이터베이스 빌더를 사용하여 데이터베이스를 가져옴.
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ItemRoomDatabase::class.java,
                    "item_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                return instance

            }
        }


    }
}