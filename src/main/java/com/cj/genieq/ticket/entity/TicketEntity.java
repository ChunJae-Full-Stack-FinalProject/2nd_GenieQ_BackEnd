package com.cj.genieq.ticket.entity;

import com.cj.genieq.payment.entity.PaymentEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder

@Entity
@DynamicInsert
@Table(name = "TICKET")
@SequenceGenerator(name = "seqTicNo", sequenceName = "SEQ_TIC_NO", allocationSize = 1)
public class TicketEntity {
    @Id
    @GeneratedValue(generator = "seqTicNo", strategy = GenerationType.SEQUENCE)
    @Column(name = "TIC_CODE")
    private Long ticCode;

    @Column(name = "TIC_NUMBER")
    private Integer ticNumber;

    @Column(name = "TIC_PRICE")
    private Integer price;

    @OneToMany(mappedBy = "ticket")
    private List<PaymentEntity> payments = new ArrayList<>();
}
