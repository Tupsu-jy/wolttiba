package com.wolt.backend.payment

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class PaymentLogicTest {

    private val testPaymentLogic: PaymentLogic = PaymentLogic()

    @ParameterizedTest(name = "given the given parameters, the delivery fee should be {4} eurocents")
    @MethodSource("distanceArguments")
    fun deliveryFeeTest(
        cartValue: Int,
        deliveryDistance: Int,
        numberOfItems: Int,
        time: String,
        expectedPayment: Int
    ) {
        assertEquals(
            expectedPayment,
            testPaymentLogic.calculatePayment(cartValue, deliveryDistance, numberOfItems, time)
        )
    }

    private companion object {
        @JvmStatic
        fun distanceArguments(): Stream<Arguments> = Stream.of(
            Arguments.of(790, 2235, 4, "2021-10-12T13:00:00Z", 710),
            Arguments.of(2000, 600, 5, "2021-10-12T13:00:00Z", 250),
            Arguments.of(2000, 600, 5, "2021-10-15T16:00:00Z", 300),
            Arguments.of(1000, 500, 4, "2021-10-15T16:00:00Z", 240)
            //etc
        )
    }
}