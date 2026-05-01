package io.game.accounts.common.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AccountsDto {
    private Long accountNumber;
    private String accountType;
    private String branchAddress;
}
