package main.java.com.service;

import main.java.com.domain.Friendship;
import main.java.com.exceptions.ValidationException;
import main.java.com.repo.AbstractRepository;
import main.java.com.validators.FriendshipValidator;

public class FriendshipService extends AbstractService<String, Friendship>{
    private final FriendshipValidator friendshipValidator = new FriendshipValidator();

    public FriendshipService(AbstractRepository<String, Friendship> repository, AbstractRepository<Object, Object> userRepository) {
        super(repository, userRepository);
    }

    public void add(long userId1, long userId2){
        Friendship friendship = new Friendship(userId1, userId2);
        friendshipValidator.validate(friendship);

        if(dependencyRepository != null){
            if(!dependencyRepository.getKeys().contains(userId1) || !dependencyRepository.getKeys().contains(userId2))
                throw new ValidationException("User with an id cannot be found");
        }

        repository.add(friendship.getFriendshipId(),friendship);
    }

    public void remove(long userId1, long userId2){
        Friendship friendship = new Friendship(userId1, userId2);
        friendshipValidator.validate(friendship);

        repository.remove(friendship.getFriendshipId());
    }
}
