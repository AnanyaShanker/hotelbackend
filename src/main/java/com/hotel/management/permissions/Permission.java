package com.hotel.management.permissions;

public class Permission {
	private Integer permissionId;
	private PermissionName permissionName;
	private String description;
	public Permission() {
		super();
	}
	public Permission(Integer permissionId, PermissionName permissionName, String description) {
		super();
		this.permissionId = permissionId;
		this.permissionName = permissionName;
		this.description = description;
	}
	public Integer getPermissionId() {
		return permissionId;
	}
	public void setPermissionId(Integer permissionId) {
		this.permissionId = permissionId;
	}
	public PermissionName getPermissionName() {
		return permissionName;
	}
	public void setPermissionName(PermissionName permissionName) {
		this.permissionName = permissionName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "Permission [permissionId=" + permissionId + ", permissionName=" + permissionName + ", description="
				+ description + "]";
	}
	
	
	
	

}
