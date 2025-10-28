package main.java.com.service;

import main.java.com.domain.Friendship;
import main.java.com.repo.AbstractRepository;
import main.java.com.validators.FriendshipValidator;

public class FriendshipService extends AbstractService<String, Friendship>{
    private final FriendshipValidator friendshipValidator = new FriendshipValidator();

    public FriendshipService(AbstractRepository<String, Friendship> repository) {
        super(repository);
    }

    public void add(long userId1, long userId2){
        Friendship friendship = new Friendship(userId1, userId2);
        friendshipValidator.validate(friendship);

        repository.add(friendship.getFriendshipId(),friendship);
    }

    public void remove(long userId1, long userId2){
        Friendship friendship = new Friendship(userId1, userId2);
        friendshipValidator.validate(friendship);

        repository.remove(friendship.getFriendshipId());
    }
}
