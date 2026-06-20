package com.satyajeet.hospital.dto;
import com.satyajeet.hospital.entity.Bill;
import lombok.Data;
import java.math.BigDecimal;
@Data
public class BillRequest {
    private Long patientId;
    private Long appointmentId;
    private BigDecimal consultationFee;
    private BigDecimal medicineCharges;
    private BigDecimal labCharges;
    private BigDecimal roomCharges;
    private Bill.PaymentMethod paymentMethod;
}
