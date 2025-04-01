package com.example.skills53dic

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.skills53dic.screens.MediaCenter
import com.google.gson.Gson
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import java.io.InputStream

class BuyTicketViewModel : ViewModel() {
    var ticketTypeData = mutableStateOf(listOf<Int>(0, 0, 0, 0, 0))
    var ticketType = mutableStateListOf<String>("一日票", "雙日票", "優待票", "敬老票", "學生票")

    fun setTicketTypeData(index: Int, item: String) {
        val newItem: Int = item.toIntOrNull() ?: 0
        if (newItem >= 0) {
            ticketTypeData.value = ticketTypeData.value.toMutableList().also {
                it[index] = newItem
            }
        }
    }
}

class MediaCenterDetailViewModel : ViewModel() {
    var data = mutableStateOf<MediaCenter?>(null)

    fun setData(newData: MediaCenter) {
        data.value = newData
    }
}

class AddTicketViewModel : ViewModel() {
    var type = mutableStateOf("")
    var name = mutableStateOf("")
    var email = mutableStateOf("")
    var phone = mutableStateOf("")
    var date = mutableStateOf("")
    var ticketId = mutableStateOf("")
    var price = mutableStateOf("")

    data class qrcodeDataUserSchema(
        val name: String, val email: String, val phoneNumber: String, val date: String
    )

    data class qrcodeDataSchema(
        val ticketId: String,
        val ticketType: String,
        val price: String,
        val payment: String,
        val user: qrcodeDataUserSchema
    )

    fun qrcodeRead(context: Context, file: String) {
        val bitmap = getBitmapFromAssets(context, file)
        val qrcodeDecodedData = bitmap?.let {
            decodeQRCode(it)
        }
        val gson = Gson()
        val qrcodeData = gson.fromJson(qrcodeDecodedData, qrcodeDataSchema::class.java)
        type.value = qrcodeData.ticketType
        name.value = qrcodeData.user.name
        email.value = qrcodeData.user.email
        phone.value = qrcodeData.user.phoneNumber
        date.value = qrcodeData.user.date
        ticketId.value = qrcodeData.ticketId
        price.value = qrcodeData.price
    }

    fun getBitmapFromAssets(context: Context, fileName: String): Bitmap? {
        return try {
            val assetManager = context.assets
            val inputStream: InputStream = assetManager.open(fileName)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun decodeQRCode(bitmap: Bitmap): String? {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        val source = RGBLuminanceSource(width, height, pixels)
        val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
        val reader = MultiFormatReader()
        val result = reader.decode(binaryBitmap)
        return result.text
    }
}