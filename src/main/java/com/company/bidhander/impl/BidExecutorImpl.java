package com.company.bidhander.impl;

import com.company.bidhander.BidExecutor;
import com.company.dto.BidDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BidExecutorImpl implements BidExecutor {

    private static final Logger logger = LogManager.getRootLogger();
    private static final Map<String, Queue<BidDto>> queryMap = new HashMap<>();

    @Override
    public void execute(List<BidDto> list) {
        ExecutorService executorServiceForQueue = Executors.newCachedThreadPool();
        ExecutorService executorServiceForObject = Executors.newCachedThreadPool();
        addQueueToMap(list);

        list.forEach(bid -> {
            Thread task = new Thread(() -> {
                String type = bid.getType();
                Queue<BidDto> queue = queryMap.get(type);
                queue.offer(bid);
            });
            executorServiceForObject.submit(task);
        });

        shutdownExecutorService(executorServiceForObject, 1);

        queryMap.forEach((key, value) -> executorServiceForQueue.submit(createTaskForQueue(value)));
        shutdownExecutorService(executorServiceForQueue, 1);
    }

    private void addQueueToMap(List<BidDto> list) {
        list.stream().map(BidDto::getType).distinct().forEach(type -> queryMap.putIfAbsent(type, new ConcurrentLinkedQueue<>()));
    }

    private Thread createTaskForQueue(Queue<BidDto> queue) {
        return new Thread(() -> {
            BidDto value;
            while ((value = queue.poll()) != null) {
                String payload = new String(Base64.getDecoder().decode(value.getPayload()));
                logger.info("Bid id: " + value.getId()
                        + ", TimeStamp: " + value.getTimestamp()
                        + ", Type: " + value.getType()
                        + ", Payload: " + payload);
            }
        });
    }

    private void shutdownExecutorService(ExecutorService service, Integer waitInSeconds) {
        service.shutdown();
        try {
            service.awaitTermination(waitInSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
    }
}
