package com.example.cocoman.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cocoman.R

class MyProfileFragment : Fragment(){
    var userId: Int? = 1

    companion object {

        private const val USERID = "userId"

        fun newInstance(userId: Int): MyProfileFragment {
            var bundle = Bundle()
            bundle.putInt(USERID, userId)
            var fragment = MyProfileFragment()
            fragment.arguments = bundle

            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = arguments?.getInt(MyProfileFragment.USERID) ?: 0

    }
}