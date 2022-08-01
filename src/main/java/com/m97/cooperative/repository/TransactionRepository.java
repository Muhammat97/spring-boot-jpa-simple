package com.m97.cooperative.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.m97.cooperative.model.TransactionModel;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionModel, String> {

	List<TransactionModel> findAllByTranDateBetween(Date tranDateStart, Date tranDateEnd);

}
