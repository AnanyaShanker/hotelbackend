package com.hotel.management.users;

import org.springframework.web.multipart.MultipartFile;

public class UserDTO {
    private Integer userId;
    private Integer roleId;
    private String name;
    private String email;
    private String phone;
    private String password;
    private MultipartFile profileImage;
    private MultipartFile idDocument;  
    private String notes;
    private String status;
    private String securityQuestion;    
    private String securityAnswer;
    

    public UserDTO() {}

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public Integer getRoleId() { return roleId; }
    public void setRoleId(Integer roleId) { this.roleId = roleId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public MultipartFile getProfileImage() { return profileImage; }
    public void setProfileImage(MultipartFile profileImage) { this.profileImage = profileImage; }

    public MultipartFile getIdDocument() { return idDocument; }   // ✅ getter/setter updated
    public void setIdDocument(MultipartFile idDocument) { this.idDocument = idDocument; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getSecurityQuestion() { return securityQuestion; }   // ✅ getter/setter updated
    public void setSecurityQuestion(String securityQuestion) { this.securityQuestion = securityQuestion; }

    public String getSecurityAnswer() { return securityAnswer; }
    public void setSecurityAnswer(String securityAnswer) { this.securityAnswer = securityAnswer; }
}
