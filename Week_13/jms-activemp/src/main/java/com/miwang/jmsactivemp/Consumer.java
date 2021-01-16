package com.miwang.jmsactivemp;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.util.Map;

/**
 * @author guozq
 * @date 2021-01-14-4:27 下午
 */
@Component
public class Consumer {
    
    @JmsListener(destination = "activeTest")
    public void receiveMessage(final Map message) throws JMSException {
        System.out.println(message.toString());
    }
}
