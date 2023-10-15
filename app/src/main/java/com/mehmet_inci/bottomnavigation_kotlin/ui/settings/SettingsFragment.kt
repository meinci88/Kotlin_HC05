package com.mehmet_inci.bottomnavigation_kotlin.ui.settings // ktlint-disable package-name

import android.Manifest.permission.BLUETOOTH_CONNECT
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mehmet_inci.bottomnavigation_kotlin.R
import com.mehmet_inci.bottomnavigation_kotlin.SharedViewModel
import com.mehmet_inci.bottomnavigation_kotlin.UserSettingsManager
import com.mehmet_inci.bottomnavigation_kotlin.databinding.FragmentSettingsBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.OutputStream
import java.util.UUID


class SettingsFragment : Fragment() {

    private lateinit var getUserSettingsManager: UserSettingsManager

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null


    val BLUETOOTH_PERMISSION_REQUEST_CODE = 1 // You can choose any value for the request code.

    private val bluetoothReceiver = BluetoothReceiver()
    val spinner_selectedItem = ArrayList<String>()
    private lateinit var binding: FragmentSettingsBinding

    // private lateinit var sharedViewModel: SharedViewModel
    val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    var MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private lateinit var adapter: ArrayAdapter<String>
    var itemAddress: String? = null
    var itemName: String? = null
    var btSocket: BluetoothSocket? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
       /* sharedViewModel.getnumLED().observe(
            viewLifecycleOwner,
            Observer {
                    valInt4 ->
                binding.textViewNumLEDs.setText(valInt4)
            },
        )*/
        println("SettingsFragment Hier ist onCreateView -------------------------")

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        // Check if the device has an accelerometer
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

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


        val root: View = binding.root
        return root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        //region check if connected or not
        if (btSocket != null && btSocket!!.isConnected) {
            println("Connected")
            binding.spinner.visibility = View.INVISIBLE
            binding.textViewSelectedItem.text = itemName
            binding.textViewSelectedItem.setOnLongClickListener {
                disconnect()
                true
            }
        } else {
            println("nicht Connected")
            binding.spinner.visibility = View.VISIBLE
            binding.textViewSelectedItem.text = "Disconnected"
            devList()
        }
        //endregion

        devList()

