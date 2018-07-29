package org.pirateislands.skyblock.dto;

/**
 * Owned by SethyCorp, and KueMedia respectively.
 **/
public class Streak {
    private Integer days;
    private Long callDown;

    public Streak(final Integer days, final Long callDown) {
        this.days = days;
        this.callDown = callDown;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Long getCallDown() {
        return callDown;
    }

    public void setCallDown(Long callDown) {
        this.callDown = callDown;
    }
}
