package com.hotel.management.users;



public class UserCreatedEvent {
    private final int userId;

    public UserCreatedEvent(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }
}
