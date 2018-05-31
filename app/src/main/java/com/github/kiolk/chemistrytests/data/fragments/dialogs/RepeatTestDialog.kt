package com.github.kiolk.chemistrytests.data.fragments.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.models.ResultInformation
import com.github.kiolk.chemistrytests.ui.activities.TEST_PARAM_INT
import com.github.kiolk.chemistrytests.ui.activities.TestingActivity

class RepeatTestDialog : DialogFragment(){

    companion object {
        val RESULT_BND : String = "result"
    }

    private var mResult : ResultInformation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mResult = arguments?.getSerializable(RESULT_BND) as? ResultInformation
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val repeatTest = AlertDialog.Builder(activity)
        val intent = Intent(context, TestingActivity::class.java)
        val params = mResult?.testParams
        repeatTest.setMessage(R.string.ARE_YOU_WANT)
        repeatTest.setPositiveButton(R.string.REPEAT, DialogInterface.OnClickListener{ dialog, which ->
            if(params != null) {
                params.questionList = null
                intent.putExtra(TEST_PARAM_INT, params)
                startActivity(intent)
            }
        })
        repeatTest.setNeutralButton(R.string.REPEAT_THIS_QUESTION, DialogInterface.OnClickListener{ dialog, which ->
            if(params != null) {
                params.questionList = mResult?.listAskedQuestionsId
                intent.putExtra(TEST_PARAM_INT, params)
                startActivity(intent)
            }
        })
        repeatTest.setNegativeButton(R.string.REMOVE, DialogInterface.OnClickListener{ dialog, which ->

        })
        return repeatTest.create()
    }

    fun fromInstance(result : ResultInformation) : RepeatTestDialog {
        val fragment = RepeatTestDialog()
        val bundle = Bundle()
        bundle.putSerializable(RESULT_BND, result)
        fragment.arguments = bundle
        return fragment
    }
}
