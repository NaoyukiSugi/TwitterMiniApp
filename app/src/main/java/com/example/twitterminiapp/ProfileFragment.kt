package com.example.twitterminiapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.twitterminiapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var viewBinding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentProfileBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: ProfileFragmentArgs by navArgs()
        val user = args.selectedUserIcon
        viewBinding.apply {
            Glide.with(viewBinding.root).load(user.profileImageUrlHttps).into(userIcon)
            userName.text = user.name
            userScreenName.text = user.screenName
            userDescription.text = user.description
        }
    }
}
