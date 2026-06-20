package com.satyajeet.hospital.controller;
import com.satyajeet.hospital.dto.*;
import com.satyajeet.hospital.entity.Bill;
import com.satyajeet.hospital.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController @RequestMapping("/api/bills") @RequiredArgsConstructor
public class BillController {
    private final BillService billService;

    @PostMapping
    public ResponseEntity<Bill> createBill(@RequestBody BillRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(billService.createBill(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bill> getBill(@PathVariable Long id) {
        return ResponseEntity.ok(billService.getBillById(id));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<Page<Bill>> getBillsByPatient(
            @PathVariable Long patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(billService.getBillsByPatient(patientId, page, size));
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<Bill> processPayment(
            @PathVariable Long id,
            @RequestParam BigDecimal amount,
            @RequestParam Bill.PaymentMethod method) {
        return ResponseEntity.ok(billService.processPayment(id, amount, method));
    }

    @GetMapping("/report")
    public ResponseEntity<BillingReportDto> getMonthlyReport(
            @RequestParam int year, @RequestParam int month) {
        return ResponseEntity.ok(billService.getMonthlyReport(year, month));
    }
}
