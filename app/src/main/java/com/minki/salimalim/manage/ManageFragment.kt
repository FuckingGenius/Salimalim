package com.minki.salimalim.manage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.minki.salimalim.R
import kotlinx.android.synthetic.main.manage_housekeeping.*

class ManageFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.manage_housekeeping,null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val goods1 = ManageRecyclerData("삼푸", "2022-03-18", "욕실용품", 1, 500, 50)
        val goods2 = ManageRecyclerData("식용유", "2022-03-25", "식품", 1, 600, 60)
        val goods3 = ManageRecyclerData("고무장갑", "2022-02-04", "주방용품", 1, null, 100)
        val goods4 = ManageRecyclerData("치약", "2022-04-04", "욕실용품", 2, 150, 30)
        val goods5 = ManageRecyclerData("로션", "2021-12-17", "생활용품", 1, 100, 200)
        val items = ArrayList<ManageRecyclerData>()

        items.add(goods1)
        items.add(goods2)
        items.add(goods3)
        items.add(goods4)
        items.add(goods5)

        val adapter = ManageRecyclerAdapter(items)
        adapter.setItemClickListener(object : ManageRecyclerAdapter.OnItemClickListener{
            override fun onItemClick(v: View, position: Int) {

            }

        })
        ManageRecyclerView.adapter = adapter

        val tab = ManageTabLayout
        tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab!!.position){
                    0 -> Test.text = "전체"
                    1 -> Test.text = "식품"
                    2 -> Test.text = "욕실용품"
                    3 -> Test.text = "생활용품"
                    4 -> Test.text = "주방용품"
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }
}