package com.elotech.biblioteca_arom.dtos;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class LoanDTO {
    private Long loanId;
    private String loanDate;
    private String returnDate;
    private String status;
    private String userName;
    private String bookTitle;
 }
