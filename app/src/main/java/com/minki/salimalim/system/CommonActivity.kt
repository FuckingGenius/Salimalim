package com.minki.salimalim.system

import android.content.Context
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
        var heightPixels : Int = 0
        var widthPixels : Int = 0
        var xdpi : Int = 0
        var ydpi : Int = 0
        var iconSize = 0.03f
        var navigationSize = 0.15f
    }
    lateinit var sqlHelper : SqlHelper
    var surfingQuery = ""
    var isSearch = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sqlHelper = SqlHelper((this),"manage_table",null,1)
        var dbHelper = DataBaseHelper(this)
        Log.v("호호","누가먼저냐")
        categories = sqlHelper.getCategories()
        updateGoods(this)
    }


    fun updateGoods(context : Context){
        val sqlHelper = SqlHelper((context),"manage_table",null,1)
        goods = sqlHelper.getGoods()
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