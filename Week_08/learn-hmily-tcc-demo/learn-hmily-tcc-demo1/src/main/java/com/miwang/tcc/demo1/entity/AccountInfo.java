package com.miwang.tcc.demo1.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author guozq
 * @date 2020-12-09-11:04 上午
 */
@Data
public class AccountInfo implements Serializable {
	
	private Long id;
	
	private String accountName;
	
	private String accountNo;
	
	private String accountPassword;
	
	private Double accountBalance;

}
