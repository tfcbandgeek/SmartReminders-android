package jgappsandgames.me.save.utility

// Version 1.0.0+1
// 8/19/2018

fun divideString(string: String, number: Int): ArrayList<String> {
    val array = ArrayList<String>(number)
    val split: Int = string.length / number

    for (i in 0 until number) {
        if (i == (number - 1)) array.add(string.substring(split * i))
        else array.add(string.substring(split * i, split * (i + 1)))
    }

    return array
}