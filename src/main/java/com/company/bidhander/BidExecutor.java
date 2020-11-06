package com.company.bidhander;

import com.company.dto.BidDto;

import java.util.List;

public interface BidExecutor {
    void execute(List<BidDto> list);
}
