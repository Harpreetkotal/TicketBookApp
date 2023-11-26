package com.work.ticketbook.view.activity

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.work.ticketbook.R
import com.work.ticketbook.model.SeatData
import com.work.ticketbook.model.SeatInfo
import com.work.ticketbook.model.SeatRow
import com.work.ticketbook.model.getDisplayShowTime
import com.work.ticketbook.utility.AVAILABLE
import com.work.ticketbook.utility.Messages
import com.work.ticketbook.utility.SELECTED
import com.work.ticketbook.utility.SOLD
import com.work.ticketbook.viewmodel.SeatSelectionViewModel
import kotlinx.android.synthetic.main.actionbar_header.view.img_option
import kotlinx.android.synthetic.main.actionbar_header.view.req_ticket_count
import kotlinx.android.synthetic.main.actionbar_header.view.txt_header
import kotlinx.android.synthetic.main.actionbar_header.view.txt_sub_header
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author: Harpreet Kaur
 *
 * Activity Class : Seat Selection Screen
 */

class SeatSelectionScreenActivity : AppCompatActivity() {
    /**
     * local variable
     */

    private lateinit var context: Context

    private lateinit var seatSelectionViewModel: SeatSelectionViewModel

    private lateinit var seatInfo: SeatInfo

    private var seatReqAvailCount = 0;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        context = this@SeatSelectionScreenActivity

        /**
         * creating the viewModel instance
         */
        seatSelectionViewModel = ViewModelProvider(this).get(SeatSelectionViewModel::class.java)

        initializeAPIRequestData()

        updateActionBarData();

        refreshButtonVisibility()

        refreshSelectedSeatCount()

        fetchSeatInfoData()

