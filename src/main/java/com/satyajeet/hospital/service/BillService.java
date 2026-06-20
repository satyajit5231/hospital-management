package com.satyajeet.hospital.service;
import com.satyajeet.hospital.dto.*;
import com.satyajeet.hospital.entity.Bill;
import com.satyajeet.hospital.exception.ResourceNotFoundException;
import com.satyajeet.hospital.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.*;

@Service @RequiredArgsConstructor
public class BillService {
    private final BillRepository billRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    @Transactional
    public Bill createBill(BillRequest req) {
        var patient = patientRepository.findById(req.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + req.getPatientId()));

        BigDecimal medicine = req.getMedicineCharges() != null ? req.getMedicineCharges() : BigDecimal.ZERO;
        BigDecimal lab = req.getLabCharges() != null ? req.getLabCharges() : BigDecimal.ZERO;
        BigDecimal room = req.getRoomCharges() != null ? req.getRoomCharges() : BigDecimal.ZERO;
        BigDecimal total = req.getConsultationFee().add(medicine).add(lab).add(room);

        Bill.BillBuilder builder = Bill.builder()
                .patient(patient)
                .consultationFee(req.getConsultationFee())
                .medicineCharges(medicine).labCharges(lab).roomCharges(room)
                .totalAmount(total).paidAmount(BigDecimal.ZERO)
                .paymentMethod(req.getPaymentMethod());

        if (req.getAppointmentId() != null) {
            var appt = appointmentRepository.findById(req.getAppointmentId()).orElse(null);
            builder.appointment(appt);
        }
        return billRepository.save(builder.build());
    }

    public Bill getBillById(Long id) {
        return billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found: " + id));
    }

    public Page<Bill> getBillsByPatient(Long patientId, int page, int size) {
        return billRepository.findByPatientId(patientId, PageRequest.of(page, size));
    }

    @Transactional
    public Bill processPayment(Long id, BigDecimal amount, Bill.PaymentMethod method) {
        Bill bill = getBillById(id);
        BigDecimal paid = bill.getPaidAmount() != null ? bill.getPaidAmount() : BigDecimal.ZERO;
        BigDecimal newPaid = paid.add(amount);
        bill.setPaidAmount(newPaid);
        bill.setPaymentMethod(method);
        if (newPaid.compareTo(bill.getTotalAmount()) >= 0) {
            bill.setPaymentStatus(Bill.PaymentStatus.PAID);
            bill.setPaidDate(LocalDateTime.now());
        } else {
            bill.setPaymentStatus(Bill.PaymentStatus.PARTIAL);
        }
        return billRepository.save(bill);
    }

    public BillingReportDto getMonthlyReport(int year, int month) {
        LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime end = start.plusMonths(1);
        BigDecimal totalBilled = billRepository.sumTotalByDateRange(start, end);
        BigDecimal totalCollected = billRepository.sumPaidByDateRange(start, end);
        if (totalBilled == null) totalBilled = BigDecimal.ZERO;
        if (totalCollected == null) totalCollected = BigDecimal.ZERO;
        long totalBills = billRepository.count();
        long paidBills = billRepository.findByPaymentStatus(Bill.PaymentStatus.PAID, Pageable.unpaged()).getTotalElements();
        long pendingBills = billRepository.findByPaymentStatus(Bill.PaymentStatus.PENDING, Pageable.unpaged()).getTotalElements();
        return BillingReportDto.builder()
                .totalBilled(totalBilled).totalCollected(totalCollected)
                .outstanding(totalBilled.subtract(totalCollected))
                .totalBills(totalBills).paidBills(paidBills).pendingBills(pendingBills)
                .build();
    }
}
