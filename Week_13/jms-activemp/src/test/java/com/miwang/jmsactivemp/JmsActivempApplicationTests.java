package com.miwang.jmsactivemp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jms.JMSException;

@SpringBootTest
class JmsActivempApplicationTests {
    
    @Autowired
    private Producer producer;
    
    @Test
    public void testProducer() throws JMSException {
        producer.sendMessage("activeTest", "{\"name\":\"Miwang\"} ");
    }
    
}
