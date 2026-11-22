package com.example.myapplication.data.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.myapplication.domain.repository.ReminderScheduler

/** Ставит напоминания через AlarmManager (приблизительное время, без специальных разрешений). */
class AndroidReminderScheduler(private val context: Context) : ReminderScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun scheduleProjectInactivity(projectTitle: String) = schedule(
        requestCode = CODE_PROJECT,
        delayMs = 3 * 60_000L,
        title = "Вы давно не заходили в проект",
        text = "Проект «$projectTitle» ждёт вас"
    )

    override fun scheduleAppInactivity() = schedule(
        requestCode = CODE_APP,
        delayMs = 60_000L,
        title = "Matule скучает по вам",
        text = "Вы давно не открывали приложение"
    )

    override fun cancelAppInactivity() = cancel(CODE_APP)

    override fun cancelAll() {
        cancel(CODE_APP)
        cancel(CODE_PROJECT)
    }

    private fun schedule(requestCode: Int, delayMs: Long, title: String, text: String) {
        val intent = Intent(context, ReminderReceiver::class.java)
            .putExtra(ReminderReceiver.EXTRA_TITLE, title)
            .putExtra(ReminderReceiver.EXTRA_TEXT, text)
            .putExtra(ReminderReceiver.EXTRA_ID, requestCode)
        val pending = PendingIntent.getBroadcast(
            context, requestCode, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delayMs, pending)
    }

    private fun cancel(requestCode: Int) {
        val pending = PendingIntent.getBroadcast(
            context, requestCode, Intent(context, ReminderReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        pending?.let {
            alarmManager.cancel(it)
            it.cancel()
        }
    }

    private companion object {
        const val CODE_APP = 1
        const val CODE_PROJECT = 2
    }
}