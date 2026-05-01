package io.game.accounts.controller;

import io.game.accounts.common.constant.AccountsConstants;
import io.game.accounts.common.dto.CustomerDto;
import io.game.accounts.common.dto.ResponseData;
import io.game.accounts.service.IAccountsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountsController {

    private final IAccountsService accountsService;

    @PostMapping("/create")
    ResponseEntity<ResponseData<Void>> createAccount(@RequestBody CustomerDto request) {
        accountsService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseData.<Void>builder()
                        .statusCode(AccountsConstants.STATUS_201)
                        .message(AccountsConstants.MESSAGE_201)
                        .object(null)
                        .build());
    }

    @GetMapping
    ResponseEntity<ResponseData<CustomerDto>> fetchAccountDetails(@RequestParam String mobileNumber) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseData.<CustomerDto>builder()
                        .statusCode(AccountsConstants.STATUS_200)
                        .message(AccountsConstants.MESSAGE_200)
                        .object(accountsService.getCustomerWithMobilbeNumber(mobileNumber))
                        .build());
    }
}
