package com.work.ticketbook.retrofit

import com.work.ticketbook.model.SeatAvailabilityModel
import com.work.ticketbook.utility.API_SEAT_INFO
import com.work.ticketbook.utility.CONTENT_TYPE
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * @author: Harpreet Kaur
 *
 * API Interface to define ALL APIS
 */
interface ApiInterface {

    @Headers(CONTENT_TYPE)
    @GET(API_SEAT_INFO)
    fun getSeatInfo(@Query("movieName") movieName: String,
                    @Query("cinemaName") cinemaName: String,
                    @Query("city") city: String,
                    @Query("showTime") showTime: String,
                    @Query("reqSeatCount") reqSeatCount: String) : Call<SeatAvailabilityModel>



}