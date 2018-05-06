package com.github.kiolk.chemistrytests.data.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.ui.TestingActivity

class LeavTestDialog : DialogFragment(){

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogLeave = AlertDialog.Builder(activity)
        dialogLeave.setMessage(R.string.LEAV_TEST)
        dialogLeave.setPositiveButton(R.string.YES) { dialog, which ->
            val act = activity as TestingActivity
            act.showResult()
        }
        dialogLeave.setNegativeButton(R.string.NO) { dialog, which -> fragmentManager?.fragments?.clear() }

        return dialogLeave.create()
    }
}
