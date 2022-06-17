package com.minki.salimalim.manage

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.minki.salimalim.MainActivity
import com.minki.salimalim.R
import com.minki.salimalim.SqlHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.manage_housekeeping.*
import java.util.*
import kotlin.collections.ArrayList

class ManageFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.manage_housekeeping, null)
    }

    var categories = mutableMapOf<Int,String>()
    val items = ArrayList<ManageRecyclerData>()
    lateinit var sqlHelper : SqlHelper
    lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    private lateinit var tab : TabLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        sqlHelper = SqlHelper((activity as MainActivity),"manage_table",null,1)
        categories = (activity as MainActivity).categories
        val category = ArrayList<String>()

        for(i in categories.values)
            category.add(i)

        if(sqlHelper.tableExist()) {
            items.addAll(sqlHelper.selectManage())
            Log.v("불러오기1",sqlHelper.selectLastId().toString())
            Log.v("불러오기", items.toString())
        }
        val inflater = LayoutInflater.from((activity as MainActivity))


        val adapter = ManageRecyclerAdapter(items,categories)
        adapter.setItemClickListener(object : ManageRecyclerAdapter.OnItemClickListener {
            @SuppressLint("SetTextI18n")
            override fun onItemClick(v: View, item: ManageRecyclerData) {

                // 아이템 클릭 시 이벤트
                val viewPopup = inflater.inflate(R.layout.view_item_popup, ManageHouseKeepingLayout,false)
                val dialog = AlertDialog.Builder((activity as MainActivity)).setView(viewPopup).show()
                viewPopup.findViewById<TextView>(R.id.ViewItemName).text = item.goodsName
                viewPopup.findViewById<TextView>(R.id.ViewItemCategory).text = categories.get(item.category)
                viewPopup.findViewById<TextView>(R.id.ViewItemPurchasedDate).text = item.purchaseDate
                viewPopup.findViewById<TextView>(R.id.ViewItemVolume).text = "${item.volume}(g/ml)"
                viewPopup.findViewById<TextView>(R.id.ViewItemQuantity).text = "${item.quantity} 개"
                viewPopup.findViewById<TextView>(R.id.ViewItemUsedTerm).text = "${item.usedTerm} 일"
                viewPopup.findViewById<TextView>(R.id.ViewItemDaysLeft).text = v.findViewById<TextView>(R.id.ManageDaysLeft).text.split(" ")[0]
                viewPopup.findViewById<Button>(R.id.ViewItemEdit).setOnClickListener {
                    val editManage = Intent((activity as MainActivity), EditManageActivity::class.java)
                    editManage.putExtra("item",item)
                    editManage.putExtra("category",category)
                    dialog.dismiss()
                    activityResultLauncher.launch(editManage) // activityResultLauncher 사용
                }
                viewPopup.findViewById<Button>(R.id.ViewItemDelete).setOnClickListener {

                    AlertDialog.Builder((activity as MainActivity)).setPositiveButton("예") { dialogInterface, i ->
                        sqlHelper.deleteManage(item)
                        dialog.dismiss()
                        updateView()
                    }.setNegativeButton("아니오") { dialogInterface, i ->
                        dialogInterface.dismiss()
                    }.setTitle("정말로 삭제하시겠습니까?").show()
                }
                viewPopup.findViewById<Button>(R.id.ViewItemPurchase).setOnClickListener {
                    dialog.dismiss()
                    (activity as MainActivity).isSearch = true
                    (activity as MainActivity).surfingQuery = item.goodsName
                    (activity as MainActivity).selectFragment(1)
                    (activity as MainActivity).Main_Bottom_Navigation.selectedItemId = R.id.shop
                }
            }

        })
        ManageRecyclerView.adapter = adapter

        AddManage.setOnClickListener {
            val addManage = Intent((activity as MainActivity), AddManageActivity::class.java)
            activityResultLauncher.launch(addManage) // activityResultLauncher 사용
        }


        // startActivityForResult 대신에 새로 생긴 activityResultLauncher
        activityResultLauncher  = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == RESULT_OK) {
                // 등록하거나 수정에 성공했을 때
                updateView()
                Handler().postDelayed({
                    when(tab.selectedTabPosition){
                        0 -> adapter.filter.filter(" ")
                        1 -> adapter.filter.filter("식품")
                        2 -> adapter.filter.filter("욕실용품")
                        3 -> adapter.filter.filter("생활용품")
                        4 -> adapter.filter.filter("주방용품")
                    }
                },1000)
            }
        }

        tab = ManageTabLayout
        tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab!!.position){
                    0 -> adapter.filter.filter(" ")
                    1 -> adapter.filter.filter("식품")
                    2 -> adapter.filter.filter("욕실용품")
                    3 -> adapter.filter.filter("생활용품")
                    4 -> adapter.filter.filter("주방용품")
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun updateView(){
        items.clear()
        items.addAll(sqlHelper.selectManage())
        ManageRecyclerView.adapter!!.notifyDataSetChanged()
    }
}