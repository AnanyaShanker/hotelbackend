package com.hotel.management.users;
 
import java.sql.Timestamp;
 
public class User {
	private Integer userId;
	private Integer roleId;
    private String name;
    private String email;
    private String phone;
    private String passwordHash;
    private String passwordSalt;
    private String profileImage;
	private String iDocument;
    private String notes;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String securityQuestions;
    private String securityAnswerHash;
    private String securityAnswerSalt;
	public User() {
		super();
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPasswordHash() {
		return passwordHash;
	}
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	public String getPasswordSalt() {
		return passwordSalt;
	}
	public void setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}
	public String getProfileImage() {
		return profileImage;
	}
	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}
	public String getiDocument() {
		return iDocument;
	}
	public void setiDocument(String iDocument) {
		this.iDocument = iDocument;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public Timestamp getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getSecurityQuestions() {
		return securityQuestions;
	}
	public void setSecurityQuestions(String securityQuestions) {
		this.securityQuestions = securityQuestions;
	}
	public String getSecurityAnswerHash() {
		return securityAnswerHash;
	}
	public void setSecurityAnswerHash(String securityAnswerHash) {
		this.securityAnswerHash = securityAnswerHash;
	}
	public String getSecurityAnswerSalt() {
		return securityAnswerSalt;
	}
	public void setSecurityAnswerSalt(String securityAnswerSalt) {
		this.securityAnswerSalt = securityAnswerSalt;
	}
	@Override
	public String toString() {
		return "User [userId=" + userId + ", roleId=" + roleId + ", name=" + name + ", email=" + email + ", phone="
				+ phone + ", passwordHash=" + passwordHash + ", passwordSalt=" + passwordSalt + ", profileImage="
				+ profileImage + ", iDocument=" + iDocument + ", notes=" + notes + ", status=" + status + ", createdAt="
				+ createdAt + ", updatedAt=" + updatedAt + "]";
	}
    
 
}