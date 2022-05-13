package com.example.android.smartwg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.smartwg.adapter.RecycAdapterHighscore
import kotlinx.android.synthetic.main.activity_high_score_list_kitchen.*
import com.example.android.smartwg.model.Highscore
import com.example.android.smartwg.repository.Repository
import com.example.myapplication.util.globals
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class HighScoreListKitchenActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    private val recAdapterKitchen by lazy {
        RecycAdapterHighscore()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_score_list_kitchen)

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        setupRecyclerViewHighscore()
        createHighscoreList(globals.userSACode, recAdapterKitchen, viewModel.highscoreResponse)

        val bPlus = findViewById<Button>(R.id.bCreateNewKitchen)
        bPlus.setOnClickListener {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.GERMAN)
            val currentDateAndTime: String =
                simpleDateFormat.format(Date())

            System.out.println(currentDateAndTime)

            insertNewHighscore(currentDateAndTime);
        }

    }

    private fun setupRecyclerViewHighscore() {
        recyclerViewKitchen.adapter = recAdapterKitchen
        recyclerViewKitchen.layoutManager = LinearLayoutManager(this)
        recyclerViewKitchen.setNestedScrollingEnabled(false)
    }

    private fun createHighscoreList(
        SACODE: Int?,
        recAdapter: RecycAdapterHighscore,
        highscoreResponse: MutableLiveData<Response<List<Highscore>>>
    ) {
        viewModel.getHighscoresOfWGRepoViewM(SACODE, "Kitchen", highscoreResponse)
        highscoreResponse.observe(this,
            object : androidx.lifecycle.Observer<Response<List<Highscore>>> {
                override fun onChanged(t: Response<List<Highscore>>?) {
                    if (t != null) {
                        if (t.isSuccessful) {
                            t.body().let {
                                if (it != null) {
                                    recAdapter.setData(it)
                                    Log.d("TEST : ", it.get(0).toString())
                                }
                            }
                        }
                    } else {
                        if (t != null) {
                            Toast.makeText(
                                this@HighScoreListKitchenActivity,
                                t.code().toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            })
    }

    private fun insertNewHighscore(Date: String) {
        viewModel.createNewHighscoreViewM(
            globals.userId,
            globals.userSACode,
            Date,
            "Kitchen",
            globals.userFirstName,
            globals.userLastName
        )
        viewModel.echoStringResponse.observe(this, Observer { response ->
            if (response.isSuccessful) {
                var resp: String = response.body().toString()
                Toast.makeText(this, resp, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(
                    this,
                    response.code().toString() + " Create Highscore failed!",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}