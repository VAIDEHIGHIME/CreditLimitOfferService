package com.vaidehighime.creditlimitservice.controller;

import com.vaidehighime.creditlimitservice.exceptions.CreditLimitServiceResourceNotFoundException;
import com.vaidehighime.creditlimitservice.models.Account;
import com.vaidehighime.creditlimitservice.models.CreditLimitOffer;
import com.vaidehighime.creditlimitservice.models.CreditLimitOfferStatus;
import com.vaidehighime.creditlimitservice.models.CreditLimitType;
import com.vaidehighime.creditlimitservice.repository.AccountRepository;
import com.vaidehighime.creditlimitservice.repository.CreditLimitOfferRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.google.common.base.Preconditions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/creditlimitoffers")
public class CreditLimitOfferService {
    @Autowired
    private CreditLimitOfferRepository creditLimitOfferRepository;
    @Autowired
    private AccountRepository accountRepository;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd h:m");



    @PostMapping("/create")
    public Long createCreditLimitOfferForAccount(@RequestParam @NonNull final Long accountId,
                                                 @RequestParam @NonNull final CreditLimitType creditLimitType,
                                                 @RequestParam @NonNull final int newLimit,
                                                 @RequestParam @NonNull final String offerActivationTime,
                                                 @RequestParam @NonNull final String offerExpiryTime) {

        Account eligibleAccountForCreditLimitOffer =
                ValidateInputRequest(accountId, creditLimitType, newLimit, offerActivationTime, offerExpiryTime);

        CreditLimitOffer newCreditLimitOffer = new CreditLimitOffer(eligibleAccountForCreditLimitOffer, creditLimitType, newLimit,
                offerActivationTime, offerExpiryTime, CreditLimitOfferStatus.PENDING);
        eligibleAccountForCreditLimitOffer.getCreditLimitOffers().add(newCreditLimitOffer);
        accountRepository.save(eligibleAccountForCreditLimitOffer);
        return creditLimitOfferRepository.save(newCreditLimitOffer).getOfferId();
    }

    @PutMapping("{offerId}")
    public ResponseEntity<CreditLimitOfferStatus> updateCreditLimitOfferStatus(@PathVariable @NonNull final Long offerId,
                                                            @RequestParam @NonNull final String creditLimitOfferStatus) {
        CreditLimitOffer specifiedCreditLimitOffer = creditLimitOfferRepository.findById(offerId).orElseThrow(
                () -> new CreditLimitServiceResourceNotFoundException("Invalid Credit Offer id : " + offerId)
        );
        specifiedCreditLimitOffer.setCreditLimitOfferStatus(CreditLimitOfferStatus.valueOf(creditLimitOfferStatus));

        if (specifiedCreditLimitOffer.getCreditLimitOfferStatus() == CreditLimitOfferStatus.ACCEPTED) {
            updateAssociatedAccountWithAcceptedCreditLimitOffer(specifiedCreditLimitOffer);
        }

        creditLimitOfferRepository.save(specifiedCreditLimitOffer);

        return ResponseEntity.ok(specifiedCreditLimitOffer.getCreditLimitOfferStatus());
    }

    @GetMapping("{accountId}")
    public ResponseEntity<List<CreditLimitOffer>> getAccountCreditLimitOffers(@PathVariable @NonNull final Long accountId,
                                                                              @RequestParam(required = false) String activeDate) {
        Account accountDetails = accountRepository.findById(accountId).orElseThrow(
                () -> new CreditLimitServiceResourceNotFoundException("Account with id : " + accountId + "is not present")
        );
        List<CreditLimitOffer> activeCreditLimitOffers = filterCreditLimitOffers(accountDetails.getCreditLimitOffers(), activeDate);
        return ResponseEntity.ok(activeCreditLimitOffers);

    }

    private List<CreditLimitOffer> filterCreditLimitOffers(List<CreditLimitOffer> creditLimitOffers, String activeDate) {
        List<CreditLimitOffer> filteredList = new LinkedList<>() {
        };
        for (CreditLimitOffer creditLimitOffer : creditLimitOffers) {
            if (isActiveDateInRange(creditLimitOffer, activeDate)
                    && creditLimitOffer.getCreditLimitOfferStatus() == CreditLimitOfferStatus.PENDING) {
                filteredList.add(creditLimitOffer);
            }
        }
        return filteredList;
    }

    private boolean isActiveDateInRange(CreditLimitOffer creditLimitOffer, String activeDate) {
        //bypassing the activeDate filter in case the active date is not provided
        if (Objects.isNull(activeDate)) return true;

        String creditLimitOfferActivationDate = creditLimitOffer.getOfferActivationTime();
        String creditLimitOfferExpiryDate = creditLimitOffer.getOfferExpiryTime();

        try {
            if (this.sdf.parse(activeDate).before(sdf.parse(creditLimitOfferActivationDate))
            && this.sdf.parse(activeDate).before(sdf.parse(creditLimitOfferExpiryDate))) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            throw new RuntimeException(e + "Exception While Parsing the Offer Time Range");
        }


    }


    private Account ValidateInputRequest(Long accountId, CreditLimitType creditLimitType, int newLimit, String offerActivationTime, String offerExpiryTime) {
        ValidateActiveExpireTimeRange(offerActivationTime, offerExpiryTime);

        Account associatedAccountForOfferCreation = accountRepository.findById(accountId).orElseThrow(
                () -> new CreditLimitServiceResourceNotFoundException("Created Limit Offer cannot be created as the account " +
                        "with account Id: " + accountId + "does not exists")
        );

        ValidateHigherCreditLimit(creditLimitType, newLimit, associatedAccountForOfferCreation);
        return associatedAccountForOfferCreation;
    }

    private void ValidateActiveExpireTimeRange(String offerActivationTime, String offerExpiryTime) {
        try {
            Preconditions.checkArgument(this.sdf.parse(offerActivationTime).before(sdf.parse(offerExpiryTime)),
                    "Invalid Time Range of Offer");
        } catch (ParseException e) {
            throw new RuntimeException(e + "Exception While Parsing the Offer Time Range");
        }
    }

    private static void ValidateHigherCreditLimit(CreditLimitType creditLimitType, int newLimit, Account associatedAccountForOfferCreation) {
        switch (creditLimitType) {
            case ACCOUNT_LIMIT -> {
                Preconditions.checkArgument(associatedAccountForOfferCreation.getAccountLimit() < newLimit);
            }
            case PER_TRANSACTION_LIMIT -> {
                Preconditions.checkArgument(associatedAccountForOfferCreation.getPerTransactionLimit() < newLimit);
            }
        }
    }

    private void updateAssociatedAccountWithAcceptedCreditLimitOffer(CreditLimitOffer specifiedCreditLimitOffer) {
        Account associatedAccount = specifiedCreditLimitOffer.getAccount();
        CreditLimitType creditLimitType = specifiedCreditLimitOffer.getLimitType();
        switch (creditLimitType) {
            case ACCOUNT_LIMIT -> {
                associatedAccount.setLastAccountLimit(associatedAccount.getAccountLimit());
                associatedAccount.setAccountLimit(specifiedCreditLimitOffer.getNewLimit());
            }
            case PER_TRANSACTION_LIMIT -> {
                associatedAccount.setLastAccountLimit(associatedAccount.getPerTransactionLimit());
                associatedAccount.setPerTransactionLimit(specifiedCreditLimitOffer.getNewLimit());
            }
        }
        associatedAccount.setAccountLimitUpdateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")));
        accountRepository.save(associatedAccount);
    }

}
