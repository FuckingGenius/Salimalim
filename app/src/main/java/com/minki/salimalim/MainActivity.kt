package com.minki.salimalim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.minki.salimalim.manage.ManageFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val currentNum = 0
    private var fragments = ArrayList<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val manageFragment = ManageFragment()
        val shopFragment = ShopFragment()
        val communityFragment = CommunityFragment()

        fragments.add(manageFragment)
        fragments.add(shopFragment)
        fragments.add(communityFragment)

        for(i in 0 until fragments.size)
            supportFragmentManager.beginTransaction().add(R.id.Main_Frame, fragments[i]).commit()
        selectFragment(0)
        findViewById<BottomNavigationView>(R.id.Main_Bottom_Navigation).setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return if(currentNum == item.itemId)
            false
        else{
            when(item.itemId){
                R.id.manage -> selectFragment(0)
                R.id.shop -> selectFragment(1)
                R.id.community -> selectFragment(2)
            }
            true
        }
    }

    private fun selectFragment(num : Int){
        for(i in 0 until fragments.size){
            if(i == num)
                supportFragmentManager.beginTransaction().show(fragments[i]).commit()
            else
                supportFragmentManager.beginTransaction().hide(fragments[i]).commit()
        }
    }
}