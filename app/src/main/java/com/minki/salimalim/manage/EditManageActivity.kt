package com.minki.salimalim.manage

import android.app.DatePickerDialog
import android.content.Intent
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AlertDialogLayout
import androidx.core.widget.addTextChangedListener
import com.minki.salimalim.system.CommonActivity
import com.minki.salimalim.MainActivity
import com.minki.salimalim.R
import com.minki.salimalim.SqlHelper
import kotlinx.android.synthetic.main.add_manage.*
import java.util.*
import kotlin.collections.ArrayList

class EditManageActivity : CommonActivity() {

    private val options = 6
    private var categoryNum = 0
    private var goodsNum = 0
    private var currentCategory = 0
    private var originalDate = ""
    private var originalQuantity = 0
    private var originalVolume : Int = 0
    private var update = false

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_manage)

        var dialogList = ArrayList<CharSequence>()
        val categoryList = ArrayList<CharSequence>()
        val goodsList = ArrayList<CharSequence>()
        val dialog = AlertDialog.Builder(this)
        val data : ManageRecyclerData = intent.getSerializableExtra("item") as ManageRecyclerData
        var category : ArrayList<String> = intent.getStringArrayListExtra("category") as ArrayList<String>
        update = intent.getBooleanExtra("update?",false)

        val editable = Editable.Factory.getInstance()

        var titleText = ""
        if(update){
            titleText = "갱신"
            originalDate = data.purchaseDate
            originalQuantity = data.quantity
            originalVolume = if(data.volume == null) 1 else data.volume!!
            Linear_UsedTerm.visibility = View.GONE
        }else{
            AddManagePurchasedDate.text = editable.newEditable(data.purchaseDate)
            titleText = "수정"
        }

        AddManageTitle.text = "물품 $titleText"
        AddManageSend.text = titleText
        AddManageCategory.text = editable.newEditable(goods[data.goodsName-1].goodsName)
        AddManageCategoryOriginal.text = editable.newEditable(category.get(data.category-1))
        categoryNum = data.category
        AddManageVolume.text = editable.newEditable(data.volume.toString())
        AddManageQuantity.text = editable.newEditable(data.quantity.toString())
        AddManageUsedTerm.text = editable.newEditable(data.usedTerm.toString())


        // 분류 얻어오기
        for(i in categories.values)
            categoryList.add(i)


        // 입력 여부 확인을 위한 리스트 초기화
        for(i in 0 until options)
            canSend.add(true)

        Log.v("카테고리2",categoryList.toString())

        addTextListener(AddManageGoodsName,0)
        addTextListener(AddManageQuantity,1)
        addTextListener(AddManageVolume,2)
        addTextListener(AddManageUsedTerm,3)


        AddManageCategory.setOnClickListener {
            if (!update) {
                if (AddManageCategory.text == "")
                    canSend[4] = false
                dialogList.clear()
                dialogList.addAll(categoryList)
                dialog.setTitle("분류를 선택하세요").setItems(
                    dialogList.toTypedArray()
                ) { dialog, position ->
                    currentCategory = position + 1
                    goodsList.clear()
                    goodsList.addAll(getGoods(position + 1))
                    Log.v("호호", "$goodsList")
                    dialogList.clear()
                    dialogList.addAll(goodsList)
                    val dialog = AlertDialog.Builder(this)
                    dialog.setTitle("물품명을 선택하세요")
                        .setItems(dialogList.toTypedArray()) { dialog, position ->
                            canSend[4] = true
                            AddManageCategory.text = "${goodsList[position]}"
                        }.setPositiveButton("물품 추가하기") { dialog, position ->
                            val addDialog = AlertDialog.Builder(this)
                            val newView = AlertDialogLayout.inflate(this, R.layout.add_goods_edittext, null)
                            addDialog.setView(newView)
                            addDialog.setTitle("추가할 물품명을 입력하세요")
                                .setPositiveButton("추가") { dialog, position ->
                                    val goodsName = newView.findViewById<EditText>(R.id.AddGoods).text
                                    if (sqlHelper.addGoods(GoodsData(0, goodsName.toString(), currentCategory), this)) {
                                        AddManageCategory.text = goodsName
                                        canSend[4] = true
                                        Toast.makeText(this, "${goodsName}가 추가되었습니다!", Toast.LENGTH_SHORT).show()
                                    } else
                                        Toast.makeText(this, "중복된 상품명입니다.", Toast.LENGTH_SHORT).show()
                                }.show()
                        }.create().show()
                }.create().show()
            }
        }

        AddManagePurchasedDate.setOnClickListener{
            if(AddManageCategoryOriginal.text == "")
                canSend[5] = false
            val calendar = Calendar.getInstance()
            val dateListener = DatePickerDialog.OnDateSetListener { view, year, month, day ->
                val monthString = if(month < 9) "0${month+1}" else month+1
                val dayString = if(day < 10) "0$day" else day
                AddManagePurchasedDate.text = "$year-$monthString-$dayString"
                canSend[5] = true
            }
            DatePickerDialog(this,dateListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        AddManageSend.setOnClickListener {
            if(!canSend.contains(false)) {
                for (i in goods) {
                    Log.v("이전$i", "${i.goodsName} 과 ${AddManageCategory.text}")
                    if (i.goodsName.equals(AddManageCategory.text.toString())) {
                        categoryNum = i.category
                        goodsNum = i.id
                    }
                }
                val sqlHelper = SqlHelper(this, "manage_table", null, 1)
                data.goodsName = goodsNum
                data.purchaseDate = AddManagePurchasedDate.text.toString()
                data.category = categoryNum
                data.quantity = AddManageQuantity.text.toString().toInt()
                data.volume = AddManageVolume.text.toString().toInt()
                data.usedTerm = if (update)
                    (getTime(originalDate) - getTime(AddManagePurchasedDate.text.toString())) / originalQuantity
                else
                    AddManageUsedTerm.text.toString().toInt()

                if (sqlHelper.updateManage(data)) {
                    setResult(RESULT_OK)
                    this.finish()
                } else
                    Toast.makeText(this, "이미 해당 물품이 있습니다.", Toast.LENGTH_SHORT).show()
            }else{
                val num = canSend.indexOf(false)
                var where = ""
                when(num){
                    0 -> where = "상품명"
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

    private val canSend = ArrayList<Boolean>()

    private val textWatcher = object : TextWatcher{
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