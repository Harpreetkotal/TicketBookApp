package com.work.ticketbook.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.work.ticketbook.model.SeatAvailabilityModel
import com.work.ticketbook.repository.SeatSelectionRepository.getSeatAvailabilityAPICall

/**
 *@Author : Harpreet kaur
 *
 * viewModel Class for Seat Selection Screen
 * define any live data objects for API response
 * Scale : we can add as many apis here according to the requirement
 *
 */

class SeatSelectionViewModel : ViewModel() {

    var servicesLiveData: MutableLiveData<SeatAvailabilityModel>? = null

    lateinit var requestModel : SeatAvailabilityModel;

    val requestSeatTotalCount = 2;

    /**
      * invoking the API through repository
     */
    fun getSeatInfoData() : LiveData<SeatAvailabilityModel>? {
        servicesLiveData = getSeatAvailabilityAPICall(requestModel)
        return servicesLiveData
    }

    /**
     * Method to set the API request for seat availability API
     */
    fun setSeatInfoRequestData(
        movieName: String,
        cinemaName: String,
        city: String,
        showTime: String
    ) {
        requestModel = SeatAvailabilityModel(
        movieName,
        cinemaName,
        city,
            showTime,
        requestSeatTotalCount.toString()
        )
    }

}