package com.work.ticketbook.model

import com.work.ticketbook.utility.EMPTY
import com.work.ticketbook.utility.HYPHEN
import com.work.ticketbook.utility.getFormattedDate

/**
 * @author: Harpreet Kaur
 *
 * API request response data models
 */
data class SeatAvailabilityModel (
    val movieName: String? = null,
    val cinemaName:String? =null,
    val city:String? = null,
    val showTime:String? = null,
    val reqSeatCount:String? =null,
    val seatInfo : SeatInfo? =null,
    var actionableErrorSubLabel: Int = 0

)

data class SeatInfo(
    val seatRows: List<SeatRow?>? = null
)

data class SeatRow(
    val rowId: String?,
    val rowLabel: String?,
    val maxSeatCount: String?,
    val seats: List<SeatData?>? = null
)

data class SeatData(
    val seatNo: String?,
    var status: String?
)

private const val SLOT_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
private const val SLOT_UI_DATE_FORMAT = "EEEE, MMM YY"
private const val SLOT_UI_TIME_FORMAT = "hh:mm a"

/**
    setting the display date time for UI
 */

fun SeatAvailabilityModel.getDisplayShowTime(): String = showTime?.let { showTime ->

    val showTimeDate = showTime ?: EMPTY

    if (showTimeDate.isEmpty())
        return HYPHEN

    val date = getFormattedDate(SLOT_DATE_TIME_FORMAT, SLOT_UI_DATE_FORMAT, showTimeDate)

    if (date.isNullOrEmpty())
        return HYPHEN

    val showStartTime: String =
        getFormattedDate(SLOT_DATE_TIME_FORMAT, SLOT_UI_TIME_FORMAT, showTimeDate) ?: EMPTY


    return "$date , $showStartTime "

} ?: HYPHEN