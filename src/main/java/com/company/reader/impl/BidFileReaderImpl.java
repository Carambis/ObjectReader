package com.company.reader.impl;

import com.company.reader.BidReader;
import com.company.dto.BidDto;
import com.company.util.FileUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BidFileReaderImpl implements BidReader {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public List<BidDto> getDtos() throws IOException {
        ObjectReader reader = mapper.readerForListOf(BidDto.class);
        List<BidDto> bidDtos = reader.readValue(new File(FileUtils.pathToBidFile));
        return bidDtos;
    }
}
