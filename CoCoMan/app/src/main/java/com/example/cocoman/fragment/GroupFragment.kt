package com.example.cocoman.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cocoman.R

class GroupFragment : Fragment(){
    companion object {

        private const val USERID = "userId"

        fun newInstance(userId: Int): GroupFragment {
            var bundle = Bundle()
            bundle.putInt(USERID, userId)
            var fragment = GroupFragment()
            fragment.arguments = bundle

            return fragment
        }
    }
    var userId: Int? = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userId = arguments?.getInt(USERID)
        return inflater.inflate(R.layout.fragment_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}