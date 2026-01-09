package com.domain;

public class Friendship {
    private final long userId1;
    private final long userId2;
    private final String friendshipId;

    public Friendship(long userId1, long userId2) {
        if (userId1 > userId2) {
            long temp = userId1;
            userId1 = userId2;
            userId2 = temp;
        }

        this.userId1 = userId1;
        this.userId2 = userId2;
        this.friendshipId = userId1 + "-" + userId2;
    }

    public long getUserId1() {
        return userId1;
    }

    public long getUserId2() {
        return userId2;
    }

    public String getFriendshipId() {
        return friendshipId;
    }
}
