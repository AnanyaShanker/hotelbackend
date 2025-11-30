package com.hotel.management.users;
 
 
 
public class   UserMapper {
	public static User toUser(UserDTO dto) {
		if(dto==null) return null;
		 User user = new User();
		 if(dto.getUserId()!=null)user.setUserId(dto.getUserId());
		 if(dto.getRoleId()!=null)user.setRoleId(dto.getRoleId());
		 user.setName(dto.getName());
		 user.setEmail(dto.getEmail());
		 user.setPhone(dto.getPhone());
		 user.setNotes(dto.getNotes());
		 user.setStatus(dto.getStatus());
		 user.setSecurityQuestions(dto.getSecurityQuestion());
		 return user;
		
		 }
 
		 public static  UserDTO toDTO(User user) {
			 if(user==null) return null;
		 UserDTO dto = new UserDTO();
		 dto.setUserId(user.getUserId());
		 dto.setRoleId(user.getRoleId());
		 dto.setName(user.getName());
		 dto.setEmail(user.getEmail());
		 dto.setPhone(user.getPhone());
		 dto.setNotes(user.getNotes());
		 dto.setStatus(user.getStatus());
		 dto.setSecurityQuestion(user.getSecurityQuestions());
		
		 return dto;
		 }
 
}