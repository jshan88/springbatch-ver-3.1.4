package com.jshan.batch.jobs.rsvpReport.tasklet;

import com.jshan.batch.entity.event.Event;
import com.jshan.batch.repository.event.EventRepository;
import com.jshan.batch.repository.event.GuestGroupRepository;
import com.jshan.batch.repository.event.SubOrganizationRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventMetaDataSetTasklet implements Tasklet {

    private final EventRepository eventRepository;
    private final GuestGroupRepository guestGroupRepository;
    private final SubOrganizationRepository subOrganizationRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        Set<Long> eventIdSet = getActiveRsvpEventIdSet();
//        Map<Long, String> groupMap = getActiveGroupIdAndNameMap(eventIdSet);
//        Map<Long, String> subOrganizationMap = getSubOrganizationMap(eventIdSet);

        StepExecution stepExecution = contribution.getStepExecution();
        ExecutionContext executionContext = stepExecution.getExecutionContext();
        executionContext.put("rsvpEventIdSet", eventIdSet);
//        executionContext.put("groupMap", groupMap);
//        executionContext.put("subOrganizationMap", subOrganizationMap);

        return RepeatStatus.FINISHED;
    }

    private Set<Long> getActiveRsvpEventIdSet() {
        List<Event> events = eventRepository.findAllByActiveFlagIsTrueAndRsvpFlagIsTrue();
        return events.stream()
            .map(Event::getId)
            .collect(Collectors.toSet());
    }

//    private Map<Long, String> getActiveGroupIdAndNameMap(Set<Long> eventIdSet) {
//        List<GuestGroup> guestGroups = guestGroupRepository.findAllByActiveFlagIsTrueAndEventIdIsIn(eventIdSet);
//        Map<Long, String> groupMap = new ConcurrentHashMap<>();
//        guestGroups.forEach(group -> groupMap.put(group.getId(), group.getName()));
//        return groupMap;
//    }
//    private Map<Long, String> getSubOrganizationMap(Set<Long> eventIdSet) {
//        List<SubOrganization> subOrganizations = subOrganizationRepository.findAllByEventIdIsIn(eventIdSet);
//        Map<Long, String> subOrganizationMap = new HashMap<>();
//        subOrganizations.forEach(subOrganization -> subOrganizationMap.put(subOrganization.getId(), getOrganizationName(subOrganization)));
//        return subOrganizationMap;
//    }
//
//    private String getOrganizationName(SubOrganization subOrganization) {
//        String organizationName = subOrganization.getOrganization().getOrganizationName();
//        String subOrganizationName = subOrganization.getSubOrganizationName();
//        return String.format("%s - %s", organizationName, subOrganizationName);
//    }
}
