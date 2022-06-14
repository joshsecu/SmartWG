package com.example.android.smartwg

import android.media.Image
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.smartwg.adapter.RecycAdapterShoppingList
import com.example.android.smartwg.repository.Repository
import com.example.myapplication.util.globals
import kotlinx.android.synthetic.main.activity_shopping_list_overview.*

class ShoppingListOverviewActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    private val recAdapterShoppingListOverview by lazy{
        RecycAdapterShoppingList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_list_overview)

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        setupRecyclerViewShoppingList()
        getShoppingList()

        val bCreateShoppingList = findViewById<Button>(R.id.bCreateNewShoppingList)
        val bEditShoppingList = findViewById<ImageButton>(R.id.vEditShoppingList)
        val bRemoveShippingList = findViewById<ImageButton>(R.id.vRemoveShoppingList)
        val bConfirmShoppingListChanges = findViewById<ImageButton>(R.id.vConfirmShoppingListChanges)
        /**
        bCreateShoppingList.setOnClickListener {
            createShoppingList()
        }
        bEditShoppingList.setOnClickListener {
            bEditShoppingList.visibility = View.GONE
            bConfirmShoppingListChanges.visibility = View.VISIBLE
        }
        bRemoveShippingList.setOnClickListener {

        }
        bConfirmShoppingListChanges.setOnClickListener {
            bConfirmShoppingListChanges.visibility = View.VISIBLE
            bEditShoppingList.visibility = View.GONE
        }
        */
    }

    /**
     * @
     */
    private fun getShoppingList() {
        viewModel.getShoppingListsFromUserViewM(globals.userId)
        viewModel.shoppingListResponse.observe(this
        ) { shoppingListResponse ->
            if (shoppingListResponse != null) {
                if (shoppingListResponse.isSuccessful) {
                    shoppingListResponse.body().let {
                        if (it != null) {
                            recAdapterShoppingListOverview.setData(it)
                            println(it)
                        }
                    }
                }
            } else {
                Toast.makeText(
                    this@ShoppingListOverviewActivity,
                    "Error: ShoppingListRepsonse is null!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }

    private fun createShoppingList() {
        viewModel.createShoppingListFromUserViewM(globals.userId)
        getShoppingList()
    }

    private fun deleteShoppingList() {
        viewModel.deleteShoppingListFromUserViewM(globals.userId)
    }

    private fun editShoppingList() {
        TODO("NOT YET IMPLEMENTED")
        // viewModel.editShoppingListFromUserViewM(globals.userId, newTitle)
    }


    private fun setupRecyclerViewShoppingList() {
        recyclerViewShoppingListOverview.adapter = recAdapterShoppingListOverview
        recyclerViewShoppingListOverview.layoutManager = LinearLayoutManager(this)
        recyclerViewShoppingListOverview.setNestedScrollingEnabled(false)
    }

}