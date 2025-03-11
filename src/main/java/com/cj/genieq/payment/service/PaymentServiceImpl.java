package com.cj.genieq.payment.service;

import com.cj.genieq.member.entity.MemberEntity;
import com.cj.genieq.member.repository.MemberRepository;
import com.cj.genieq.payment.entity.PaymentEntity;
import com.cj.genieq.payment.repository.PaymentRepository;
import com.cj.genieq.ticket.entity.TicketEntity;
import com.cj.genieq.ticket.repository.TicketRepository;
import com.cj.genieq.usage.service.UsageService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final MemberRepository memberRepository;
    private final TicketRepository ticketRepository;
    private final PaymentRepository paymentRepository;
    private final UsageService usageService;

    // 결제 내역 추가
    @Override
    @Transactional
    public void insertPayment(Long memCode, Long ticCode) {
        MemberEntity member = memberRepository.findById(memCode)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));

        TicketEntity ticket = ticketRepository.findById(ticCode)
                .orElseThrow(() -> new EntityNotFoundException("티켓이 존재하지 않습니다."));

        PaymentEntity payment = PaymentEntity.builder()
                .price(ticket.getPrice())
                .date(LocalDateTime.now())
                .member(member)
                .ticket(ticket)
                .build();
        try {
            // 결제 저장
            paymentRepository.save(payment);

            // 이용권 추가
            usageService.updateUsage(memCode, ticket.getTicNumber(), "이용권 결제");
        } catch (Exception e) {
            throw new RuntimeException("결제 처리 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }
}
