package com.minki.salimalim

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.minki.salimalim.manage.ManageRecyclerData
import java.util.*
import kotlin.collections.ArrayList

class SqlHelper(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int)
    : SQLiteOpenHelper(context, name, factory, version) {

    private val manageTableName = "manage_table"
    private val categoryTableName = "category_table"

    private val manageTable = "create table $manageTableName (id integer primary key autoincrement, goodsName text, purchasedDate text, category integer default 1, quantity integer, volume integer, usedTerm integer)"
    private val categoryTable = "create table $categoryTableName (id integer primary key autoincrement, category text)"
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(manageTable)
        db?.execSQL(categoryTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    fun insertManage(manage : ManageRecyclerData){
        val values = ContentValues()

        values.put("goodsName",manage.goodsName)
        values.put("category",manage.category)
        values.put("purchasedDate",manage.purchaseDate)
        values.put("quantity",manage.quantity)
        values.put("volume",manage.volume)
        values.put("usedTerm",manage.usedTerm)
        val wd = writableDatabase
        wd.insert(manageTableName,null,values)
        Log.v("데이터 입력 성공",values.toString())
        wd.close()
    }

    fun selectLastId() : Int{
        val selectLastId = "select id from $manageTableName order by id desc "
        val rd = readableDatabase
        val cursor = rd.rawQuery(selectLastId,null)
        cursor.moveToFirst()
        Log.v("불러오기1 개수", cursor.count.toString())
        val idNum = cursor.getColumnIndex("id")
        return cursor.getInt(idNum)
    }

    fun getCategories() : MutableMap<Int,String> {
        val categories : MutableMap<Int,String> = mutableMapOf()

        val rd = readableDatabase
        val cursor = rd.rawQuery("select * from $categoryTableName",null)
        while(cursor.moveToNext()){
            val idNum = cursor.getColumnIndex("id")
            val categoryNum = cursor.getColumnIndex(("category"))
            val id = cursor.getInt(idNum)
            val category = cursor.getString(categoryNum)
            categories[id] = category
        }
        return categories
    }

    fun selectManage() : ArrayList<ManageRecyclerData>{
        val list = ArrayList<ManageRecyclerData>()
        val selectAll = "select * from $manageTableName"
        val rd = readableDatabase
        val cursor = rd.rawQuery(selectAll,null)

        while(cursor.moveToNext()){
            val idNum = cursor.getColumnIndex("id")
            val goodNameNum = cursor.getColumnIndex(("goodsName"))
            val purchasedDateNum = cursor.getColumnIndex(("purchasedDate"))
            val categoryNum = cursor.getColumnIndex(("category"))
            val quantityNum = cursor.getColumnIndex(("quantity"))
            val volumeNum= cursor.getColumnIndex(("volume"))
            val usedTermNum= cursor.getColumnIndex(("usedTerm"))

            val id = cursor.getInt(idNum)
            val goodName = cursor.getString(goodNameNum)
            val purchasedDate = cursor.getString(purchasedDateNum)
            val category= cursor.getInt(categoryNum)
            val quantity = cursor.getInt(quantityNum)
            val volume = cursor.getInt(volumeNum)
            val usedTerm = cursor.getInt(usedTermNum)
            list.add(ManageRecyclerData(id,goodName,purchasedDate,category,quantity,volume,usedTerm))
        }
        cursor.close()
        rd.close()

        return list
    }

    fun updateManage(manage: ManageRecyclerData){
        val values = ContentValues()

        values.put("goodsName",manage.goodsName)
        values.put("category",manage.category)
        values.put("purchasedDate",manage.purchaseDate)
        values.put("quantity",manage.quantity)
        values.put("volume",manage.volume)
        values.put("usedTerm",manage.usedTerm)

        val wd = writableDatabase
        wd.update("$manageTableName",values,"id=${manage.id}",null)
        wd.close()
    }

    fun deleteManage(manage: ManageRecyclerData){
        val deleteManage = "delete from $manageTableName where id = ${manage.id}"
        val wd = writableDatabase
        wd.execSQL(deleteManage)
        wd.close()
    }

    fun deleteAll(){
        val deleteManage = "drop table $manageTableName"
        val wd = writableDatabase
        wd.execSQL(deleteManage)
        wd.close()
    }

    fun tableExist() : Boolean {
        val wd = writableDatabase
        val cursor = wd.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE name='$manageTableName'",null)
        Log.v("흠",cursor.count.toString())
        return cursor.count == 1
    }
}