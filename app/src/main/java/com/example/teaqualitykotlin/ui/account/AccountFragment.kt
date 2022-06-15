package com.example.teaqualitykotlin.ui.account

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.teaqualitykotlin.*
import com.example.teaqualitykotlin.databinding.FragmentAccountBinding
import com.example.teaqualitykotlin.ui.authorization.SignInActivity
import com.google.firebase.auth.FirebaseAuth

class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding

    private val viewModel: AccountViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var sale = 0
        binding = FragmentAccountBinding.inflate(inflater, container, false)

        initFirebase()
        binding.exitTextView.setOnClickListener {
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
        REF_DATABASE_ROOT.child(NODE_USERS).child(firebaseAuth.uid.toString()).child(NODE_SALE).child(
            CHILD_SUM).get().addOnSuccessListener {
                sale = it.value.toString().toInt()
        }
        binding.saleCardView.setOnClickListener {
            if (sale > 3000) {
                binding.saleTextView.text = "2"
            } else if (sale > 2000) {
                binding.saleTextView.text = "5"
                binding.saleCardView.setCardBackgroundColor(Color.YELLOW)
            } else if (sale > 1000) {
                binding.saleTextView.text = "10"
            }
            Toast.makeText(context, "$sale", Toast.LENGTH_SHORT).show()
        }
        if (sale > 3000) {
            binding.saleTextView.text = "2"
        } else if (sale > 2000) {
            binding.saleTextView.text = "5"
        } else if (sale > 1000) {
            binding.saleTextView.text = "10"
        }
        
        binding.emailTv.text = firebaseAuth.currentUser?.email
        binding.saleTextView.text =

        return binding.root
    }
}