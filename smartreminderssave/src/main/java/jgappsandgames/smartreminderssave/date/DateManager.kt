package jgappsandgames.smartreminderssave.date

// Java
import java.util.Calendar

// Kotlin
import kotlin.collections.ArrayList

// Save Code
import jgappsandgames.smartreminderssave.tasks.Task
import jgappsandgames.smartreminderssave.tasks.TaskManager

// Log Code
import me.jgappsandgames.openlog.Exception
import me.jgappsandgames.openlog.Log

/**
 * DateManager
 * Created by joshua on 12/12/2017.
 *
 * Sorts Tasks Based on Their Date Due
 * TODO: Save Sorted Tasks
 * TODO: Improve Sort Efficiency
 * TODO: Split into Week & Month
 */
class DateManager {
    companion object {
        // Data ------------------------------------------------------------------------------------
        private var weeks: ArrayList<KeyWeek>? = null
        private var months: ArrayList<KeyMonth>? = null

        // Management Methods ----------------------------------------------------------------------
        /**
         * Create
         *
         * Called to Sort All of the Tasks
         */
        @JvmStatic
        fun create() {
            Log.d("DateManager", "Create Called")
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
                weeks!!.add(KeyWeek(i, Week(calendar.clone() as Calendar)))
                calendar.add(Calendar.WEEK_OF_YEAR, 1)
            }

            for (task in tasks) {
                if (task.getDateDue()!!.before(weeks!![0].week.getStart())) continue
                var i = 0
                var b = true

                while (b) {
                    if (weeks!![i].week.addTask(task)) b = false

                    if (i > weeks!!.size - 10) {
                        val t = Calendar.getInstance()
                        t.add(Calendar.WEEK_OF_YEAR, weeks!!.size)
                        weeks!!.add(KeyWeek(weeks!!.size, Week(t)))
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
                if (task.getDateDue()!!.before(months!![0].month.getStart())) continue
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

        // Getters ---------------------------------------------------------------------------------
        /**
         * GetDayCount
         *
         * @return The number of Days loaded (As seen from the Week View)
         */
        @JvmStatic
        fun getDayCount(): Int {
            Log.d("DateManager", "GetDayCount Called")
            return getWeekCount() * 7
        }

        /**
         * GetToday (Depricated)
         *
         * @return Today's Day
         */
        @Deprecated("Removal in 11.1 or 11.2")
        @JvmStatic
        fun getToday(): Day {
            Exception.f("DateManager", "GetToday Depricated")
            Log.d("DateManager", "GetToday Called")
            return getWeek(0).getDay(Calendar.getInstance())
        }

        /**
         * GetDay (Depricated)
         *
         * This Method Will Soon Return The Day Object
         *
         * @param date_active The Day We Want to Get
         * @return The Tasks For This Day
         */
        @Deprecated("Will Soon Return the Day Object (11.1 or 11.2")
        @JvmStatic
        fun getDay(date_active: Calendar): ArrayList<Task> {
            Exception.f("DateManager", "GetDay Depricated")
            Log.d("DateManager", "GetDay Called")
            if (date_active.before(getWeek(0).getStart())) return ArrayList()

            if (weeks == null) create()
            for (week in weeks!!) {
                if (week.week.getStart().before(date_active) && week.week.getEnd().after(date_active)) return week.week.getDay(date_active).tasks
            }

            return ArrayList()
        }

        /**
         * GetDayObject
         *
         * @param date_active The Day We Want to Get
         * @return The Day we Want to Get
         */
        @JvmStatic
        fun getDayObject(date_active: Calendar): Day {
            Log.d("DateManager", "GetDayObject Called")
            if (date_active.before(getWeek(0).getStart())) return Day(date_active)

            if (weeks == null) create()
            for (week in weeks!!) {
                if (week.week.getStart().before(date_active) && week.week.getEnd().after(date_active)) return week.week.getDay(date_active)
            }

            return Day(date_active)
        }

        /**
         * GetDayTasks
         *
         * @param date_active The Day We Want to Get
         * @return The Tasks For This Day
         */
        @JvmStatic
        fun getDayTasks(date_active: Calendar): ArrayList<Task> {
            Log.d("DateManager", "GetDayTasks Called")
            if (date_active.before(getWeek(0).getStart())) return ArrayList()

            if (weeks == null) create()
            for (week in weeks!!) {
                if (week.week.getStart().before(date_active) && week.week.getEnd().after(date_active)) return week.week.getDay(date_active).tasks
            }

            return ArrayList()
        }

        /**
         * GetWeekCount
         *
         * @return The Number of Weeks Loaded
         */
        @JvmStatic
        fun getWeekCount(): Int {
            Log.d("DateManager", "GetWeekCount Called")
            return weeks!!.size
        }

        /**
         * GetWeek(int)
         *
         * @param week The Week We Want to Get
         * @return The Week we Wanted
         */
        @JvmStatic
        fun getWeek(week: Int): Week {
            Log.d("DateManager", "GetWeek Called")
            if (weeks == null) create()
            for (w in weeks!!) if (w.key == week) return w.week

            // Todo: Return Special Case
            return Week(Calendar.getInstance())
        }

        /**
         * GetWeekTaasks(int)
         *
         * @param week The Week We Want to Get
         * @return The WTasks for this Week
         */
        @JvmStatic
        fun getWeekTasks(week: Int): ArrayList<Task> {
            Log.d("DateManager", "GetWeekTasks Called")
            if (weeks == null) create()
            for (w in weeks!!) if (w.key == week) return w.week.getAllTasks()

            // Todo: Return Special Case
            return ArrayList()
        }

        /**
         * GetMonthCount
         *
         * @return The Number of Months Loaded
         */
        @JvmStatic
        fun getMonthCount(): Int {
            Log.d("DateManager", "GetMonthCount Called")
            return months!!.size
        }

        /**
         * GetMonth(int)
         *
         * @param month The Month We Want to Get
         * @return The Month we Wanted
         */
        @JvmStatic
        fun getMonth(month: Int): Month {
            Log.d("DateManager", "GetMonth Called")
            if (months == null) create()
            for (m in months!!) if (m.key == month) return m.month

            // Todo: Return Special Case
            return Month(Calendar.getInstance())
        }

        /**
         * GetMonthTasks(int)
         *
         * @param month The Month We Want to Get
         * @return The Tasks of the Month
         */
        @JvmStatic
        fun getMonthTasks(month: Int): ArrayList<Task> {
            Log.d("DateManager", "GetMonthTasks Called")
            if (months == null) create()
            for (m in months!!) if (m.key == month) return m.month.getAllTasks()

            // Todo: Return Special Case
            return ArrayList()
        }

        // Classes ---------------------------------------------------------------------------------
        /**
         * KeyWeek Class
         *
         * Class used to Store and Sort Weeks
         */
        private class KeyWeek(val key: Int, val week: Week)

        /**
         * KeyMonth Class
         *
         * Class used to Store and Sort Months
         */
        private class KeyMonth(val key: Int, val month: Month)
    }
}