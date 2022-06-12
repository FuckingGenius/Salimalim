package com.minki.salimalim.system

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.minki.salimalim.SqlHelper
import java.text.SimpleDateFormat
import java.util.*

open class CommonActivity : AppCompatActivity() {
    var categories : MutableMap<Int,String> = mutableMapOf()
    var surfingQuery = ""
    var isSearch = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var sqlHelper = SqlHelper((this),"manage_table",null,1)
        categories = sqlHelper.getCategories()
    }

    fun getTime(time : String) : Int {
        val t = System.currentTimeMillis()
        val date = Date(t)
        val format2 = SimpleDateFormat("yyyy-MM-dd")
        val now = format2.parse(format2.format(date))
        val purchased = format2.parse(time)
        return ((now.time - purchased.time) / 60000 / 1440).toInt()
    }
}