@file:JvmName("Helper")

package com.work.ticketbook.utility

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 *  @author: Harpreet Kaur
 *
 * * This file contains general purpose helper classes and methods
 *
 */

class VmwException(val code: Int = 0, val errorMsg: String): Exception(errorMsg)

fun getFormattedDate(
    oldFormat: String,
    newFormat: String,
    theDate: String?
): String? {

    if (theDate.isNullOrEmpty())
        return null

    val oldDateFormat: SimpleDateFormat? = getSimpleDateFormat(oldFormat)
    val newDateFormat: SimpleDateFormat? = getSimpleDateFormat(newFormat)

    if (oldDateFormat == null || newDateFormat == null)
        return null

    val date: Date = try {
        oldDateFormat.parse(theDate)
    } catch (e: ParseException) {
        null
    } ?: return null

    return try {
        newDateFormat.format(date)
    } catch (e: ParseException) {
        null
    }
}

private fun getSimpleDateFormat(
    format: String?,
    locale: Locale = Locale.getDefault(),
    timeZone: TimeZone = TimeZone.getTimeZone("Asia/Kolkata")
): SimpleDateFormat? = try {
    SimpleDateFormat(format, locale).apply {
        this.timeZone = timeZone
    }
} catch (e : IllegalArgumentException) {
    null
}