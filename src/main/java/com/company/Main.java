package com.company;

import com.company.bidhander.BidExecutor;
import com.company.reader.impl.BidFileReaderImpl;
import com.company.bidhander.impl.BidExecutorImpl;
import com.company.reader.BidReader;
import io.reactivex.rxjava3.core.Observable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final BidReader reader = new BidFileReaderImpl();
    private static final BidExecutor executor = new BidExecutorImpl();
    private static final Logger logger = LogManager.getRootLogger();

    public static void main(String[] args) throws IOException {
        Observable.interval(0, 1, TimeUnit.MINUTES)
                .map(result -> reader.getDtos())
                .subscribe(executor::execute, error -> logger.error(error.getMessage()));
        System.in.read();
    }
}
