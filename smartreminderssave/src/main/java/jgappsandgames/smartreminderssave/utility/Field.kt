package jgappsandgames.smartreminderssave.utility

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.getOrElse

internal class Field<T>(var timeStamp: Long, var data: Option<T>, var default: T) {
    fun get(): T {
        return data.getOrElse { default }
    }

    fun getOptional(): Option<T> {
        return data
    }

    fun hasValue(): Boolean {
        return data !is None
    }

    fun setValue(value: T) {
        data = Some(value)
    }

    fun setValue(value: T, timeStamp: Long) {
        data = Some(value)
        this.timeStamp = timeStamp
    }
}