package com.m97.cooperative.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.m97.cooperative.util.CustomTimestampSerializer;

@Entity
@Table(schema = "test", name = "tran")
public class TransactionModel {

	@Id
	@Column(name = "tran_uuid")
	private String tranUuid;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@Transient
	private String fullName;

	@NotBlank(message = "E006|tranType")
	@Pattern(regexp = "DEPOSIT|WITHDRAWAL|LOAN|REFUND", message = "E011|tranType|DEPOSIT or WITHDRAWAL or LOAN or REFUND")
	@Column(name = "tran_type")
	private String tranType;

	@NotNull(message = "E006|tranAmount")
	@Min(value = 1, message = "E013|tranAmount|{value}|90000000000000000")
	@Max(value = 90000000000000000L, message = "E013|tranAmount|1|{value}")
	@Column(name = "tran_amount")
	private BigDecimal tranAmount;

	@JsonSerialize(using = CustomTimestampSerializer.class)
	@CreationTimestamp
	@Column(name = "tran_date")
	private Timestamp tranDate;

	@Column(name = "created_by")
	private String createdBy;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "cust_acct_id")
	private CustomerAccountModel customerAccountModel;

	public TransactionModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getTranUuid() {
		return tranUuid;
	}

	public void setTranUuid(String tranUuid) {
		this.tranUuid = tranUuid;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getTranType() {
		return tranType;
	}

	public void setTranType(String tranType) {
		this.tranType = tranType;
	}

	public BigDecimal getTranAmount() {
		return tranAmount;
	}

	public void setTranAmount(BigDecimal tranAmount) {
		this.tranAmount = tranAmount;
	}

	public Timestamp getTranDate() {
		return tranDate;
	}

	public void setTranDate(Timestamp tranDate) {
		this.tranDate = tranDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public CustomerAccountModel getCustomerAccountModel() {
		return customerAccountModel;
	}

	public void setCustomerAccountModel(CustomerAccountModel customerAccountModel) {
		this.customerAccountModel = customerAccountModel;
	}

	@Override
	public String toString() {
		return "TransactionModel [tranUuid=" + tranUuid + ", fullName=" + fullName + ", tranType=" + tranType
				+ ", tranAmount=" + tranAmount + ", tranDate=" + tranDate + ", createdBy=" + createdBy + "]";
	}

}
