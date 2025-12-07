package com.hotel.management.users;
 
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.management.exception.DuplicateEmailException;
import com.hotel.management.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
 
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
 
@Service
public class UserServiceImpl implements UserService {
 
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ApplicationEventPublisher eventPublisher;
 
	private final ObjectMapper objectMapper = new ObjectMapper();
 
	@Override
	public UserDTO createUser(UserDTO dto) {
	    // map dto -> user
		
		if (userRepository.emailExists(dto.getEmail())) {
	        throw new DuplicateEmailException("Email already registered");
	    }
	    User user = UserMapper.toUser(dto);
 
	    // 🔹 PASSWORD SALT & HASH
	    String salt = Utils.generateSalt();
	    String hashed = Utils.generateHash(salt + dto.getPassword());
	    user.setPasswordSalt(salt);
	    user.setPasswordHash(hashed);
 
	    // 🔹 SECURITY QUESTION & ANSWER  ❗ PLACE IT HERE
	    if (dto.getSecurityQuestion() == null || dto.getSecurityAnswer() == null) {
	        throw new RuntimeException("Security question and answer are required");
	    }
	    String secSalt = Utils.generateSalt();
	    String secHash = Utils.generateHash(secSalt + dto.getSecurityAnswer());
	    user.setSecurityQuestions(dto.getSecurityQuestion());
	    user.setSecurityAnswerSalt(secSalt);
	    user.setSecurityAnswerHash(secHash);
 
	    // 🔹 FILE UPLOAD (already existing logic)
	    String profilePath = null;
	    if (dto.getProfileImage() != null && !dto.getProfileImage().isEmpty()) {
	        profilePath = storeFile(dto.getProfileImage());
	        user.setProfileImage(profilePath);
	    }
 
	    String idDocPath = null;
	    if (dto.getIdDocument() != null && !dto.getIdDocument().isEmpty()) {
	        idDocPath = storeFile(dto.getIdDocument());
	        user.setiDocument(idDocPath);
	    }
 
	    if (user.getStatus() == null)
	        user.setStatus("active");
 
	    int generatedId = userRepository.save(user);
	    user.setUserId(generatedId);
	    
	    eventPublisher.publishEvent(new UserCreatedEvent(user.getUserId()));
 
	    return UserMapper.toDTO(user);
	}
 
	private String storeFile(MultipartFile file) {
		try {
			String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename().replaceAll("\\s+", "_");
			Path uploadPath = Paths.get(Utils.UPLOAD_DIR);
			Files.createDirectories(uploadPath);
			Path filePath = uploadPath.resolve(fileName);
			Files.write(filePath, file.getBytes());
			return filePath.toString();
		} catch (IOException e) {
			throw new RuntimeException("Failed to store file: " + e.getMessage(), e);
		}
	}
 
	public UserDTO getUserById(int userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		return UserMapper.toDTO(user);
	}
 
	@Override
	public UserDTO getUserByEmail(String email) {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
		return UserMapper.toDTO(user);
	}
 
	@Override
	public List<UserDTO> getAllUsers() {
		List<User> users = userRepository.findAll();
		return users.stream().map(UserMapper::toDTO).collect(Collectors.toList());
	}
 
	@Override
	public UserDTO updateUser(int userId, UserDTO dto) {
		User existing = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
// update allowed fields
		if (dto.getRoleId() != null)
			existing.setRoleId(dto.getRoleId());
		if (dto.getName() != null)
			existing.setName(dto.getName());
		if (dto.getPhone() != null)
			existing.setPhone(dto.getPhone());
		if (dto.getNotes() != null)
			existing.setNotes(dto.getNotes());
		if (dto.getStatus() != null)
			existing.setStatus(dto.getStatus());
 
		if (dto.getProfileImage() != null && !dto.getProfileImage().isEmpty()) {
			String p = storeFile(dto.getProfileImage());
			existing.setProfileImage(p);
		}
		if (dto.getIdDocument() != null && !dto.getIdDocument().isEmpty()) {
			String d = storeFile(dto.getIdDocument());
			existing.setiDocument(d);
		}
 
		userRepository.update(existing);
		return UserMapper.toDTO(existing);
	}
 
	@Override
	public UserDTO login(String email, String password) {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
		String computed = Utils.generateHash(user.getPasswordSalt() + password);
		if (!computed.equals(user.getPasswordHash()))
			throw new RuntimeException("Invalid credentials");
		return UserMapper.toDTO(user);
	}

	// ✅ NEW: Verify password without full login
	@Override
	public boolean verifyPassword(String email, String password) {
		try {
			User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
			String computed = Utils.generateHash(user.getPasswordSalt() + password);
			return computed.equals(user.getPasswordHash());
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public int toggleStatus(int userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		String newStatus = "active".equalsIgnoreCase(user.getStatus()) ? "inactive" : "active";
		int rows = userRepository.updateStatus(userId, newStatus);
		return "active".equalsIgnoreCase(newStatus) ? 1 : 0;
	}
 
	@Override
	public void deleteUser(int userId) {
		userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		userRepository.deleteById(userId);
	}
	public Map<String,Object> getSecurityQuestion(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with given email"));
        return Map.of(
        		"userId",user.getUserId(),
        		"securityQuestion",user.getSecurityQuestions());
    }
 
    // ──────────────────────────────────────────────
    // Forgot Password Step 2 → Validate answer & reset password
 
    public boolean resetPassword(String email, String answer, String newPassword) {
 
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
 
        // Validate answer
        String computedHash = Utils.generateHash(user.getSecurityAnswerSalt() + answer);
        if (!computedHash.equals(user.getSecurityAnswerHash())) {
            return false; // wrong answer
        }
 
        // Generate new password salt + hash
        String newSalt = Utils.generateSalt();
        String newHash = Utils.generateHash(newSalt + newPassword);
 
        // Update database
        userRepository.updatePasswordAndSalt(user.getUserId(), newSalt, newHash);
 
        return true;
    }
}