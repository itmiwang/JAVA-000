package com.miwang.jmsactivemp;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.util.Map;

/**
 * @author guozq
 * @date 2021-01-14-4:22 下午
 */
@Component
public class Producer {
    
    @Autowired
    private JmsTemplate jmsTemplate;
    
    public void sendMessage(final String topic, final String message) throws JMSException {
        Map map = new Gson().fromJson(message, Map.class);
        jmsTemplate.convertAndSend(topic, map);
    }
}
