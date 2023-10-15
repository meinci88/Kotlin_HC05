package com.mehmet_inci.bottomnavigation_kotlin.ui.lichteffekte // ktlint-disable package-name

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mehmet_inci.bottomnavigation_kotlin.SharedViewModel
import com.mehmet_inci.bottomnavigation_kotlin.databinding.FragmentLichteffekteBinding

class LichteffekteFragment : Fragment(), SensorEventListener {
    private lateinit var LED: EditText
    private lateinit var sharedViewModel: SharedViewModel

    private var _binding: FragmentLichteffekteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        _binding = FragmentLichteffekteBinding.inflate(inflater, container, false)

        // Save num LEDs in LichteffekteFragment
        binding.textInputLayout.setEndIconOnClickListener {
            Toast.makeText(requireContext(), binding.textInputEditText.text.toString() + "LEDs gespeichert", Toast.LENGTH_SHORT).show()
        }

        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // TextChanged listener
        binding.textInputEditText.addTextChangedListener() {
            sharedViewModel.setnumLED(it.toString())
        }
    }

    override fun onResume() {
        super.onResume()
        // Register the accelerometer sensor listener
        // accelerometer?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
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
    } }

