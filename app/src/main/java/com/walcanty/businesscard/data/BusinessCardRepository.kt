package com.walcanty.businesscard.data

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking

class BusinessCardRepository(private val dao:BusinessCardDao) {

//    fun insert(businessCard: BusinessCard) = runBlocking {
//        launch(Dispatchers.IO){
//            dao.insert(businessCard)
//        }
//    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(businessCard: BusinessCard){
        dao.insert(businessCard)
    }

    //fun getAll() = dao.getAll()
    val getAll: Flow<List<BusinessCard>> = dao.getAll()



    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(businessCard: BusinessCard){
        dao.update(businessCard)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(businessCard: BusinessCard){
        dao.delete(businessCard)
    }

//    @Suppress("RedundantSuspendModifier")
//    @WorkerThread
//    suspend fun getCardById(id: Int){
//        dao.getCardById(id)
//    }

    fun getCardById(cardId: Int) = runBlocking { dao.getCardById(cardId) }

    fun searchCards(query: String): Flow<List<BusinessCard>> {
        return dao.searchCards("%$query%")
    }


}