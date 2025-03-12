package com.cj.genieq.payment.service;

import com.cj.genieq.payment.dto.response.PaymentListResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface PaymentService {
    void insertPayment(Long memCode, Long ticCode);
    List<PaymentListResponseDto> getPaymentList(
            Long memCode, LocalDate startDate, LocalDate endDate);
}