        //region getValue1 LiveData
        sharedViewModel.getValue1().observe(
            viewLifecycleOwner,
            Observer { valInt1 ->
                binding.textViewSeekbarVal1.text = valInt1.toString()
                try {
                    val outputStream: OutputStream = btSocket!!.outputStream
                    outputStream.write(valInt1.toByteArray())
                } catch (e: java.lang.Exception) {
                    println("-------Fehler: " + e)
                }
            },
        )
        //endregion
        //region getValue2 LiveData
        sharedViewModel.getValue2().observe(
            viewLifecycleOwner,
            Observer { valInt2 ->
                binding.textViewSeekbarVal2.text = valInt2.toString()
                try {
                    val outputStream: OutputStream = btSocket!!.outputStream
                    outputStream.write(valInt2.toByteArray())
                } catch (e: java.lang.Exception) {
                    println("-------Fehler: " + e)
                }
            },
        )
        //endregion
        //region getValue3 LiveData
        sharedViewModel.getValue3().observe(
            viewLifecycleOwner,
            Observer { valInt3 ->
                binding.textViewSeekbarVal3.text = valInt3.toString()
                try {
                    val outputStream: OutputStream = btSocket!!.outputStream
                    outputStream.write(valInt3.toByteArray())
                } catch (e: java.lang.Exception) {
                    println("-------Fehler: " + e)
                }
            },
        )
        //endregion
        /*sharedViewModel.getnumLED().observe(
            viewLifecycleOwner,
            Observer {
                    valInt4 ->
                binding.textViewNumLEDs.setText(valInt4)
            },
        )*/
    }

    override fun onResume() {
        println("SettingsFragment Hier ist onResume -------------------------")
        super.onResume()



        // Register the accelerometer sensor listener
        if (accelerometer != null) {
            // Register the listener
            // accelerometer?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
        }
    }



    private fun devList() {
        if (ActivityCompat.checkSelfPermission(requireActivity(), BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED
        ) { ActivityCompat.requestPermissions(requireActivity(), arrayOf(BLUETOOTH_CONNECT), BLUETOOTH_PERMISSION_REQUEST_CODE)
        } else {
            val devices = mBluetoothAdapter.bondedDevices
            adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_expandable_list_item_1, spinner_selectedItem)
            adapter.setDropDownViewResource(R.layout.spinner_item_layout)
            // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = adapter
            spinner_selectedItem.clear()
            if (devices != null) {
                for (device in devices) {
                    binding.spinner.adapter = adapter
                    spinner_selectedItem.add(device.address + ":  " + device.name.toString())
                    adapter.notifyDataSetChanged()
                }
            }

            // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner_selectedItem.add(0, "Bitte Bluetoothgerät wählen")
            binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    positionAddressName: Int,
                    id: Long,
                ) {
                    var item = spinner_selectedItem[positionAddressName]
                    if (item != "Bitte Bluetoothgerät wählen") {
                        itemAddress = item.substring(0, 17)
                        itemName = item.substring(18, item.length)
                        alertDialog_BluetoothVerbinden()
                    }
                    binding.spinner.setSelection(0)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle nothing selected if needed
                }
            }
        }
    }

    private fun alertDialog_BluetoothVerbinden() {
        // builder.setTitle("Alert Dialog")
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Mit dem Bluetoothgerät verbinden.")
        builder.setPositiveButton("OK") { dialog, which ->
            // Do something when the user clicks the positive button
            dialog.dismiss()

            bluetooth_bound()
        }
        // Optionally, you can add a negative button
        builder.setNegativeButton("Cancel") { dialog, which ->
            // Do something when the user clicks the negative button
            dialog.dismiss()
            Toast.makeText(context, "wird nicht gebunden", Toast.LENGTH_SHORT).show()
        }
        val dialog = builder.create()
        dialog.show()
    }
    private fun bluetooth_bound() {
        //region Starte eine Coroutine, um die Bluetooth-Verbindung herzustellen
        GlobalScope.launch {
            val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            val connectedDevices = bluetoothAdapter.bondedDevices
            // val devices = mBluetoothAdapter.bondedDevices
            val BT_Device: BluetoothDevice = mBluetoothAdapter.getRemoteDevice(itemAddress)
            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    BLUETOOTH_CONNECT,
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
            }
            try {
                btSocket?.close()
            } catch (e: Exception) {
            }
            try {
                btSocket =
                    BT_Device.createRfcommSocketToServiceRecord(this@SettingsFragment.MY_UUID)
                btSocket?.connect()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        //endregion
    }


    private inner class BluetoothReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action: String? = intent.action
            // val BT_Device: BluetoothDevice = mBluetoothAdapter.getRemoteDevice(itemAddress)
            if (BluetoothDevice.ACTION_ACL_DISCONNECTED == action) {
                binding.textViewSelectedItem.text = "Disconnected"
                binding.spinner.visibility = View.VISIBLE
            }

            if (BluetoothDevice.ACTION_ACL_CONNECTED == action) {
                // Device is connected
                Toast.makeText(context, "Bluetooth Connected", Toast.LENGTH_SHORT).show()
                // binding.textViewSelectedItem.text = "Connected"
            }
            if (BluetoothDevice.ACTION_ACL_CONNECTED == action) {
                // Device bonded
                binding.textViewSelectedItem.text = itemName
                binding.textViewSelectedItem.setOnLongClickListener {
                    disconnect()
                    true
                }
                binding.spinner.visibility = View.INVISIBLE
            }
        }
    }
    private fun disconnect() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Bluetoothverbindung trennen.")
        builder.setPositiveButton("OK") { dialog, which ->
            // Do something when the user clicks the positive button
            dialog.dismiss()
            try {
                btSocket?.close()
            } catch (e: Exception) {
            }
        }
        // Optionally, you can add a negative button
        builder.setNegativeButton("Cancel") { dialog, which ->
            // Do something when the user clicks the negative button
            dialog.dismiss()
            Toast.makeText(context, "wird nicht getrennt", Toast.LENGTH_SHORT).show()
        }
        val dialog = builder.create()
        dialog.show()
    }


    override fun onStart() {
        super.onStart()

        getUserSettingsManager = UserSettingsManager(requireContext())
        val userValue = getUserSettingsManager.getUserSetting("numLEDs", "8") as String
        //binding.textViewNumLEDs.setText(userValue)
        binding.textViewNumLEDs.editText?.setText(userValue)
        sharedViewModel.getnumLED().observe(
            viewLifecycleOwner,
            Observer {
                    valInt4 ->
                binding.textViewNumLEDs.editText?.setText(valInt4)
            },
        )
        println("SettingsFragment Hier ist onStart -------------------------")

        // Register the BroadcastReceiver to listen for Bluetooth events
        val filter = IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED)
        val filter1 = IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        requireActivity().registerReceiver(bluetoothReceiver, filter)
        requireActivity().registerReceiver(bluetoothReceiver, filter1)
    }

    override fun onStop() {
        super.onStop()
        println("SettingsFragment Hier ist onStop -------------------------")

        // Unregister the BroadcastReceiver when the fragment is stopped
        requireActivity().unregisterReceiver(bluetoothReceiver)
    }
}


