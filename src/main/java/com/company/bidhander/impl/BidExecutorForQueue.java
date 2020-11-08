package com.company.bidhander.impl;

import com.company.dto.BidDto;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class BidExecutorForQueue extends AbstractBidExecutor {

    @Override
    public void execute(List<BidDto> list) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Map<String, List<BidDto>> collect = list.stream().collect(Collectors.groupingBy(BidDto::getType));

        collect.forEach((key, value) -> executorService.submit(createQueueTask(key, value)));

        shutdownExecutorService(executorService, 1);
    }

    private Runnable createQueueTask(String key, List<BidDto> value) {
        return () -> {
            Queue<BidDto> queue = gerOrCreateQueue(key);
            queue.addAll(value);
            logQueue(queue);
        };
    }

    private synchronized Queue<BidDto> gerOrCreateQueue(String key){
        Queue<BidDto> queue = queryMap.get(key);
        if (Objects.isNull(queue)) {
            queue = new ConcurrentLinkedQueue<>();
            queryMap.put(key, queue);
        }
        return queue;
    }
}
