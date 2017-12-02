package jgappsandgames.smartreminderssave.date

import jgappsandgames.smartreminderssave.tasks.Task
import jgappsandgames.smartreminderssave.tasks.TaskManager
import java.util.*

/**
 * DateManager
 * Created by joshua on 12/1/2017.
 */
class DateManager {
    companion object {
        // Data
        @JvmStatic
        private var weeks: ArrayList<KeyValue>? = null
        @JvmStatic
        private var months: ArrayList<KeyMonth>? = null

        // Management Methods
        @JvmStatic
        fun create() {
            if (TaskManager.tasks == null) throw RuntimeException("TaskManager needs to be loaded before the DateManager can do any work")

            val tasks = ArrayList<Task>()
            for (t in TaskManager.tasks) {
                val task = Task(t)
                if (task.getDateDue() != null) tasks.add(task)
            }

            val calendar = Calendar.getInstance()
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)

            weeks = ArrayList()
            for (i in 0..9) {
                weeks!!.add(KeyValue(i, Week(calendar.clone() as Calendar)))
                calendar.add(Calendar.WEEK_OF_YEAR, 1)
            }

            for (task in tasks) {
                if (task.getDateDue().before(weeks!![0].week.getStart())) continue
                var i = 0
                var b = true

                while (b) {
                    if (weeks!![i].week.addTask(task)) b = false

                    if (i > weeks!!.size - 10) {
                        val t = Calendar.getInstance()
                        t.add(Calendar.WEEK_OF_YEAR, weeks!!.size)
                        weeks!!.add(KeyValue(weeks!!.size, Week(t)))
                    }

                    if (i >= 52) b = false
                    i++
                }
            }

            val marker = Calendar.getInstance()
            marker.set(Calendar.DAY_OF_MONTH, 1)
            marker.set(Calendar.HOUR_OF_DAY, 0)
            marker.set(Calendar.MINUTE, 0)
            marker.set(Calendar.SECOND, 1)

            months = ArrayList()
            for (i in 0..11) {
                months!!.add(KeyMonth(i, Month(marker.clone() as Calendar)))
                marker.add(Calendar.MONTH, 1)
            }

            for (task in tasks) {
                if (task.getDateDue().before(months!![0].month.getStart())) continue
                var i = 0
                var b = true

                while (b) {
                    if (months!![i].month.addTask(task)) b = false

                    if (i > months!!.size - 2) {
                        val t = Calendar.getInstance()
                        t.add(Calendar.MONTH, months!!.size)
                        months!!.add(KeyMonth(months!!.size, Month(t)))
                    }

                    if (i >= 24) b = false
                    i++
                }
            }
        }

        // Getters
        @JvmStatic
        fun getDayCount(): Int = getWeekCount() * 7

        @JvmStatic
        fun getWeekCount(): Int = weeks!!.size

        @JvmStatic
        fun getMonthCount(): Int = months!!.size

        @JvmStatic
        fun getWeek(week: Int): Week {
            if (weeks == null) create()
            for (w in weeks!!) if (w.key == week) return w.week

            // Todo: Return Special Case
            return Week(Calendar.getInstance())
        }

        @JvmStatic
        fun getMonth(month: Int): Month {
            if (months == null) create()
            for (m in months!!) if (m.key == month) return m.month

            // Todo: Return Special Case
            return Month(Calendar.getInstance())
        }

        @JvmStatic
        fun getToday(): Day = getWeek(0).getDay(Calendar.getInstance())

        @JvmStatic
        fun getDay(date_active: Calendar): ArrayList<Task> {
            if (date_active.before(getWeek(0).getStart())) return ArrayList()

            if (weeks == null) create()
            for (week in weeks!!) if (week.week.getStart().before(date_active) && week.week.getEnd().after(date_active)) return week.week.getDay(date_active).getTasks()

            return ArrayList()
        }

        // Classes
        private class KeyValue(val key: Int, val week: Week)

        private class KeyMonth(val key: Int, val month: Month)
    }
}