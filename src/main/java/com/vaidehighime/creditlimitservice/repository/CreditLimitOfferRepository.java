package com.vaidehighime.creditlimitservice.repository;

import com.vaidehighime.creditlimitservice.models.CreditLimitOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditLimitOfferRepository extends JpaRepository<CreditLimitOffer, Long> {
}
