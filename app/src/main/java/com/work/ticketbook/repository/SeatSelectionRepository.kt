package com.work.ticketbook.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.work.ticketbook.R
import com.work.ticketbook.model.SeatAvailabilityModel
import com.work.ticketbook.model.getDisplayShowTime
import com.work.ticketbook.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
/**
 * @Author : Harpreet Kaur
 * Repository class for seat selection for adding API and DB Calls
 */
object SeatSelectionRepository {

    val seatAvailabilityModel = MutableLiveData<SeatAvailabilityModel>()

    /**
      * Pass the request para,meter to server fetch the data
      *
      * Currently its mock api, only passing to show the implementation
     */

    fun getSeatAvailabilityAPICall(request: SeatAvailabilityModel): MutableLiveData<SeatAvailabilityModel> {

        val call = request.movieName?.let {
            request.cinemaName?.let { it1 ->
                request.city?.let { it2 ->
                    request.showTime?.let { it3 ->
                        request.reqSeatCount?.let { it4 ->
                            RetrofitClient.apiInterface.getSeatInfo(
                                it, it1,
                                it2, it3, it4
                            )
                        }
                    }
                }
            }
        }

        call?.enqueue(object : Callback<SeatAvailabilityModel> {

            override fun onFailure(call: Call<SeatAvailabilityModel>, t: Throwable) {

                /**
                 * Setting the error message for API fail scenario
                 */
                val errorSeatInfo = SeatAvailabilityModel()
                errorSeatInfo.actionableErrorSubLabel = R.string.error_msg_1003
                seatAvailabilityModel.value = errorSeatInfo

            }

            override fun onResponse(
                call: Call<SeatAvailabilityModel>,
                response: Response<SeatAvailabilityModel>
            ) {

                Log.v("DEBUG : ", response.body().toString())

                /**
                 * Setting the Seat availability data on model object
                 * you can process the response according to UI model , mock is same only
                 */
                val data = response.body()
                seatAvailabilityModel.value = SeatAvailabilityModel(
                    data?.movieName, data?.cinemaName, data?.city,
                    data?.getDisplayShowTime(), data?.reqSeatCount, data?.seatInfo, 0
                )
            }
        })

        return seatAvailabilityModel
    }
}