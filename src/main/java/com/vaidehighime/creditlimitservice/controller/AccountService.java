package com.vaidehighime.creditlimitservice.controller;

import com.vaidehighime.creditlimitservice.exceptions.CreditLimitServiceResourceNotFoundException;
import com.vaidehighime.creditlimitservice.models.Account;
import com.vaidehighime.creditlimitservice.repository.AccountRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @PostMapping
    public Long createCustomerAccount(@RequestBody @NonNull final Account account) {
        return accountRepository.save(account).getAccountId();
    }

    @GetMapping("{accountId}")
    public ResponseEntity<Account> getCustomerAccountDetails(@PathVariable @NonNull final Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(
                () -> new CreditLimitServiceResourceNotFoundException("The account with id: " + accountId + "does not exists")
        );
        return ResponseEntity.ok(account);
    }
}
