package org.alwyn.shortlink.project.dto.resp.LinkAccessStats;

import lombok.Data;

@Data
public class DailyAccessStatsRespDTO {

    private String date;

    private Integer pv;

    private Integer uv;

    private Integer uip;

}
