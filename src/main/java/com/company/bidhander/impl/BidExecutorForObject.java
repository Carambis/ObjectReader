package com.company.bidhander.impl;

import com.company.dto.BidDto;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BidExecutorForObject extends AbstractBidExecutor {

    @Override
    public void execute(List<BidDto> list) {
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

        queryMap.forEach((key, value) -> logQueue(value));
    }

    private void addQueueToMap(List<BidDto> list) {
        list.stream().map(BidDto::getType).distinct().forEach(type -> queryMap.putIfAbsent(type, new ConcurrentLinkedQueue<>()));
    }
}
