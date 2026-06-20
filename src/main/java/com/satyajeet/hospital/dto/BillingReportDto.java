package com.satyajeet.hospital.dto;
import lombok.*;
import java.math.BigDecimal;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class BillingReportDto {
    private BigDecimal totalBilled;
    private BigDecimal totalCollected;
    private BigDecimal outstanding;
    private Long totalBills;
    private Long paidBills;
    private Long pendingBills;
}
