package org.skypro.staradvisor.model;

import java.util.List;

public class RuleStatisticResponse {

    private List<RuleStatisticDto> stats;

    public RuleStatisticResponse() {
    }

    public RuleStatisticResponse(List<RuleStatisticDto> stats) {
        this.stats = stats;
    }

    public List<RuleStatisticDto> getStats() {
        return stats;
    }

    public void setStats(List<RuleStatisticDto> stats) {
        this.stats = stats;
    }
}
