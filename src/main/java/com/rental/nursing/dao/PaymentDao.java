package com.rental.nursing.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rental.nursing.entity.Payment;

public interface PaymentDao extends JpaRepository<Payment, Long> {

}
