package com.mehmet_inci.bottomnavigation_kotlin.ui.lichteffekte // ktlint-disable package-name

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mehmet_inci.bottomnavigation_kotlin.SharedViewModel
import com.mehmet_inci.bottomnavigation_kotlin.databinding.FragmentLichteffekteBinding

class LichteffekteFragment : Fragment(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    private lateinit var sharedViewModel: SharedViewModel
    private var _binding: FragmentLichteffekteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        _binding = FragmentLichteffekteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.seekBar1.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.seekBar2.setProgress(0)
                binding.seekBar3.setProgress(0)
                val value1 = "A" + progress
                sharedViewModel.setValue1(value1)
                binding.textViewSeekbarVal1.text = value1
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        binding.seekBar2.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.seekBar1.setProgress(0)
                binding.seekBar3.setProgress(0)
                val value2 = "B" + progress
                sharedViewModel.setValue2(value2)
                binding.textViewSeekbarVal2.text = value2
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        binding.seekBar3.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.seekBar1.setProgress(0)
                binding.seekBar2.setProgress(0)
                val value3 = "C" + progress
                sharedViewModel.setValue3(value3)
                binding.textViewSeekbarVal3.text = value3
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        // Register the accelerometer sensor listener
        accelerometer?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        // Handle accelerometer data changes here
        if (event != null) {
            val x = event.values[0]
            val y = event.values[1] * 4.5f
            val z = event.values[2]
            // Do something with the accelerometer data
            // For example, update UI elements or perform calculations
            // binding.seekBar1.setProgress(y.toInt())
        }
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        SensorManager.SENSOR_STATUS_ACCURACY_HIGH
    }
}
