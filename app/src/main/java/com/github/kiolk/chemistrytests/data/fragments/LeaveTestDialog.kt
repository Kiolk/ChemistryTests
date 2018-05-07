package com.github.kiolk.chemistrytests.data.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.ui.TestingActivity

class LeaveTestDialog : DialogFragment(){

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogLeave = AlertDialog.Builder(activity)
        dialogLeave.setMessage(R.string.LEAV_TEST)
        dialogLeave.setPositiveButton(R.string.SHOW_RESULT){ dialog, which ->
            val act = activity as TestingActivity
            act.showResult()
        }
        dialogLeave.setNegativeButton(R.string.NO) { dialog, which -> fragmentManager?.fragments?.clear() }
        dialogLeave.setNeutralButton(R.string.YES) {dialog, which -> activity?.finish()}

        return dialogLeave.create()
    }
}
