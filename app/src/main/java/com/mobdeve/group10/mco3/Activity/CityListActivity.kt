package com.mobdeve.group10.mco3.Activity

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.group10.mco3.Adapter.CityAdapter
import com.mobdeve.group10.mco3.ViewModel.CityViewModel
import com.mobdeve.group10.mco3.databinding.ActivityCityListBinding
import com.mobdeve.group10.mco3.model.CityResponseApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CityListActivity : AppCompatActivity() {
    lateinit var binding: ActivityCityListBinding
    // Lazily initialize the CityAdapter
    private val cityAdapter by lazy { CityAdapter() }
    // Initialize the CityViewModel using viewModels delegate
    private val cityViewModel: CityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using ViewBinding
        binding = ActivityCityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set transparent status bar
        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
        }

        binding.apply {
            // Add a TextWatcher to listen for changes in the EditText
            cityEdt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // Not used in this implementation
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // Not used in this implementation
                }

                override fun afterTextChanged(s: Editable?) {
                    // Show progress bar
                    progressBar2.visibility = View.VISIBLE
                    // Load cities based on the text entered
                    cityViewModel.loadCity(s.toString(), 10)
                        .enqueue(object : Callback<CityResponseApi> {
                            override fun onResponse(
                                call: Call<CityResponseApi>,
                                response: Response<CityResponseApi>
                            ) {
                                if (response.isSuccessful) {
                                    // Retrieve response body
                                    val data = response.body()
                                    data?.let {
                                        // Hide progress bar
                                        progressBar2.visibility = View.GONE
                                        // Submit data to the adapter and set adapter to RecyclerView
                                        cityAdapter.differ.submitList(it)
                                        cityView.apply {
                                            layoutManager = LinearLayoutManager(
                                                this@CityListActivity,
                                                LinearLayoutManager.HORIZONTAL,
                                                false
                                            )
                                            adapter = cityAdapter
                                        }
                                    }
                                }
                            }

                            override fun onFailure(call: Call<CityResponseApi>, t: Throwable) {
                                // Handle failure
                            }
                        })
                }
            })
        }
    }
}
