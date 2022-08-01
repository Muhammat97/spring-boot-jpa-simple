package com.m97.cooperative.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(schema = "test", name = "acct")
public class AccountModel {

	@Id
	@GeneratedValue(generator = "test.acct_acct_id_seq")
	@Column(name = "acct_id")
	private Integer acctId;

	@Column(name = "acct_name", unique = true)
	private String acctName;

	@Column(name = "acct_type")
	private String acctType;

	private String currency;

	@OneToMany(mappedBy = "accountModel", cascade = CascadeType.ALL)
	private List<CustomerAccountModel> customerAccountModels;

	public AccountModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getAcctId() {
		return acctId;
	}

	public void setAcctId(Integer acctId) {
		this.acctId = acctId;
	}

	public String getAcctName() {
		return acctName;
	}

	public void setAcctName(String acctName) {
		this.acctName = acctName;
	}

	public String getAcctType() {
		return acctType;
	}

	public void setAcctType(String acctType) {
		this.acctType = acctType;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public List<CustomerAccountModel> getCustomerAccountModels() {
		return customerAccountModels;
	}

	public void setCustomerAccountModels(List<CustomerAccountModel> customerAccountModels) {
		this.customerAccountModels = customerAccountModels;
	}

	@Override
	public String toString() {
		return "AccountModel [acctId=" + acctId + ", acctName=" + acctName + ", acctType=" + acctType + ", currency="
				+ currency + ", customerAccountModels=" + customerAccountModels + "]";
	}

}
