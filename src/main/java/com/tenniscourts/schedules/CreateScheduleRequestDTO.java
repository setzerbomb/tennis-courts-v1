package com.tenniscourts.schedules;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class CreateScheduleRequestDTO {

    @NotNull
    private Long tennisCourtId;

    @ApiModelProperty(required = true,example = "2021-10-16T12:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull
    private LocalDateTime startDateTime;

    @ApiModelProperty(required = true,example = "2021-10-16T18:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull
    private LocalDateTime endDateTime;

}
