package com.jshan.batch.jobs.rsvpReport.writer;

import com.jshan.batch.jobs.rsvpReport.persistence.InMemorySummaryMap.SummaryKey;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GuestDailySummaryWriter implements ItemWriter<SummaryKey> {

    private final Map<SummaryKey, Long> summaryCountMap;

    @Override
    public void write(Chunk<? extends SummaryKey> chunk) {
        chunk.getItems().forEach(key -> summaryCountMap.merge(key, 1L, Long::sum));
    }
}
