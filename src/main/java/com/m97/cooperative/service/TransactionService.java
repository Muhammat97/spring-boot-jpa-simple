package com.m97.cooperative.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.m97.cooperative.model.CustomerAccountModel;
import com.m97.cooperative.model.CustomerModel;
import com.m97.cooperative.model.GenericModel;
import com.m97.cooperative.model.TransactionModel;
import com.m97.cooperative.model.props.MapCollectionProps;
import com.m97.cooperative.repository.CustomerAccountRepository;
import com.m97.cooperative.repository.CustomerRepository;
import com.m97.cooperative.repository.TransactionRepository;
import com.m97.cooperative.util.CommonUtil;
import com.m97.cooperative.util.ResponseUtil;

@Service
public class TransactionService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CustomerAccountRepository customerAccountRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private MapCollectionProps mapCollectionProps;

	private static final Logger LOGGER = LogManager.getLogger();

	public ResponseEntity<Object> getAllData(String startDate, String endDate) {
		GenericModel genericModel = new GenericModel();
		Date sDate = null;
		Date eDate = null;

		if (StringUtils.hasLength(startDate) || StringUtils.hasLength(endDate)) {
			if (!StringUtils.hasLength(startDate)) {
				genericModel.setCode("E006");
				genericModel.setArgs1("startDate");
				return ResponseUtil.setResponse(genericModel);
			}
			if (!StringUtils.hasLength(endDate)) {
				genericModel.setCode("E006");
				genericModel.setArgs1("endDate");
				return ResponseUtil.setResponse(genericModel);
			}

			try {
				SimpleDateFormat sdf = new SimpleDateFormat(CommonUtil.PATTERN_DATE);
				sdf.setTimeZone(CommonUtil.TIME_ZONE);
				sDate = sdf.parse(startDate);
				eDate = sdf.parse(endDate);

				if (sDate.after(eDate)) {
					genericModel.setCode("E014");
					genericModel.setArgs1("startDate");
					genericModel.setArgs2("endDate");
					return ResponseUtil.setResponse(genericModel);
				}

				Calendar calEndDate = Calendar.getInstance();
				calEndDate.setTime(eDate);
				calEndDate.add(Calendar.DATE, 1);

				eDate = calEndDate.getTime();
			} catch (Exception e) {
				genericModel.setCode("E012");
				genericModel.setArgs1("startDate and endDate");
				genericModel.setArgs2(CommonUtil.PATTERN_DATE);
				return ResponseUtil.setResponse(genericModel);
			}
		}

		try {
			List<TransactionModel> transactionModels = new ArrayList<>();
			if (sDate == null || eDate == null)
				transactionModels = transactionRepository.findAll();
			else
				transactionModels = transactionRepository.findAllByTranDateBetween(sDate, eDate);

			if (transactionModels.isEmpty())
				genericModel.setCode("S002");
			else
				genericModel.setCode("S001");
			genericModel.setData(transactionModels);
		} catch (Exception e) {
			LOGGER.error("REPOSITORY", e);
			genericModel.setCode("E002");
		}

		return ResponseUtil.setResponse(genericModel);
	}

	public ResponseEntity<Object> getDataByCustUuid(String custUuid) {
		GenericModel genericModel = new GenericModel();

		try {
			Optional<CustomerModel> customerModel = customerRepository.findByCustUuid(custUuid);
			if (!customerModel.isPresent()) {
				genericModel.setCode("E003");
				return ResponseUtil.setResponse(genericModel);
			}

			List<TransactionModel> transactionModels = new ArrayList<>();
			for (CustomerAccountModel customerAccountModel : customerModel.get().getCustomerAccountModels()) {
				transactionModels.addAll(customerAccountModel.getTransactionModels());
			}

			if (transactionModels.isEmpty())
				genericModel.setCode("S002");
			else
				genericModel.setCode("S001");
			genericModel.setData(transactionModels);
		} catch (Exception e) {
			LOGGER.error("REPOSITORY", e);
			genericModel.setCode("E002");
		}

		return ResponseUtil.setResponse(genericModel);
	}

	public ResponseEntity<Object> entryData(String custUuid, TransactionModel model, String username) {
		GenericModel genericModel = new GenericModel();

		LOGGER.info(model);

		try {
			Optional<CustomerModel> customerModel = customerRepository.findByCustUuid(custUuid);
			if (!customerModel.isPresent()) {
				genericModel.setCode("E003");
				return ResponseUtil.setResponse(genericModel);
			}

			CustomerAccountModel customerAccountModel = customerModel
					.get().getCustomerAccountModels().stream().filter(v -> mapCollectionProps.getTransactionType()
							.get(model.getTranType()).equals(v.getAccountModel().getAcctName()))
					.findFirst().orElse(null);

			if (customerAccountModel == null) {
				genericModel.setCode("E003");
				return ResponseUtil.setResponse(genericModel);
			}

			model.setTranUuid(UUID.randomUUID().toString());
			model.setCreatedBy(username);
			model.setCustomerAccountModel(customerAccountModel);

			customerAccountModel.getTransactionModels().add(model);

			BigDecimal amount = model.getTranAmount();
			if ("WITHDRAWAL".equals(model.getTranType()) || "REFUND".equals(model.getTranType()))
				amount = amount.negate();

			BigDecimal balance = customerAccountModel.getBalance().add(amount);
			if (balance.compareTo(new BigDecimal(0)) == -1) {
				genericModel.setCode("E015");
				return ResponseUtil.setResponse(genericModel);
			}

			customerAccountModel.setBalance(balance);
			customerAccountModel.setUpdatedBy(model.getCreatedBy());

			customerAccountRepository.save(customerAccountModel);

			genericModel.setCode("S003");
			genericModel.setData(model.getTranUuid());
		} catch (Exception e) {
			LOGGER.error("REPOSITORY", e);
			genericModel.setCode("E002");
		}

		return ResponseUtil.setResponse(genericModel);
	}

}
