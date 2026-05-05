package io.game.accounts.controller;

import io.game.accounts.common.dto.CustomerDto;
import io.game.accounts.common.dto.ResponseData;
import io.game.accounts.service.IAccountsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static io.game.accounts.common.constant.AccountsConstants.*;
import static org.springframework.http.HttpStatus.EXPECTATION_FAILED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
class AccountsController {

    private final IAccountsService accountsService;

    @PostMapping("/create")
    ResponseEntity<ResponseData<Void>> createAccount(@RequestBody CustomerDto request) {
        accountsService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseData.<Void>builder()
                        .statusCode(STATUS_201)
                        .message(MESSAGE_201)
                        .object(null)
                        .build());
    }

    @GetMapping
    ResponseEntity<ResponseData<CustomerDto>> fetchAccountDetails(@RequestParam String mobileNumber) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseData.<CustomerDto>builder()
                        .statusCode(STATUS_200)
                        .message(MESSAGE_200)
                        .object(accountsService.getCustomerWithMobilbeNumber(mobileNumber))
                        .build());
    }

    @PutMapping("/update")
    ResponseEntity<ResponseData<Object>> updateAccounts(@RequestBody CustomerDto dto) {
        boolean isUpdated = accountsService.updateAccount(dto);
        if (isUpdated) {
            return ResponseEntity.ok(ResponseData.builder()
                    .message(MESSAGE_200)
                    .statusCode(STATUS_200)
                    .build());
        } else {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(ResponseData.builder()
                            .message(STATUS_417)
                            .statusCode(STATUS_417)
                            .build());
        }
    }

    @DeleteMapping("/delete")
    ResponseEntity<ResponseData<Object>> deleteAccounts(@RequestParam String mobileNumber) {
        boolean isAccountDeleted = accountsService.deleteAccount(mobileNumber);
        if (isAccountDeleted) {
            return ResponseEntity.ok(ResponseData.builder()
                    .message(MESSAGE_200)
                    .statusCode(STATUS_200)
                    .build());
        } else {
            return ResponseEntity.status(EXPECTATION_FAILED)
                    .body(ResponseData.builder()
                            .message(STATUS_417)
                            .statusCode(STATUS_417)
                            .build());
        }
    }
}
