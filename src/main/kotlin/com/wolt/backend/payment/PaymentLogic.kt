package com.wolt.backend.payment

import org.springframework.stereotype.Service
import java.time.*
import java.time.format.DateTimeFormatter

@Service
class PaymentLogic {

    /**
     * Calculates the delivery fee based on the given cart value, delivery distance, number of items,
     * and delivery time.
     *
     * @param cartValue the value of the cart in cents
     * @param deliveryDistance the delivery distance in meters
     * @param numberOfItems the number of items in the cart
     * @param time the delivery time in ISO-8601 format
     * @return the delivery fee in cents
     */
    fun calculatePayment(cartValue: Int, deliveryDistance: Int, numberOfItems: Int, time: String): Int {
        return if (freeDelivery(cartValue)){
            return 0
        } else {
            var deliveryFee = smallOrderSurcharge(cartValue)
            deliveryFee += distanceFee(deliveryDistance)
            deliveryFee += bulkFee(numberOfItems)
            if (isRushHour(time)) deliveryFee=(deliveryFee*1.2).toInt()
            deliveryFee = maxFee(deliveryFee)
            return deliveryFee
        }
    }

    /**
     * Calculates the surcharge for small orders.
     *
     * @param cartValue the value of the cart in cents
     * @return the surcharge in cents
     */
    private fun smallOrderSurcharge(cartValue: Int): Int {
        return if (cartValue<1000) 1000-cartValue else 0
    }

    /**
     * Calculates the fee based on the delivery distance.
     *
     * @param deliveryDistance the delivery distance in meters
     * @return the fee in cents
     */
    private fun distanceFee(deliveryDistance: Int): Int {
        return if (deliveryDistance<1000) {
            200
        } else {
            ((deliveryDistance+500-1)/500)*100
        }
    }

    /**
     * Calculates the fee based on the number of items in the cart.
     *
     * @param numberOfItems the number of items in the cart
     * @return the fee in cents
     */
    private fun bulkFee(numberOfItems: Int): Int {
        val firstLimit = 4
        val secondLimit = 12
        val firstSurcharge = 50
        val secondSurcharge = 120

        return if (numberOfItems<=firstLimit) {
            0
        } else if (numberOfItems<=secondLimit) {
            (numberOfItems-(firstLimit))*firstSurcharge
        } else {
            ((secondLimit-(firstLimit))*firstSurcharge)+(numberOfItems-(secondSurcharge))*secondSurcharge
        }
    }

    /**
     * Checks if the delivery time is during rush hour.
     *
     * @param time the delivery time in ISO-8601 format
     * @return true if the delivery time is during rush hour, false otherwise
     */
    private fun isRushHour(time: String): Boolean {
        val dateInstant = Instant.from(DateTimeFormatter.ISO_INSTANT.parse(time))
        val date = LocalDateTime.ofInstant(dateInstant, ZoneId.of(ZoneOffset.UTC.id))
        return date.dayOfWeek==DayOfWeek.FRIDAY && 19>=date.hour && 15<=date.hour
    }

    /**
     * Checks if the cart value qualifies for free delivery.
     *
     * @param cartValue the value of the cart in cents
     * @return true if the cart value qualifies for free delivery, false otherwise
     */
    private fun freeDelivery(cartValue: Int): Boolean {
        return cartValue>=10000
    }

    
    private fun maxFee(deliveryFee: Int): Int {
        return if (deliveryFee>1500) 1500 else deliveryFee
    }
}