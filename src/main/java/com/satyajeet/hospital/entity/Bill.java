package com.satyajeet.hospital.entity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name = "bills")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Bill {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "appointment_id")
    private Appointment appointment;
    @Column(name = "consultation_fee", nullable = false) private BigDecimal consultationFee;
    @Column(name = "medicine_charges") private BigDecimal medicineCharges;
    @Column(name = "lab_charges") private BigDecimal labCharges;
    @Column(name = "room_charges") private BigDecimal roomCharges;
    @Column(name = "total_amount", nullable = false) private BigDecimal totalAmount;
    @Column(name = "paid_amount") private BigDecimal paidAmount;
    @Enumerated(EnumType.STRING) private PaymentStatus paymentStatus;
    @Enumerated(EnumType.STRING) private PaymentMethod paymentMethod;
    @Column(name = "bill_date", nullable = false) private LocalDateTime billDate;
    @Column(name = "paid_date") private LocalDateTime paidDate;
    @Column(name = "created_at") private LocalDateTime createdAt;
    @PrePersist protected void onCreate() {
        createdAt = LocalDateTime.now(); billDate = LocalDateTime.now();
        if (paymentStatus == null) paymentStatus = PaymentStatus.PENDING;
    }
    public enum PaymentStatus { PENDING, PARTIAL, PAID, REFUNDED }
    public enum PaymentMethod { CASH, CARD, UPI, INSURANCE, ONLINE }
}
