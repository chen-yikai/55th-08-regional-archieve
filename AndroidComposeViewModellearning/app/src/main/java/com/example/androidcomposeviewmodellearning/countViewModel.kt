package com.example.androidcomposeviewmodellearning

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class countViewModel :ViewModel(){

    var count by mutableStateOf(0)
        private set
    fun increment(){
        count++
    }
}