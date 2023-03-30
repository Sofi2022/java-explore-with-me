package ru.practicum.requests.dto;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Valid
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventRequestStatusUpdateRequest {

    //private Map<List<Long>, String> requestIds;

    //@NotNull
    private List<Long> requestIds;

    //@NotBlank
    private String status;
}
