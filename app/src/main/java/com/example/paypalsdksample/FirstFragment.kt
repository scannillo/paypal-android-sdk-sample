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
import com.paypal.android.card.Card
import com.paypal.android.card.CardClient
import com.paypal.android.card.CardRequest
import com.paypal.android.card.model.CardResult
import com.paypal.android.core.Address
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

    var accessToken: String? = null
    var orderID: String? = null

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
        Networking.fetchAccessToken { accessToken ->
            binding.textviewFirst.text = "AccessToken: " + accessToken
            this.accessToken = accessToken

            binding.textviewFirst.text = "Fetching OrderID ..."
            Networking.fetchOrderID(createOrder()) { orderID ->
                binding.textviewFirst.text = "OrderID: " + orderID
                this.orderID = orderID
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            // Perform Card checkout
            val card = Card(
                number = "4005519200000004",
                expirationMonth = "01",
                expirationYear = "2025",
                securityCode = "123",
                billingAddress = Address(
                    streetAddress = "123 Main St.",
                    extendedAddress = "Apt. 1A",
                    locality = "city",
                    region = "IL",
                    postalCode = "12345",
                    countryCode = "US"
                )
            )

            val cardRequest = CardRequest(orderID!!, card)

            val config = CoreConfig(
                "AUiHPkr1LO7TzZH0Q5_aE8aGNmTiXZh6kKErYFrtXNYSDv13FrN2NElXabVV4fNrZol7LAaVb1gJj9lr",
                accessToken,
                environment = Environment.SANDBOX
            )
            val cardClient = CardClient(this.requireActivity(), config)
            cardClient.approveOrderListener = this

            binding.textviewFirst.text = "Approving order ..."
            cardClient.approveOrder(this.requireActivity(), cardRequest)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun createOrder(): OrderRequest {
        val amount = Amount("USD", "10.00")
        val applicationContext = ApplicationContext("https://example.com/returnUrl", "https://example.com/returnUrl")
        return OrderRequest(
            "AUTHORIZE",
            arrayOf(PurchaseUnit(amount)),
            applicationContext
        )
    }

    // IMPLEMENT - ApproveOrderListener Interface

    override fun onApproveOrderCanceled() {
        println("onApproveOrderCanceled")
    }

    override fun onApproveOrderFailure(error: PayPalSDKError) {
        println("onApproveOrderFailure" + error)
    }

    override fun onApproveOrderSuccess(result: CardResult) {
        binding.textviewFirst.text = result.orderID + ": " + result.status.toString()
        println("onApproveOrderSuccess")
    }

    override fun onApproveOrderThreeDSecureDidFinish() {
        println("onApproveOrderThreeDSecureDidFinish")
    }

    override fun onApproveOrderThreeDSecureWillLaunch() {
        println("onApproveOrderThreeDSecureWillLaunch")
    }
}