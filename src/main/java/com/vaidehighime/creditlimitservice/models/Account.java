package com.vaidehighime.creditlimitservice.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;


@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accountId")
    private Long accountId;

    @OneToMany(targetEntity = CreditLimitOffer.class, cascade= CascadeType.ALL)
    @JoinColumn(name="account_credit_offers_fid")
    private List<CreditLimitOffer> associatedCreditLimitOffers;
    @Column(name = "customerId")
    private Long customerId;
    @Column(name = "accountLimit")
    private int accountLimit;
    @Column(name = "perTransactionLimit")
    private int perTransactionLimit;
    @Column(name = "lastAccountLimit")
    private int lastAccountLimit;
    @Column(name = "lastPerTransactionLimit")
    private int lastPerTransactionLimit;
    @Column(name = "accountLimitUpdateTime")
    private String accountLimitUpdateTime;
    @Column(name = "perTransactionLimitUpdateTime")
    private String perTransactionLimitUpdateTime;

    public Account() {
    }

    public Account(Long accountId, Long customerId, int accountLimit, int perTransactionLimit, int lastAccountLimit,
                   int lastPerTransactionLimit, String accountLimitUpdateTime, String perTransactionLimitUpdateTime) {
        super();
        this.customerId = customerId;
        this.accountLimit = accountLimit;
        this.perTransactionLimit = perTransactionLimit;
        this.lastAccountLimit = lastAccountLimit;
        this.lastPerTransactionLimit = lastPerTransactionLimit;
        this.accountLimitUpdateTime = accountLimitUpdateTime;
        this.perTransactionLimitUpdateTime = perTransactionLimitUpdateTime;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public int getAccountLimit() {
        return accountLimit;
    }

    public void setAccountLimit(int accountLimit) {
        this.accountLimit = accountLimit;
    }

    public int getPerTransactionLimit() {
        return perTransactionLimit;
    }

    public void setPerTransactionLimit(int perTransactionLimit) {
        this.perTransactionLimit = perTransactionLimit;
    }

    public int getLastAccountLimit() {
        return lastAccountLimit;
    }

    public void setLastAccountLimit(int lastAccountLimit) {
        this.lastAccountLimit = lastAccountLimit;
    }

    public int getLastPerTransactionLimit() {
        return lastPerTransactionLimit;
    }

    public void setLastPerTransactionLimit(int lastPerTransactionLimit) {
        this.lastPerTransactionLimit = lastPerTransactionLimit;
    }

    public String getAccountLimitUpdateTime() {
        return accountLimitUpdateTime;
    }

    public void setAccountLimitUpdateTime(String accountLimitUpdateTime) {
        this.accountLimitUpdateTime = accountLimitUpdateTime;
    }

    public String getPerTransactionLimitUpdateTime() {
        return perTransactionLimitUpdateTime;
    }

    public void setPerTransactionLimitUpdateTime(String perTransactionLimitUpdateTime) {
        this.perTransactionLimitUpdateTime = perTransactionLimitUpdateTime;
    }

    public List<CreditLimitOffer> getCreditLimitOffers() {
        return this.associatedCreditLimitOffers;
    }

    public void setAssociatedCreditLimitOffers(List<CreditLimitOffer> creditLimitOffers) {
        this.associatedCreditLimitOffers = creditLimitOffers;
    }
}
