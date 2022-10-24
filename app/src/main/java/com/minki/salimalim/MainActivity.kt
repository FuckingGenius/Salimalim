package com.minki.salimalim

import android.app.ActionBar
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.minki.salimalim.manage.ManageFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.minki.salimalim.shop.ShopFragment
import com.minki.salimalim.system.CommonActivity
import com.minki.salimalim.system.MyReceiver
import kotlinx.android.synthetic.main.activity_main.*

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


        heightPixels = resources.displayMetrics.heightPixels
        widthPixels = resources.displayMetrics.widthPixels
        xdpi = resources.displayMetrics.widthPixels
        ydpi = resources.displayMetrics.heightPixels

        Main_Bottom_Navigation.layoutParams .height = (ydpi * navigationSize).toInt()
        Log.v("아아아", Main_Bottom_Navigation.layoutParams.height.toString())

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, MyReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(

            this, 1004, intent,
            PendingIntent.FLAG_MUTABLE
        )

        val triggerTime = (SystemClock.elapsedRealtime() // 기기가 부팅된 후 경과한 시간 사용
                + 3 * 1000) // ms 이기 때문에 초단위로 변환 (*1000)
        alarmManager.set(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            triggerTime,
            pendingIntent
        ) // set : 일회성 알림
        "3 초 후에 알림이 발생합니다."
        Log.v("받긴함?","가긴하는걸까'")


//        val repeatInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES
//        /*
//        1. INTERVAL_FIFTEEN_MINUTES : 15분
//        2. INTERVAL_HALF_HOUR : 30분
//        3. INTERVAL_HOUR : 1시간
//        4. INTERVAL_HALF_DAY : 12시간
//        5. INTERVAL_DAY : 1일
//         */
//        val triggerTime = (SystemClock.elapsedRealtime()
//                + repeatInterval)
//        alarmManager.setInexactRepeating(
//            AlarmManager.ELAPSED_REALTIME_WAKEUP,
//            triggerTime, repeatInterval,
//            pendingIntent
//        ) // setInexactRepeating : 반복 알림
//        /*
//        1. ELAPSED_REALTIME : ELAPSED_REALTIME 사용. 절전모드에 있을 때는 알람을 발생시키지 않고 해제되면 발생시킴.
//        2. ELAPSED_REALTIME_WAKEUP : ELAPSED_REALTIME 사용. 절전모드일 때도 알람을 발생시킴.
//        3. RTC : Real Time Clock 사용. 절전모드일 때는 알람을 발생시키지 않음.
//        4. RTC_WAKEUP : Real Time Clock 사용. 절전모드 일 때도 알람을 발생시킴.
//         */
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return if(currentNum == item.itemId)
            false
        else{
            when(item.itemId){
                R.id.manage -> selectFragment(0)
                R.id.shop -> selectFragment(1)
//                R.id.community -> selectFragment(2)
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