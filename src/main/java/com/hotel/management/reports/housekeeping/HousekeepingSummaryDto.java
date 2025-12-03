package com.hotel.management.reports.housekeeping;

public class HousekeepingSummaryDto {

    private Integer totalTasks;
    private Integer completedTasks;
    private Integer pendingTasks;
    private Integer inProgressTasks;
    private Double completionPercentage;
    private Double averageCompletionTime;

    public HousekeepingSummaryDto(Integer totalTasks, Integer completedTasks,
                                  Integer pendingTasks, Integer inProgressTasks,
                                  Double completionPercentage, Double averageCompletionTime) {
        this.totalTasks = totalTasks;
        this.completedTasks = completedTasks;
        this.pendingTasks = pendingTasks;
        this.inProgressTasks = inProgressTasks;
        this.completionPercentage = completionPercentage;
        this.averageCompletionTime = averageCompletionTime;
    }

	public Integer getTotalTasks() {
		return totalTasks;
	}

	public void setTotalTasks(Integer totalTasks) {
		this.totalTasks = totalTasks;
	}

	public Integer getCompletedTasks() {
		return completedTasks;
	}

	public void setCompletedTasks(Integer completedTasks) {
		this.completedTasks = completedTasks;
	}

	public Integer getPendingTasks() {
		return pendingTasks;
	}

	public void setPendingTasks(Integer pendingTasks) {
		this.pendingTasks = pendingTasks;
	}

	public Integer getInProgressTasks() {
		return inProgressTasks;
	}

	public void setInProgressTasks(Integer inProgressTasks) {
		this.inProgressTasks = inProgressTasks;
	}

	public Double getCompletionPercentage() {
		return completionPercentage;
	}

	public void setCompletionPercentage(Double completionPercentage) {
		this.completionPercentage = completionPercentage;
	}

	public Double getAverageCompletionTime() {
		return averageCompletionTime;
	}

	public void setAverageCompletionTime(Double averageCompletionTime) {
		this.averageCompletionTime = averageCompletionTime;
	}

	@Override
	public String toString() {
		return "HousekeepingSummaryDto [totalTasks=" + totalTasks + ", completedTasks=" + completedTasks
				+ ", pendingTasks=" + pendingTasks + ", inProgressTasks=" + inProgressTasks + ", completionPercentage="
				+ completionPercentage + ", averageCompletionTime=" + averageCompletionTime + "]";
	}

    
}
