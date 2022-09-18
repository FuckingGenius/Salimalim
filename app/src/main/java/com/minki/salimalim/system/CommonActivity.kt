package com.minki.salimalim.system

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.minki.salimalim.SqlHelper
import com.minki.salimalim.manage.GoodsData
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

open class CommonActivity : AppCompatActivity() {
    var categories : MutableMap<Int,String> = mutableMapOf()
    companion object{
        var goods = ArrayList<GoodsData>()
    }
    var surfingQuery = ""
    var isSearch = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var sqlHelper = SqlHelper((this),"manage_table",null,1)
        var dbHelper = DataBaseHelper(this)
        goods = sqlHelper.getGoods()
        Log.v("호호","누가먼저냐")
        categories = sqlHelper.getCategories()
    }

    fun getGoods (position : Int): ArrayList<CharSequence> {
        val temp = ArrayList<CharSequence>()
        for(i in goods){
            if(categories[i.category].equals(categories[position]))
                temp.add(i.goodsName)
        }
        Log.v("호호 이것이",temp.toString())
        return temp
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