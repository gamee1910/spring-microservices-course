package io.game.accounts.service;

import io.game.accounts.common.dto.CustomerDto;

public interface IAccountsService {

    void createAccount(CustomerDto request);

    CustomerDto fetchAccount(String mobileNumber);

    boolean updateAccount(CustomerDto request);

    boolean deleteAccount(String mobileNumber);
}
