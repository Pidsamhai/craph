package com.psm.craph.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.psm.craph.LineChart
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        random.setOnClickListener {
            val data = (0 until 9).map {
                LineChart.LineDataSet(
                    Random.nextInt(0,10).toFloat(),
                    Random.nextInt(0,10).toFloat()
                )
            }

            chart.dataSet = data
            chart.invalidate()
        }
    }
}