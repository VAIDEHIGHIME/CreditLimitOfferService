package com.vaidehighime.creditlimitservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "creditLimitOffers")
public class CreditLimitOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long offerId;
    @JsonIgnore
    @ManyToOne(targetEntity = Account.class, cascade= CascadeType.ALL)
    @JoinColumn(name="account_id", nullable = false)
    private Account account;
    @Enumerated(EnumType.STRING)
    private CreditLimitType creditLimitType;
    @Column(name = "newLimit")
    private int newLimit;
    @Column(name = "offerActivationTime")
    private String offerActivationTime;
    @Column(name = "offerExpiryTime")
    private String offerExpiryTime;
    @Enumerated(EnumType.STRING)
    private CreditLimitOfferStatus creditLimitOfferStatus;
    public CreditLimitOffer() {
    }

    public CreditLimitOffer(Account account, CreditLimitType creditLimitType, int newLimit, String offerActivationTime,
                            String offerExpiryTime, CreditLimitOfferStatus creditLimitOfferStatus) {
        super();
        this.account = account;
        this.creditLimitType = creditLimitType;
        this.newLimit = newLimit;
        this.offerActivationTime = offerActivationTime;
        this.offerExpiryTime = offerExpiryTime;
        this.creditLimitOfferStatus = creditLimitOfferStatus;
    }

    public Account getAccount() {
        return this.account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

    public CreditLimitType getLimitType() {
        return creditLimitType;
    }

    public void setLimitType(CreditLimitType creditLimitType) {
        this.creditLimitType = creditLimitType;
    }

    public int getNewLimit() {
        return newLimit;
    }

    public void setNewLimit(int newLimit) {
        this.newLimit = newLimit;
    }

    public String getOfferActivationTime() {
        return offerActivationTime;
    }

    public void setOfferActivationTime(String offerActivationTime) {
        this.offerActivationTime = offerActivationTime;
    }

    public String getOfferExpiryTime() {
        return offerExpiryTime;
    }

    public void setOfferExpiryTime(String offerExpiryTime) {
        this.offerExpiryTime = offerExpiryTime;
    }

    public CreditLimitOfferStatus getCreditLimitOfferStatus() {
        return creditLimitOfferStatus;
    }

    public void setCreditLimitOfferStatus(CreditLimitOfferStatus creditLimitOfferStatus) {
        this.creditLimitOfferStatus = creditLimitOfferStatus;
    }
}
