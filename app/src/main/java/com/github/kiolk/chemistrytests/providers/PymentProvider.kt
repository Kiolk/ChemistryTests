package com.github.kiolk.chemistrytests.providers

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.widget.Toast
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.listeners.SimpleResultCallback
import com.github.kiolk.chemistrytests.data.models.AcountModel
import com.github.kiolk.chemistrytests.ui.activities.MainActivity

class PaymentProvider {
    fun prepareDialog(context: Context, account: AcountModel) {
        val alertBuilder = AlertDialog.Builder(context)
        alertBuilder.setTitle("You select ${account.accountTitle} account")
        alertBuilder.setPositiveButton(context.resources?.getString(R.string.PAY), object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                UserAccountProvider.changeTypeUserAccount(context, account.accountId, object : SimpleResultCallback{
                    override fun successExecute() {
                        Toast.makeText(context, "Your chose account by cost ${account.accountCost.toString()}", Toast.LENGTH_SHORT).show()
                        Toast.makeText(context, "Your success change account", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        })
        val dialog = alertBuilder.create()
        dialog.show()
    }
}