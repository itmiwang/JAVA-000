package com.miwang.tcc.demo2.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author guozq
 * @date 2020-12-09-4:54 下午
 */
@Data
public class AccountInfo implements Serializable { 
	
	private Long id;
	
	private String accountName;
	
	private String accountNo;
	
	private String accountPassword;
	
	private Double accountBalance;

}
