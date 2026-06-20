package com.satyajeet.hospital.repository;
import com.satyajeet.hospital.entity.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
public interface BillRepository extends JpaRepository<Bill, Long> {
    Page<Bill> findByPatientId(Long patientId, Pageable pageable);
    Page<Bill> findByPaymentStatus(Bill.PaymentStatus status, Pageable pageable);
    @Query("SELECT SUM(b.totalAmount) FROM Bill b WHERE b.billDate BETWEEN :start AND :end")
    BigDecimal sumTotalByDateRange(LocalDateTime start, LocalDateTime end);
    @Query("SELECT SUM(b.paidAmount) FROM Bill b WHERE b.paidDate BETWEEN :start AND :end AND b.paymentStatus = 'PAID'")
    BigDecimal sumPaidByDateRange(LocalDateTime start, LocalDateTime end);
    List<Bill> findByPatientIdAndPaymentStatus(Long patientId, Bill.PaymentStatus status);
}
