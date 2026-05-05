package io.game.accounts.service.impl;

import io.game.accounts.common.constant.AccountsConstants;
import io.game.accounts.common.dto.AccountsDto;
import io.game.accounts.common.dto.CustomerDto;
import io.game.accounts.entity.Accounts;
import io.game.accounts.entity.Customer;
import io.game.accounts.exception.CustomerAlreadyExistsException;
import io.game.accounts.exception.ResourceNotFoundException;
import io.game.accounts.mapper.AccountsMapper;
import io.game.accounts.mapper.CustomerMapper;
import io.game.accounts.repository.AccountsRepository;
import io.game.accounts.repository.CustomerRepository;
import io.game.accounts.service.IAccountsService;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class AccountsServiceImpl implements IAccountsService {

    private final AccountsRepository accountsRepository;
    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public void createAccount(CustomerDto request) {
        customerRepository.findByMobileNumber(request.getMobileNumber()).ifPresent(c -> {
            throw new CustomerAlreadyExistsException(
                    "Customer already registered with mobile number: " + request.getMobileNumber());
        });

        Customer entity = CustomerMapper.mapToCustomer(request, new Customer());
        accountsRepository.save(createNewAccount(customerRepository.save(entity)));
    }

    @Override
    public CustomerDto getCustomerWithMobilbeNumber(String mobileNumber) {
        Customer customer = customerRepository
                .findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));

        Accounts accounts = accountsRepository
                .findByCustomerId(customer.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Accounts", "customerId", customer.getCustomerId().toString()));

        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));
        return customerDto;
    }

    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if (accountsDto != null) {
            Accounts accounts = accountsRepository
                    .findById(accountsDto.getAccountNumber())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Account",
                            "AccountNumber",
                            accountsDto.getAccountNumber().toString()));

            AccountsMapper.mapToAccounts(accountsDto, accounts);
            accounts = accountsRepository.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository
                    .findById(customerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString()));
            CustomerMapper.mapToCustomer(customerDto, customer);
            customerRepository.save(customer);
            isUpdated = true;
        }
        return isUpdated;
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository
                .findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "MobileNumber", mobileNumber));
        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        return true;
    }

    private Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        return newAccount;
    }
}
