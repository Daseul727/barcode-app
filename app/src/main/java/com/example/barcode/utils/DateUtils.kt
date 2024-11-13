package com.example.barcode.utils

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Class: DateUtils
 * Description:
 */
object DateUtils {
    // 화면에 날짜를 표시하는 용도의 포매터
    @JvmStatic
    val VIEW_DATE_FORMAT = "yyyy-MM-dd"
    @JvmStatic
    val viewFormatter = SimpleDateFormat(VIEW_DATE_FORMAT, Locale.getDefault())

    // 서버에 날짜를 전송하는 포매터
    @JvmStatic
    val SEND_DATE_FORMAT = "yyyyMMdd"
    @JvmStatic
    val sendFormatter = SimpleDateFormat(SEND_DATE_FORMAT, Locale.getDefault())

    /**
     * 오늘 날짜를 반환
     */
    @JvmStatic
    fun getToday(): String {
        val c = Calendar.getInstance()
        return viewFormatter.format(c.time)
    }

    /**
     * 오늘 날짜를 반환
     */
    @JvmStatic
    fun getDate(addDay: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, addDay) //변경하고 싶은 원하는 날짜 수를 넣어 준다.
        return viewFormatter.format(calendar.time)
    }

    // 날짜 비교
    // 날짜가 같으면 0
    // 시작 날짜가 크면 -1
    // 시작 날짜가 작으면 1
    @JvmStatic
    fun dateCompare(sDate: Calendar, eDate: Calendar): Int {
        // 시분초 초기화
        sDate.set(Calendar.HOUR_OF_DAY, 0)
        sDate.set(Calendar.MINUTE, 0)
        sDate.set(Calendar.SECOND, 0)
        // 시분초 초기화
        eDate.set(Calendar.HOUR_OF_DAY, 0)
        eDate.set(Calendar.MINUTE, 0)
        eDate.set(Calendar.SECOND, 0)

        if (sDate == eDate) {
            return 0
        }
        else if (sDate > eDate) {
            return -1
        }

        return 1
    }

    /**
     * 밀리세컨드 값을 mm:ss 형태로 변환
     *
     * @param milliSeconds
     * @return : mm:ss 형태로 리턴
     */
    @JvmStatic
    fun convertTommss(milliSeconds: Long): String {
        //val day = TimeUnit.SECONDS.toDays(milliSeconds)
        //val hours: Long = TimeUnit.SECONDS.toHours(milliSeconds) - day * 24
        val minute: Long = TimeUnit.SECONDS.toMinutes(milliSeconds) - TimeUnit.SECONDS.toHours(milliSeconds) * 60
        val second: Long = TimeUnit.SECONDS.toSeconds(milliSeconds) - TimeUnit.SECONDS.toMinutes(milliSeconds) * 60

        val minuteText: String = if (minute < 10) "0$minute" else "$minute"
        val secondText: String = if (second < 10) "0$second" else "$second"

        //LogUtils.debug(LOG_TAG, "Day $day Hour $hours Minute $minute Seconds $second")
        return "$minuteText:$secondText"
    }

    /**
     * 현재 시간
     *
     * @return : yyyyMMddHHmmss
     */
    @JvmStatic
    val currentTime4: String
        get() {
            val calendar = Calendar.getInstance()
            return SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(calendar.time)
        }

    @JvmStatic
    fun convertDate(timestamp: String): String {
        return timestamp.substring(0, 4) + "-" + timestamp.substring(4, 6) + "-" + timestamp.substring(6, timestamp.length)
    }
}

fun Calendar.getFormatDate(): String {
    return DateUtils.viewFormatter.format(this.time)
}