 	package com.hotel.management.notifications;

public class NotificationResponseDTO {
    private Integer notificationId;
    private Integer userId;
    private String notificationType;
    private String message;
    private String sentVia;
    private String status;
    private String createdAt;
    private Boolean isDeleted;
    private String deletedAt;
    private String userName;
    private String userEmail;
	public Integer getNotificationId() {
		return notificationId;
	}
	public Integer getUserId() {
		return userId;
	}
	public String getNotificationType() {
		return notificationType;
	}
	public String getMessage() {
		return message;
	}
	public String getSentVia() {
		return sentVia;
	}
	public String getStatus() {
		return status;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public Boolean getIsDeleted() {
		return isDeleted;
	}
	public String getDeletedAt() {
		return deletedAt;
	}
	public String getUserName() {
		return userName;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setNotificationId(Integer notificationId) {
		this.notificationId = notificationId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public void setSentVia(String sentVia) {
		this.sentVia = sentVia;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public void setDeletedAt(String deletedAt) {
		this.deletedAt = deletedAt;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

    
}
