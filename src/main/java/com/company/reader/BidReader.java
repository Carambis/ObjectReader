package com.company.reader;

import com.company.dto.BidDto;

import java.io.IOException;
import java.util.List;

public interface BidReader {
    List<BidDto> getDtos() throws IOException;
}
