package com.miwang.shardingsphere;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.miwang.shardingsphere.entity.POrder;
import com.miwang.shardingsphere.mapper.POrderDao;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
class ApacheShardingsphereDemoApplicationTests {
	
	@Resource
	private POrderDao pOrderDao;

	@Test
	void contextLoads() {
	}

	@Test
	public void insertPOrder() {
		POrder pOrder = new POrder();
		pOrder.setSerialNo(0L); //serial_no 为偶数 进_0库，为奇数 进_1库
		pOrder.setPredictSum(new BigDecimal("0"));
		pOrder.setDiscountsSum(new BigDecimal("0"));
		pOrder.setLogisticsSum(new BigDecimal("0"));
		pOrder.setActualSum(new BigDecimal("0"));
		pOrder.setPayMethodId(1L); //pay_method_id %16 根据结果进对应的表
		pOrder.setLogisticsId(0L);
		pOrder.setOrderState(0);
		pOrder.setLogisticsState(0);
		pOrder.setOperatorId(0L);
		pOrder.setRemark("");
		pOrder.setOperationId(0L);
		pOrder.setCreateTime(new Date());
		pOrder.setChangeTime(new Date());
		pOrder.setLogisticsTime(new Date());
		pOrder.setSuccessTime(new Date());
		pOrder.setSign(0);
		
		pOrderDao.insert(pOrder);
	}
	
	@Test
	public void insertPOrder1() {
		POrder pOrder = new POrder();
		pOrder.setSerialNo(1L); //serial_no 为偶数 进_0库，为奇数 进_1库
		pOrder.setPredictSum(new BigDecimal("0"));
		pOrder.setDiscountsSum(new BigDecimal("0"));
		pOrder.setLogisticsSum(new BigDecimal("0"));
		pOrder.setActualSum(new BigDecimal("0"));
		pOrder.setPayMethodId(14L); //pay_method_id %16 根据结果进对应的表
		pOrder.setLogisticsId(0L);
		pOrder.setOrderState(0);
		pOrder.setLogisticsState(0);
		pOrder.setOperatorId(0L);
		pOrder.setRemark("");
		pOrder.setOperationId(0L);
		pOrder.setCreateTime(new Date());
		pOrder.setChangeTime(new Date());
		pOrder.setLogisticsTime(new Date());
		pOrder.setSuccessTime(new Date());
		pOrder.setSign(0);
		
		pOrderDao.insert(pOrder);
	}
	
	@Test
	public void selectPOrder() {
		List<POrder> pOrderList = pOrderDao.selectAll();
		pOrderList.forEach(pOrder -> {
			System.out.println(pOrder);
		});
	}
}
