package com.m97.cooperative.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.m97.cooperative.model.CustomerAccountModel;
import com.m97.cooperative.model.CustomerModel;
import com.m97.cooperative.model.GenericModel;
import com.m97.cooperative.repository.AccountRepository;
import com.m97.cooperative.repository.CustomerRepository;
import com.m97.cooperative.util.CommonUtil;
import com.m97.cooperative.util.ResponseUtil;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private AccountRepository accountRepository;

	private static final Logger LOGGER = LogManager.getLogger();

	public ResponseEntity<Object> getAllData() {
		GenericModel genericModel = new GenericModel();

		try {
			List<CustomerModel> customerModels = customerRepository.findAll();
			if (customerModels.isEmpty())
				genericModel.setCode("S002");
			else
				genericModel.setCode("S001");
			genericModel.setData(customerModels);
		} catch (Exception e) {
			LOGGER.error("REPOSITORY", e);
			genericModel.setCode("E002");
		}

		return ResponseUtil.setResponse(genericModel);
	}

	public ResponseEntity<Object> getDataById(String custUuid) {
		GenericModel genericModel = new GenericModel();

		try {
			Optional<CustomerModel> customerModel = customerRepository.findByCustUuid(custUuid);
			if (!customerModel.isPresent()) {
				genericModel.setCode("E003");
				return ResponseUtil.setResponse(genericModel);
			}

			CustomerModel customer = customerModel.get();
			for (CustomerAccountModel customerAccountModel : customer.getCustomerAccountModels()) {
				customerAccountModel.setAcctName(customerAccountModel.getAccountModel().getAcctName());
			}

			genericModel.setCode("S001");
			genericModel.setData(customerModel.get());
		} catch (Exception e) {
			LOGGER.error("REPOSITORY", e);
			genericModel.setCode("E002");
		}

		return ResponseUtil.setResponse(genericModel);
	}

	public ResponseEntity<Object> entryData(CustomerModel customerModel, String username) {
		GenericModel genericModel = new GenericModel();

		LOGGER.info(customerModel);

		try {
			customerModel.setCustUuid(UUID.randomUUID().toString());
			customerModel.setCreatedBy(username);

			List<CustomerAccountModel> customerAccountModels = new ArrayList<>();

			CustomerAccountModel customerAccountModel = new CustomerAccountModel();
			customerAccountModel.setCustUuid(customerModel.getCustUuid());
			customerAccountModel.setCreatedBy(customerModel.getCreatedBy());
			customerAccountModel.setAccountModel(accountRepository.findByAcctName("SAVINGS").get());
			customerAccountModel.setCustomerModel(customerModel);
			customerAccountModels.add(customerAccountModel);

			customerAccountModel = new CustomerAccountModel();
			customerAccountModel.setCustUuid(customerModel.getCustUuid());
			customerAccountModel.setCreatedBy(customerModel.getCreatedBy());
			customerAccountModel.setAccountModel(accountRepository.findByAcctName("LOAN").get());
			customerAccountModel.setCustomerModel(customerModel);
			customerAccountModels.add(customerAccountModel);

			customerModel.setCustomerAccountModels(customerAccountModels);

			customerRepository.save(customerModel);

			genericModel.setCode("S003");
			genericModel.setData(customerModel.getCustUuid());
		} catch (Exception e) {
			LOGGER.error("REPOSITORY", e);
			genericModel.setCode("E002");
		}

		return ResponseUtil.setResponse(genericModel);
	}

	public ResponseEntity<Object> updateDataById(String custUuid, CustomerModel customerModel, String username) {
		GenericModel genericModel = new GenericModel();

		try {
			customerModel.setUpdatedBy(username);

			Optional<CustomerModel> oldCustomer = customerRepository.findByCustUuid(custUuid);
			if (!oldCustomer.isPresent()) {
				genericModel.setCode("E003");
				return ResponseUtil.setResponse(genericModel);
			}

			CustomerModel customer = oldCustomer.get();
			CommonUtil.updateModel(customerModel, customer);
			customerRepository.save(customer);

			genericModel.setCode("S004");
			genericModel.setData(customerModel.getCustUuid());
		} catch (Exception e) {
			LOGGER.error("REPOSITORY", e);
			genericModel.setCode("E002");
		}

		return ResponseUtil.setResponse(genericModel);
	}

}