        buttonClickListeners()

    }

    private fun refreshSelectedSeatCount() {
        message.text ="Selected Seat Count  : ${seatSelectionViewModel.requestSeatTotalCount - seatReqAvailCount}"
    }

    /**
     * Screen Buttons Click Handling method
     */
    private fun buttonClickListeners() {

        next_button.setOnClickListener {

            if(seatReqAvailCount == 0){
                Messages.toast(context, getString(R.string.seat_booked_sucessful_msg))
            }

        }

        reset_button.setOnClickListener {
            refreshSeatData()
        }
    }

    /**
     * Reset function for seat selection
     * API call to fetch the latest seat availability
     */
    private fun refreshSeatData() {
        seatReqAvailCount = seatSelectionViewModel.requestSeatTotalCount;
        updateActionBarData()
        refreshButtonVisibility()
        refreshSelectedSeatCount()
        fetchSeatInfoData()
    }

    /**
     * Seat availability API call observer
     */
    private fun fetchSeatInfoData() {

        seatSelectionViewModel.getSeatInfoData()!!.observe(this, Observer { response ->

            /**
             *  showing the API error response in screen
             */
            if(response.actionableErrorSubLabel ==  R.string.error_msg_1003){
                message.text = getText(response.actionableErrorSubLabel)
                Messages.toast(context, getString(R.string.error_msg_1003))
            }

            /**
             * showing the API success response in screen
             */
            if(response.seatInfo != null){
                refreshSelectedSeatCount()
                seatInfo = response.seatInfo
                createSeatView()
            }
        })

    }

    /**
     * method to refresh the next button visibility after seat selection
     */
    private fun refreshButtonVisibility() {
       if(seatReqAvailCount > 0){
               next_button.isVisible = false
       } else if( seatReqAvailCount == 0) {
           next_button.isVisible = true
       }
    }

    /**
     * method to create the seat availability UI view
     */
    private fun createSeatView() {
        ll_seat_view_holder.removeAllViews()
        var root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.layoutParams = getLayoutParamBasic()
        root.gravity = Gravity.CENTER

        var rowsList = seatInfo.seatRows

        if (rowsList != null) {

            for (row in rowsList) {

                /**
                 * setting the row label View Eg- A, B, C, D
                 */
                var title = TextView(this)
                title.text = row?.rowLabel
                var textParam = getLayoutParamBasic();
                textParam.setMargins(10, 10, 10, 10)
                title.setPadding(10,10,10,10)
                title.layoutParams = textParam
                title.gravity = Gravity.CENTER
                title.setTextColor(Color.parseColor("#000000"));
                root.addView(title)

                root.addView(getRowViewTemp(row))
            }
        }

        ll_seat_view_holder.addView(root)
    }

    /**
     * method to adding the seats based on response
     */
    private fun getRowViewTemp(seatRow: SeatRow?): View? {

            var row = LinearLayout(this)
            row.orientation = LinearLayout.VERTICAL
            row.gravity = Gravity.CENTER
            row.layoutParams = getLayoutParamBasic()
                var column = LinearLayout(this)
                column.orientation = LinearLayout.HORIZONTAL
                column.gravity = Gravity.CENTER
                column.layoutParams = getLayoutParamBasic()
                val totalColumns = seatRow?.seats?.size

                for (j in 0 until totalColumns!!) {
                    val seat = seatRow?.seats?.get(j)

                    if (seat != null) {
                            var seatTextView = TextView(this)

                            seatTextView.text = seat.seatNo

                            /**
                             * setting the SeatData object as tag
                             */
                            seatTextView.tag = seat

                            /**
                             * setting the seat background based on status
                             */
                            if (seat.status == SOLD) {
                                seatTextView.setBackground(ContextCompat.getDrawable(context, R.drawable.sold_seat));
                            } else {
                                seatTextView.setBackground(ContextCompat.getDrawable(context,R.drawable.available_seat))
                            }

                            var imageParam = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )

                            imageParam.setMargins(5, 5, 5, 5)
                            seatTextView.setPadding(10,10,10,10)

                            seatTextView.layoutParams = imageParam
                            seatTextView.isClickable = false
                            seatTextView.tag = Gson().toJson(seat)

                            /**
                             * setting the seat click listener
                             */
                            seatTextView.setOnClickListener(object : View.OnClickListener {
                                    override fun onClick(p0: View?) {

                                        var selectedImage: TextView= p0 as TextView

                                        /**
                                         * converting the tag to SeatData object to check the status
                                         */
                                        var selectedSeat: SeatData = Gson().fromJson(selectedImage?.tag.toString(), SeatData::class.java)

                                        /**
                                         * if seat status is sold out . show error msg
                                         */
                                        if (selectedSeat != null && selectedSeat.status == SOLD) {
                                            Messages.toast(context, getString(R.string.sold_out_seat_error))

                                        }
                                        /**
                                         * if seat status is available and request seat limit is not reached the allow the selection
                                         */
                                        else  if(selectedSeat != null && selectedSeat.status == AVAILABLE && seatReqAvailCount>0){
                                            /**
                                             * setting the selected seat background
                                             */
                                            selectedImage.setBackgroundResource(R.drawable.selcted_seat)
                                            selectedSeat.status = SELECTED

                                            /**
                                             * updating in the object for future use, sending in API
                                             */
                                            selectedImage.tag = Gson().toJson(selectedSeat)

                                            /**
                                             * reducing the count after selection
                                             * refresh the UI with updated count
                                             */
                                            seatReqAvailCount--
                                            refreshSelectedSeatCount()
                                            refreshButtonVisibility()

                                        } else {
                                            Messages.toast(context, getString(R.string.modify_seleced_seat_error))
                                        }
                                    }
                                })
                                column.addView(seatTextView)

                        }
                }
                row.addView(column)

            return row

    }

    /**
     * method to updating the toolbar data
     */
    private fun updateActionBarData() {

        toolbar.txt_header.text = "Movie : ${seatSelectionViewModel.requestModel.movieName}"
        toolbar.txt_sub_header.text = "${seatSelectionViewModel.requestModel.cinemaName} : ${ seatSelectionViewModel.requestModel.city } | ${seatSelectionViewModel.requestModel.getDisplayShowTime()}"

        /**
         * top toolbar close button click handling
         */
        toolbar.img_option.setOnClickListener {
            finish()
        }


        /**
         * top toolbar seat request count click handling
         */
        toolbar.req_ticket_count.setOnClickListener{
           Messages.toast(context, getString(R.string.currently_allowed_seat_msg))
        }
    }

    private fun initializeAPIRequestData() {

        /**
            * for assignment hard coding the display data
            * we can use from previous selected data for real application (using bundle intent or persistent data)
         */

        seatSelectionViewModel.setSeatInfoRequestData(
            "ABCD", "New Cinema",
            "BLR", "2023-11-30T13:22:36Z"
        )


        /**
         * Setting the default allowed seat avail count
         */
        seatReqAvailCount = seatSelectionViewModel.requestSeatTotalCount

    }

    /**
     * Method to crate Basic LinearLayout with Center Gravitiy
     */
    private fun getLayoutParamBasic(): LinearLayout.LayoutParams {
        var param = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        param.gravity = Gravity.CENTER
        return param
    }
}
