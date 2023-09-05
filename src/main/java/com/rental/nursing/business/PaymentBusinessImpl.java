package com.rental.nursing.business;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rental.nursing.dao.JobDao;
import com.rental.nursing.dao.PaymentDao;
import com.rental.nursing.dto.PaymentDto;
import com.rental.nursing.entity.Payment;

@Service
public class PaymentBusinessImpl implements PaymentBusiness {
	@Autowired
	private PaymentDao paymentDao;

	@Autowired
	private JobDao jobDao;

	private static final Logger logger = LoggerFactory.getLogger(PaymentBusinessImpl.class);

	@Override
	public Optional<Payment> createPayment(PaymentDto dto) {
		try {
			Payment payment = new Payment();
			payment.setJob(jobDao.findById(dto.getJobId()).get());
			payment.setPaid(dto.isPaid());
			payment.setPaymentTime(dto.getPaymentTime());
			payment = paymentDao.save(payment);
			return Optional.of(payment);
		} catch (Exception e) {
			logger.error(ErrorMessages.PAYMENT_SAVE_ERROR + e.getMessage(), e);
			return Optional.empty();
		}
	}

	@Override
	public Optional<Payment> getPaymentById(Long id) {
		return paymentDao.findById(id);
	}

	@Override
	public void checkPaymentDone(Long id) {
		Payment payment = paymentDao.findById(id).get();
		payment.setPaid(true);
		paymentDao.save(payment);
	}

}
