package com.wolt.backend.payment

import org.springframework.web.bind.annotation.*

// Define a POST endpoint for the root path ("/"), which takes in a request body of type "Message"
// and returns an object of type "DeliveryFee"
@CrossOrigin(origins = ["*"])
@RestController
class DeliveryController(private val paymentLogic: PaymentLogic) {

    // Define a POST endpoint for the root path ("/"), which takes in a request body of type "Message"
    // and returns an object of type "DeliveryFee"
    @PostMapping("/")
    fun index(@RequestBody message: Message): DeliveryFee {

        // Use the "PaymentLogic" class to calculate the delivery fee
        // based on the values passed in through the "Message" object
        return DeliveryFee(paymentLogic.calculatePayment(message.cart_value, message.delivery_distance, message.number_of_items, message.time))
    }
}

data class Message(val cart_value: Int, val delivery_distance: Int, val number_of_items: Int, val time: String)
data class DeliveryFee(val deliveryFee: Int)