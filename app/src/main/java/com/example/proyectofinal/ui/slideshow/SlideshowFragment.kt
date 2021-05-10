package com.example.proyectofinal.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.proyectofinal.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry


class SlideshowFragment : Fragment() {

    private lateinit var slideshowViewModel: SlideshowViewModel
    private lateinit var imageView: ImageView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
                ViewModelProvider(this).get(SlideshowViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_slideshow, container, false)
        imageView = root.findViewById(R.id.image_prenda_informe)
        val barChart = root.findViewById(R.id.bar_chart_semanal) as BarChart
        val barCharttwo = root.findViewById(R.id.bar_chart_mensual) as BarChart

        val entries: ArrayList<BarEntry> = ArrayList()
        entries.add(BarEntry(8f, 0))
        entries.add(BarEntry(2f, 1))
        entries.add(BarEntry(5f, 2))
        entries.add(BarEntry(20f, 3))
        entries.add(BarEntry(15f, 4))

        val bardataset = BarDataSet(entries, "Cells")

        val labels = ArrayList<String>()
        labels.add("L")
        labels.add("Ma")
        labels.add("Mi")
        labels.add("J")
        labels.add("V")

        val data = BarData(labels, bardataset)
        barChart.data = data // set the data and list of labels into chart
        barCharttwo.data = data // set the data and list of labels into chart

        return root
    }
}