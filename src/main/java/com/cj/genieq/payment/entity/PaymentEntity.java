package com.cj.genieq.payment.entity;

import com.cj.genieq.member.entity.MemberEntity;
import com.cj.genieq.ticket.entity.TicketEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder

@Entity
@DynamicInsert
@Table(name = "PAYMENT")
@SequenceGenerator(name = "seqPayNo", sequenceName = "SEQ_PAY_NO", allocationSize = 1)
public class PaymentEntity {
    @Id
    @GeneratedValue(generator = "seqPayNo", strategy = GenerationType.SEQUENCE)
    @Column(name = "PAY_CODE")
    private Long payCode;

    @Column(name = "PAY_PRICE")
    private Integer price;

    @Column(name = "PAY_DATE")
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "MEM_CODE")
    private MemberEntity member;

    @ManyToOne
    @JoinColumn(name = "TIC_CODE")
    private TicketEntity ticket;
}
