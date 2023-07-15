package com.vaidehighime.creditlimitservice.repository;

import com.vaidehighime.creditlimitservice.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
}
