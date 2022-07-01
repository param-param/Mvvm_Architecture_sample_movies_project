package com.demo.moviesapp.commonUtils.alertDialogs

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.LayoutInflater
import android.view.Gravity
import com.demo.moviesapp.R


/*class ProgressBarDialog
    : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view: View = requireActivity()!!.layoutInflater.inflate(
            R.layout.alert_dialog_for_progress_bar, LinearLayout(
                activity
            ), false
        )

        // Build dialog
        val builder = Dialog(requireActivity()!!)
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE)
        builder.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        builder.setContentView(view)
        return builder
    }
}*/
class ProgressBarDialog(context: Context?) : Dialog(context!!) {
    init {
        val wlmp = window!!.attributes
        wlmp.gravity = Gravity.CENTER_HORIZONTAL
        window!!.attributes = wlmp
        setTitle(null)
        setCancelable(false)
        setOnCancelListener(null)
        val view: View = LayoutInflater.from(context).inflate(
            R.layout.alert_dialog_for_progress_bar, null
        )
        setContentView(view)
    }
}