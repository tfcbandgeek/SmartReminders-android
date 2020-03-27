package jgappsandgames.smartreminderssave.repeats

import jgappsandgames.me.poolutilitykotlin.PoolObjectCreator
import jgappsandgames.me.poolutilitykotlin.PoolObjectInterface
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class RepeatData: PoolObjectInterface {
    companion object {
        private const val FILENAME = "a"
        private const val META = "b"
        private const val ID = "c"

        private const val STATUS = "d"
        private const val DOW = "e"
        private const val WOM = "f"
        private const val DOM = "g"

        const val NONE: Byte = 0
        const val DAILY: Byte = 1
        const val WEEKLY: Byte = 1 shl 1
        const val MONTHLY: Byte = 1 shl 2
        const val WEEK_MONTHLY: Byte = 1 shl 3
        const val YEARLY: Byte = 1 shl 4

        const val MONDAY: Byte = 1
        const val TUESDAY: Byte = 1 shl 1
        const val WEDNESDAY: Byte = 1 shl 2
        const val THURSDAY: Byte = 1 shl 3
        const val FRIDAY: Byte = 1 shl 4
        const val SATURDAY: Byte = 1 shl 5
        const val SUNDAY: Byte = 1 shl 6

        const val Z_MONDAY: Long = 1
        const val Z_TUESDAY: Long = 1 shl 1
        const val Z_WEDNESDAY: Long = 1 shl 2
        const val Z_THURSDAY: Long = 1 shl 3
        const val Z_FRIDAY: Long = 1 shl 4
        const val Z_SATURDAY: Long = 1 shl 5
        const val Z_SUNDAY: Long = 1 shl 6
        const val F_MONDAY: Long = 1 shl 10
        const val F_TUESDAY: Long = 1 shl 11
        const val F_WEDNESDAY: Long = 1 shl 12
        const val F_THURSDAY: Long = 1 shl 13
        const val F_FRIDAY: Long = 1 shl 14
        const val F_SATURDAY: Long = 1 shl 15
        const val F_SUNDAY: Long = 1 shl 16
        const val S_MONDAY: Long = 1 shl 2
        const val S_TUESDAY: Long = 1 shl 21
        const val S_WEDNESDAY: Long = 1 shl 22
        const val S_THURSDAY: Long = 1 shl 23
        const val S_FRIDAY: Long = 1 shl 24
        const val S_SATURDAY: Long = 1 shl 25
        const val S_SUNDAY: Long = 1 shl 26
        const val T_MONDAY: Long = 1 shl 30
        const val T_TUESDAY: Long = 1 shl 31
        const val T_WEDNESDAY: Long = 1 shl 32
        const val T_THURSDAY: Long = 1 shl 33
        const val T_FRIDAY: Long = 1 shl 34
        const val T_SATURDAY: Long = 1 shl 35
        const val T_SUNDAY: Long = 1 shl 36
        const val O_MONDAY: Long = 1 shl 40
        const val O_TUESDAY: Long = 1 shl 41
        const val O_WEDNESDAY: Long = 1 shl 42
        const val O_THURSDAY: Long = 1 shl 43
        const val O_FRIDAY: Long = 1 shl 44
        const val O_SATURDAY: Long = 1 shl 45
        const val O_SUNDAY: Long = 1 shl 46
        const val I_MONDAY: Long = 1 shl 50
        const val I_TUESDAY: Long = 1 shl 51
        const val I_WEDNESDAY: Long = 1 shl 52
        const val I_THURSDAY: Long = 1 shl 53
        const val I_FRIDAY: Long = 1 shl 54
        const val I_SATURDAY: Long = 1 shl 55
        const val I_SUNDAY: Long = 1 shl 56
    }

    // Data ----------------------------------------------------------------------------------------
    private var filename: String? = null
    private var meta: JSONObject? = null
    private var id: Int = 0

    private var status: Byte = 0
    private var dow: Byte = 0
    private var wom: Long = 0
    private var dom = ArrayList<Int>(4)

    private var till: Calendar? = null

    private var past: ArrayList<String> = ArrayList()
    private var template: String? = null
    private var future: ArrayList<String> = ArrayList()

    // Management Methods --------------------------------------------------------------------------
    fun create(): RepeatData {
        return this
    }

    fun load(filename: String): RepeatData {
        return this
    }

    fun load(data: JSONObject): RepeatData {
        return this
    }

    fun save(): RepeatData {
        return this
    }

    fun delete(): Boolean {
        return false
    }

    override fun deconstruct() {}

    // Getters -------------------------------------------------------------------------------------
    fun getFilename(): String = filename ?: ""

    fun getMeta(): JSONObject {
        if (meta == null) {
            meta = JSONObject()
        }

        return meta!!
    }

    override fun getID(): Int = id

    fun getStatus(): Byte = status

    fun getDOW(): Byte = dow

    fun getWOM(): Long = wom

    fun getDOM(): ArrayList<Int> = dom

    fun getTill(): Calendar? = till

    fun getPastTasks(): ArrayList<String> = past

    fun getTemplateTask(): String {
        if (template == null) {
            throw NullPointerException()
        } else {
            return template!!
        }
    }

    fun getFutureTasks(): ArrayList<String> = future

    // Setters -------------------------------------------------------------------------------------
    fun setMeta(data: JSONObject): RepeatData {
        meta = data
        return this
    }

    fun setStatus(nStatus: Byte): RepeatData {
        status = nStatus
        return this
    }

    fun setDOW(nDOW: Byte): RepeatData {
        dow = nDOW
        return this
    }

    fun setWOM(nWOM: Long): RepeatData {
        wom = nWOM
        return this
    }

    fun setDOM(nDOM: ArrayList<Int>): RepeatData {
        dom = nDOM
        return this
    }

    fun update(to: Calendar? = null): RepeatData {
        val t: Calendar
        if (to == null) {
            t = Calendar.getInstance()
            t.add(Calendar.WEEK_OF_YEAR, 1)
        } else {
            t = to
        }

        // TODO: Implement Check Methods

        return this
    }
}

class RepeatableCreator: PoolObjectCreator<RepeatData> {
    override fun generatePoolObject(): RepeatData {
        return RepeatData()
    }
}