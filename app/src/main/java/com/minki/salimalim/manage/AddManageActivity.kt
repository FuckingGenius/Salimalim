package com.minki.salimalim.manage

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.minki.salimalim.system.CommonActivity
import com.minki.salimalim.MainActivity
import com.minki.salimalim.R
import com.minki.salimalim.SqlHelper
import kotlinx.android.synthetic.main.add_manage.*
import java.util.*
import kotlin.collections.ArrayList

class AddManageActivity : CommonActivity() {

    private val options = 6
    private var categoryNum = 0

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_manage)
        val categoryList = ArrayList<CharSequence>()
        Log.v("카테고리",categories.toString())
        Log.v("카테고리",categories.values.toString())

        // 분류 얻어오기
        for(i in categories.values)
            categoryList.add(i)


        // 입력 여부 확인을 위한 리스트 초기화
        for(i in 0 until options)
            canSend.add(false)

        Log.v("카테고리2",categoryList.toString())

        addTextListener(AddManageGoodsName,0)
        addTextListener(AddManageQuantity,1)
        addTextListener(AddManageVolume,2)
        addTextListener(AddManageUsedTerm,3)


        AddManageCategory.setOnClickListener {
            if(AddManageCategory.text == "")
                canSend[4] = false
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("분류를 선택하세요").setItems(categoryList.toTypedArray()
            ) { dialog, position ->
                AddManageCategory.text = "▼ ${categoryList[position]}"
                canSend[4] = true
                categoryNum = position + 1
            }.create().show()
        }

        AddManagePurchasedDate.setOnClickListener{
            if(AddManageCategory.text == "")
                canSend[5] = false
            val calendar = Calendar.getInstance()
            val dateListener = DatePickerDialog.OnDateSetListener { view, year, month, day ->
                val monthString = if(month < 10) "0${month+1}" else month+1
                val dayString = if(day < 10) "0$day" else day
                AddManagePurchasedDate.text = "$year-$monthString-$dayString"
                canSend[5] = true
            }
            DatePickerDialog(this,dateListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        AddManageSend.setOnClickListener {
            if(!canSend.contains(false)){
                val sqlHelper = SqlHelper(this,"manage_table",null,1)
                val manage = ManageRecyclerData(sqlHelper.selectLastId(),AddManageGoodsName.text.toString(),AddManagePurchasedDate.text.toString(),categoryNum,
                AddManageQuantity.text.toString().toInt(),AddManageVolume.text.toString().toInt(),AddManageUsedTerm.text.toString().toInt())
                sqlHelper.insertManage(manage)
                setResult(RESULT_OK)
                this.finish()
            }else{
                val num = canSend.indexOf(false)
                var where = ""
                when(num){
                    0 -> where = "물품명"
                    1 -> where = "분류"
                    2 -> where = "구매날짜"
                    3 -> where = "용량"
                    4 -> where = "개수"
                    5 -> where = "평균 사용기간"
                }
                Toast.makeText(this, "${where}을 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addTextListener(view : EditText, num : Int){
        view.addTextChangedListener {
            textWatcher
            canSend[num] = it.toString() != ""
        }
    }

    val canSend = ArrayList<Boolean>()

    val textWatcher = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(p0: Editable?) {

        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java).apply {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

}