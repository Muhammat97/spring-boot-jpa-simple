package com.m97.cooperative.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.m97.cooperative.model.AccountModel;

@Repository
public interface AccountRepository extends JpaRepository<AccountModel, Integer> {

	Optional<AccountModel> findByAcctName(String acctName);

}
