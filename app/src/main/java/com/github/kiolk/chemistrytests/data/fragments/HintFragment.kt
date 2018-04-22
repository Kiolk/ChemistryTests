package com.github.kiolk.chemistrytests.data.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.models.Hint
import com.github.kiolk.chemistrytests.data.models.setFormattedText

class HintFragment : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_hint, null)
        return view ?: super.onCreateView(inflater, container, savedInstanceState)
    }

    fun showHint(hint: List<Hint>?) {
        if (hint != null) {
            view?.findViewById<TextView>(R.id.hint_fragment_text_view)?.let { setFormattedText(it, hint[0].text, hint[0].images?.get(0)) }
        }
    }
}