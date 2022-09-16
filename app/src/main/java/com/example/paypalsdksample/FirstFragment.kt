package com.example.paypalsdksample

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.paypalsdksample.databinding.FragmentFirstBinding
import com.github.kittinunf.fuel.Fuel
import com.paypal.android.card.ApproveOrderListener
import com.paypal.android.card.CardClient
import com.paypal.android.card.model.CardResult
import com.paypal.android.core.CoreConfig
import com.paypal.android.core.Environment
import com.paypal.android.core.PayPalSDKError

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment(), ApproveOrderListener {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val accessToken: String? = null

    // Perform non-UI setup
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    // Assign & declare view variables
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        // Get Access token
        binding.textviewFirst.text = "Fetching Access Token ..."
        Networking.fetchAccessToken { value ->
            binding.textviewFirst.text = value
            println(value)

            configurePayPalSDK()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun configurePayPalSDK() {
        val config = CoreConfig(accessToken, environment = Environment.SANDBOX)
        val cardClient = CardClient(this.requireActivity(), config)
        cardClient.approveOrderListener = this
    }

    // IMPLEMENT - ApproveOrderListener Interface

    override fun onApproveOrderCanceled() {
        TODO("Not yet implemented")
    }

    override fun onApproveOrderFailure(error: PayPalSDKError) {
        TODO("Not yet implemented")
    }

    override fun onApproveOrderSuccess(result: CardResult) {
        TODO("Not yet implemented")
    }

    override fun onApproveOrderThreeDSecureDidFinish() {
        TODO("Not yet implemented")
    }

    override fun onApproveOrderThreeDSecureWillLaunch() {
        TODO("Not yet implemented")
    }
}