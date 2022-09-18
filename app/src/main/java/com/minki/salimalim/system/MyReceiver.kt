package com.minki.salimalim.system

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.minki.salimalim.MainActivity
import com.minki.salimalim.R
import com.minki.salimalim.SqlHelper
import kotlinx.android.synthetic.main.manage_recycler.view.*

class MyReceiver : BroadcastReceiver() {

    lateinit var notificationManager : NotificationManager

    override fun onReceive(context: Context, intent: Intent) {
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
        createNotificationChannel()
        deliverNotification(context)
    }

    private fun deliverNotification(context: Context) {
        val contentIntent = Intent(context, MainActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(
            context,
            1004, // requestCode
            contentIntent, // 알림 클릭 시 이동할 인텐트
            PendingIntent.FLAG_MUTABLE
            /*
            1. FLAG_UPDATE_CURRENT : 현재 PendingIntent를 유지하고, 대신 인텐트의 extra data는 새로 전달된 Intent로 교체
            2. FLAG_CANCEL_CURRENT : 현재 인텐트가 이미 등록되어있다면 삭제, 다시 등록
            3. FLAG_NO_CREATE : 이미 등록된 인텐트가 있다면, null
            4. FLAG_ONE_SHOT : 한번 사용되면, 그 다음에 다시 사용하지 않음
             */
        )
        val sqlHelper = SqlHelper(context,"manage_table",null,1)
        val items = sqlHelper.selectManage()
        var text = ""
        var min = 1000
        var daysLeft = 0
        var icon = R.drawable.ic_launcher_background
        for(item in items){
            daysLeft = (item.usedTerm * item.quantity) - CommonActivity().getTime(item.purchaseDate)
            if (daysLeft < min) {
                min = daysLeft
                text = "${CommonActivity.goods[item.goodsName-1].goodsName}의 예상 기한이 "
                when (item.category) {
                    1 -> icon = R.drawable.grocery
                    2 -> icon = R.drawable.restroom
                    3 -> icon = R.drawable.household
                    4 -> icon = R.drawable.kitchen
                }
            }
        }
        if(min > 10)
            return
        text += if(min < 0)
            "${Math.abs(min)}일 경과되었습니다."
        else
            "${min}일 남았습니다."

        val builder = NotificationCompat.Builder(context,"Salimalim")
            .setSmallIcon(icon) // 아이콘
            .setContentTitle("살림을 확인하세요!") // 제목
            .setContentText(text) // 내용
            .setContentIntent(contentPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        notificationManager.notify(1004, builder.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                "Salimalim", // 채널의 아이디
                "채널 이름입니다.", // 채널의 이름
                NotificationManager.IMPORTANCE_HIGH
                /*
                1. IMPORTANCE_HIGH = 알림음이 울리고 헤드업 알림으로 표시
                2. IMPORTANCE_DEFAULT = 알림음 울림
                3. IMPORTANCE_LOW = 알림음 없음
                4. IMPORTANCE_MIN = 알림음 없고 상태줄 표시 X
                 */
            )
            notificationChannel.enableLights(true) // 불빛
            notificationChannel.lightColor = Color.RED // 색상
            notificationChannel.enableVibration(true) // 진동 여부
            notificationChannel.description = "채널의 상세정보입니다." // 채널 정보
            notificationManager.createNotificationChannel(
                notificationChannel)
        }
    }
}