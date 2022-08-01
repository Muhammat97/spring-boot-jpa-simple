package com.m97.cooperative.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.m97.cooperative.util.CustomTimestampSerializer;

@JsonInclude(Include.NON_NULL)
@Entity
@Table(schema = "test", name = "cust_acct")
public class CustomerAccountModel {

	@JsonIgnore
	@Id
	@GeneratedValue(generator = "test.cust_acct_cust_acct_id_seq")
	@Column(name = "cust_acct_id")
	private Integer custAcctId;

	@Transient
	private String custUuid;

	@Transient
	private String acctName;

	private BigDecimal balance = new BigDecimal(0);

	@JsonSerialize(using = CustomTimestampSerializer.class)
	@CreationTimestamp
	@Column(name = "created_at")
	private Timestamp createdAt;

	@Column(name = "created_by")
	private String createdBy;

	@JsonSerialize(using = CustomTimestampSerializer.class)
	@UpdateTimestamp
	@Column(name = "updated_at")
	private Timestamp updatedAt;

	@Column(name = "updated_by")
	private String updatedBy;

	@JsonProperty("transaction")
	@OneToMany(mappedBy = "customerAccountModel", cascade = CascadeType.ALL)
	private List<TransactionModel> transactionModels;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "cust_id")
	private CustomerModel customerModel;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "acct_id")
	private AccountModel accountModel;

	public CustomerAccountModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getCustAcctId() {
		return custAcctId;
	}

	public void setCustAcctId(Integer custAcctId) {
		this.custAcctId = custAcctId;
	}

	public String getCustUuid() {
		return custUuid;
	}

	public void setCustUuid(String custUuid) {
		this.custUuid = custUuid;
	}

	public String getAcctName() {
		return acctName;
	}

	public void setAcctName(String acctName) {
		this.acctName = acctName;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public List<TransactionModel> getTransactionModels() {
		return transactionModels;
	}

	public void setTransactionModels(List<TransactionModel> transactionModels) {
		this.transactionModels = transactionModels;
	}

	public CustomerModel getCustomerModel() {
		return customerModel;
	}

	public void setCustomerModel(CustomerModel customerModel) {
		this.customerModel = customerModel;
	}

	public AccountModel getAccountModel() {
		return accountModel;
	}

	public void setAccountModel(AccountModel accountModel) {
		this.accountModel = accountModel;
	}

	@Override
	public String toString() {
		return "CustomerAccountModel [custAcctId=" + custAcctId + ", custUuid=" + custUuid + ", acctName=" + acctName
				+ ", balance=" + balance + ", createdBy=" + createdBy + ", updatedAt=" + updatedAt + ", updatedBy="
				+ updatedBy + ", transactionModels=" + transactionModels + "]";
	}

}
