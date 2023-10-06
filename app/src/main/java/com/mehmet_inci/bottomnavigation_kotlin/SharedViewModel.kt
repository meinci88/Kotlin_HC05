package com.mehmet_inci.bottomnavigation_kotlin // ktlint-disable package-name

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    private val string_value1 = MutableLiveData<String>()
    fun setValue1(val1: String) { string_value1.value = val1 }
    fun getValue1(): LiveData<String> { return string_value1 }

    private val string_value2 = MutableLiveData<String>()
    fun setValue2(val2: String) { string_value2.value = val2 }
    fun getValue2(): LiveData<String> { return string_value2 }

    private val string_value3 = MutableLiveData<String>()
    fun setValue3(val3: String) { string_value3.value = val3 }
    fun getValue3(): LiveData<String> { return string_value3 }
}
