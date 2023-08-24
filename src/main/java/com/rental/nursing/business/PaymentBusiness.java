package com.rental.nursing.business;

import java.util.Optional;

import com.rental.nursing.dto.PaymentDto;
import com.rental.nursing.entity.Payment;

public interface PaymentBusiness {
	Optional<Payment> createPayment(PaymentDto paymentDto);

	Optional<Payment> getPaymentById(Long id);

	void checkPaymentDone(Long id);

}
