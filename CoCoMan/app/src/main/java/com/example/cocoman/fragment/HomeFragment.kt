package com.example.cocoman.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cocoman.R

class HomeFragment : Fragment(){
    var userId: Int? = 1
    companion object {

        private const val USERID = "userId"

        fun newInstance(userId: Int): HomeFragment {
            var bundle = Bundle()
            bundle.putInt(USERID, userId)
            var fragment = HomeFragment()
            fragment.arguments = bundle

            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = arguments?.getInt(USERID) ?: 0

    }

}