package com.github.kiolk.chemistrytests.data.fragments.accounts

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.adapters.recyclers.FeaturesRecyclerAdapter
import com.github.kiolk.chemistrytests.data.fragments.BaseFragment
import com.github.kiolk.chemistrytests.data.fragments.FeatureFragment
import com.github.kiolk.chemistrytests.data.models.AcountModel
import com.github.kiolk.chemistrytests.providers.PaymentProvider

class AccountFeaturesFragment : Fragment(){

    companion object {
        val ACCOUNT_FEATURES_LAYOUT : Int = R.layout.fragment_account
        val ACCOUNT_OBJECT_KEY : String = "Account"
    }

    var mAccount : AcountModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAccount = arguments?.getSerializable(ACCOUNT_OBJECT_KEY) as AcountModel
    }

    fun getRecycler(view : View) : RecyclerView?{
        return view.findViewById(R.id.account_features_recycler_view)
    }

    fun getTitle(view : View) : TextView?{
        return view.findViewById(R.id.account_title_text_view)
    }

    fun getSubscriptionButton(view: View) : Button?{
        val button : Button=  view.findViewById(R.id.subscribe_account_button)
        button.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
               val payment = PaymentProvider()
                mAccount?.let { context?.let { it1 -> payment.prepareDialog(it1, it) } }
            }
        })
        return button
    }

    fun setupRecycler(view : View, featuresList : List<String>){
        val recycler = getRecycler(view)
        val adapter = context?.let { FeaturesRecyclerAdapter(it, featuresList) }
        recycler?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycler?.adapter = adapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(ACCOUNT_FEATURES_LAYOUT, null)
        mAccount?.accountFeatures?.let { setupRecycler(view, it) }
        getTitle(view)?.text = mAccount?.accountTitle
        getSubscriptionButton(view)?.text = mAccount?.accountCost.toString()
        return view
    }

    fun fromInstance(account : AcountModel) : AccountFeaturesFragment{
        val fragment = AccountFeaturesFragment()
        val bundle = Bundle()
        bundle.putSerializable(ACCOUNT_OBJECT_KEY, account)
        fragment.arguments = bundle
        return fragment
    }
}
