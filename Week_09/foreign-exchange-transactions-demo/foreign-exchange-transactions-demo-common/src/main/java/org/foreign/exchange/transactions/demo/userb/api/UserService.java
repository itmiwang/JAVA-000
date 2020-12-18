package org.foreign.exchange.transactions.demo.userb.api;

import org.dromara.hmily.annotation.Hmily;
import org.foreign.exchange.transactions.demo.userb.entity.Dollar;
import org.foreign.exchange.transactions.demo.userb.entity.Rmb;

/**
 * @author guozq
 * @date 2020-12-18-2:19 下午
 */
public interface UserService {
    
    @Hmily
    boolean insertAssest(Rmb rmb, Dollar dollar);
    
    @Hmily
    boolean decreaseAssest(Rmb rmb, Dollar dollar);
}
