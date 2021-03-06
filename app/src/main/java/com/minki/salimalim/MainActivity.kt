package com.minki.salimalim

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.minki.salimalim.manage.ManageFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.minki.salimalim.shop.ShopFragment
import com.minki.salimalim.system.CommonActivity
import com.minki.salimalim.system.MyReceiver

class MainActivity : CommonActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val currentNum = 0
    private var fragments = ArrayList<Fragment>()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val manageFragment = ManageFragment()
        val shopFragment = ShopFragment()
        val communityFragment = CommunityFragment()

        fragments.add(manageFragment)
        fragments.add(shopFragment)
        fragments.add(communityFragment)

        for (i in 0 until fragments.size)
            supportFragmentManager.beginTransaction().add(R.id.Main_Frame, fragments[i]).commit()
        selectFragment(0)
        supportFragmentManager.beginTransaction().show(fragments[0]).commit()
        supportFragmentManager.beginTransaction().hide(fragments[1]).commit()
        supportFragmentManager.beginTransaction().hide(fragments[2]).commit()
        findViewById<BottomNavigationView>(R.id.Main_Bottom_Navigation).setOnNavigationItemSelectedListener(
            this
        )


        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, MyReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this, 1004, intent,
            PendingIntent.FLAG_MUTABLE
        )

        val triggerTime = (SystemClock.elapsedRealtime() // ????????? ????????? ??? ????????? ?????? ??????
                + 3 * 1000) // ms ?????? ????????? ???????????? ?????? (*1000)
        alarmManager.set(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            triggerTime,
            pendingIntent
        ) // set : ????????? ??????
        "3 ??? ?????? ????????? ???????????????."
        Log.v("??????????","??????????????????'")


//        val repeatInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES
//        /*
//        1. INTERVAL_FIFTEEN_MINUTES : 15???
//        2. INTERVAL_HALF_HOUR : 30???
//        3. INTERVAL_HOUR : 1??????
//        4. INTERVAL_HALF_DAY : 12??????
//        5. INTERVAL_DAY : 1???
//         */
//        val triggerTime = (SystemClock.elapsedRealtime()
//                + repeatInterval)
//        alarmManager.setInexactRepeating(
//            AlarmManager.ELAPSED_REALTIME_WAKEUP,
//            triggerTime, repeatInterval,
//            pendingIntent
//        ) // setInexactRepeating : ?????? ??????
//        /*
//        1. ELAPSED_REALTIME : ELAPSED_REALTIME ??????. ??????????????? ?????? ?????? ????????? ??????????????? ?????? ???????????? ????????????.
//        2. ELAPSED_REALTIME_WAKEUP : ELAPSED_REALTIME ??????. ??????????????? ?????? ????????? ????????????.
//        3. RTC : Real Time Clock ??????. ??????????????? ?????? ????????? ??????????????? ??????.
//        4. RTC_WAKEUP : Real Time Clock ??????. ???????????? ??? ?????? ????????? ????????????.
//         */
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

    fun selectFragment(num : Int){
        for(i in 0 until fragments.size){
            if(i == num)
                supportFragmentManager.beginTransaction().show(fragments[i]).commit()
            else
                supportFragmentManager.beginTransaction().hide(fragments[i]).commit()
        }
    }
}