package com.company.bidhander.impl;

import com.company.bidhander.BidExecutor;
import com.company.dto.BidDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class AbstractBidExecutor implements BidExecutor {
    private static final Logger logger = LogManager.getRootLogger();
    protected final Map<String, Queue<BidDto>> queryMap = new HashMap<>();

    protected void shutdownExecutorService(ExecutorService service, Integer waitInSeconds) {
        service.shutdown();
        try {
            service.awaitTermination(waitInSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
    }

    protected void logQueue(Queue<BidDto> queue) {
        BidDto value;
        while ((value = queue.poll()) != null) {
            String payload = new String(Base64.getDecoder().decode(value.getPayload()));
            logger.info("Bid id: " + value.getId()
                    + ", TimeStamp: " + value.getTimestamp()
                    + ", Type: " + value.getType()
                    + ", Payload: " + payload);
        }
    }

}
