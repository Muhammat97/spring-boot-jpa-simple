package com.m97.cooperative.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.m97.cooperative.model.CustomerAccountModel;

@Repository
public interface CustomerAccountRepository extends JpaRepository<CustomerAccountModel, Integer> {

}
