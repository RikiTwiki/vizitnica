package com.walcanty.businesscard.ui

import androidx.lifecycle.*
import com.walcanty.businesscard.data.BusinessCard
import com.walcanty.businesscard.data.BusinessCardRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class MainViewModel (private val businessCardRepository:BusinessCardRepository) :ViewModel(){

//    fun insert(businessCard: BusinessCard){
//        businessCardRepository.insert(businessCard)
//    }

    private val _searchQuery = MutableLiveData<String>("")

    val allTasks = MediatorLiveData<List<BusinessCard>>()

    private val tasksEventChannel = Channel<TasksEvent>()
    val tasksEvent = tasksEventChannel.receiveAsFlow()

    init {
        allTasks.addSource(_searchQuery) { query ->
            if (query.isEmpty()) {
                allTasks.addSource(businessCardRepository.getAll.asLiveData()) {
                    allTasks.value = it
                }
            } else {
                allTasks.addSource(businessCardRepository.searchCards(query).asLiveData()) {
                    allTasks.value = it
                }
            }
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }


    fun insert(businessCard: BusinessCard) =viewModelScope.launch {
        businessCardRepository.insert(businessCard)
    }

//    fun getAll():LiveData<List<BusinessCard>>{
//        return businessCardRepository.getAll()
//    }

//    fun getCardById(id: Int) = viewModelScope.launch {
//        businessCardRepository.getCardById(id)
//    }

    fun getCardById(id: Int) = businessCardRepository.getCardById(id)


    fun update(businessCard: BusinessCard) = viewModelScope.launch {
        businessCardRepository.update(businessCard)
    }

    fun delete(businessCard: BusinessCard) = viewModelScope.launch {
        businessCardRepository.delete(businessCard)
    }

    fun onTaskSwiped(businessCard: BusinessCard) = viewModelScope.launch {
        businessCardRepository.delete(businessCard)
        tasksEventChannel.send(TasksEvent.ShowUndoMessange(businessCard))
    }

    fun onUndoDeleteClick(businessCard: BusinessCard) = viewModelScope.launch{
            businessCardRepository.insert(businessCard)
    }

    sealed class TasksEvent{
        data class ShowUndoMessange(val businessCard: BusinessCard) : TasksEvent()
    }

}

class MainViewModelFactory(private val repository:BusinessCardRepository):
        ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
       if (modelClass.isAssignableFrom(MainViewModel::class.java)){
           @Suppress("UNCHECKED_CAST")
           return MainViewModel(repository) as T
       }
       throw IllegalArgumentException("Unknown ViewModel class")
    }
}