package com.example.teaqualitykotlin.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.teaqualitykotlin.databinding.FragmentAccountBinding
import com.example.teaqualitykotlin.navigator
import com.example.teaqualitykotlin.ui.authorization.SignInActivity
import com.google.firebase.auth.FirebaseAuth

class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private val viewModel: AccountViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater, container, false)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.exitTextView.setOnClickListener {
//            viewModel.logout()
            firebaseAuth.signOut()
            val intent = Intent(requireContext(), SignInActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        binding.favoriteTV.setOnClickListener {
            navigator().showFavorite()
        }

        binding.deliveryAndPaymentTv.setOnClickListener {

        }

        val email = firebaseAuth.currentUser?.email
        binding.emailTv.text = email

        return binding.root
    }
}