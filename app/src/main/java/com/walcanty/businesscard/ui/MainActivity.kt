package com.walcanty.businesscard.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.walcanty.businesscard.App
import com.walcanty.businesscard.databinding.ActivityMainBinding
import com.walcanty.businesscard.util.Image
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as App).repository)
    }

    private val adapter by lazy { BusinessCardAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.rvCards.adapter = adapter


        insertListener()

        val swipeGesture = object : SwipeGesture(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        val card = adapter.currentList[viewHolder.bindingAdapterPosition]
                        mainViewModel.onTaskSwiped(card)

                        //  mainViewModel.delete(adapter.currentList[viewHolder.bindingAdapterPosition])

//                        val position = viewHolder.bindingAdapterPosition
//                        val item = adapter.currentList[position]
//                        mainViewModel.delete(item)
                        Toasty.success(
                            this@MainActivity,
                            "Удаленная карта",
                            Toast.LENGTH_SHORT,
                            true
                        ).show()
                        Log.e("TAG", "Delete ${adapter.currentList}")
                    }

                    ItemTouchHelper.RIGHT -> {
                        val card = adapter.currentList[viewHolder.bindingAdapterPosition]
                        val intent = Intent(this@MainActivity, AddBusinessCardActivity::class.java)
                        intent.putExtra(AddBusinessCardActivity.TASK_ID, card.id)
                        startActivity(intent)


                        Log.e("TAG", "Update ${card}")
                        adapter.notifyDataSetChanged()
                    }
                }

            }

        }

        val toucheHelper = ItemTouchHelper(swipeGesture)
        toucheHelper.attachToRecyclerView(binding.rvCards)

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterCards(newText ?: "")
                return true
            }
        })

    }

    private fun filterCards(query: String) {
        mainViewModel.allTasks.observe(this, { cards ->
            adapter.submitList(cards.filter { it.name.contains(query, ignoreCase = true) })
        })
    }

    private fun insertListener() {
        binding.fab.setOnClickListener {
            val intent = Intent(this@MainActivity, AddBusinessCardActivity::class.java)
            startActivity(intent)
        }
        adapter.listenerShare = { card ->
            Image.share(this@MainActivity, card)
        }
    }


    override fun onStart() {
        super.onStart()
        mainViewModel.allTasks.observe(this, {
            adapter.submitList(it)
        })

        this.lifecycleScope.launchWhenStarted {
            mainViewModel.tasksEvent.collect { event ->
                when (event) {
                    is MainViewModel.TasksEvent.ShowUndoMessange -> {
                        Snackbar.make(
                            findViewById(android.R.id.content),
                            "Deleted Card",
                            Snackbar.LENGTH_LONG
                        )
                            .setAction("UNDO") {
                                mainViewModel.onUndoDeleteClick(event.businessCard)
                            }.show()
                    }
                }
            }
        }

    }

}