package ua.alisasira.validation.service;

import org.springframework.stereotype.Component;
import ua.alisasira.validation.entity.User;
import ua.alisasira.validation.exception.ResourceNotFoundException;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Component
public class UserServiceImpl implements UserService{

    private final AtomicLong idGenerator = new AtomicLong();
    private final Map<Long, User> storage = new ConcurrentHashMap<>();

    @Override
    public User create(User user) {
        user.setId(idGenerator.incrementAndGet());
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if(user == null){
            throw new ResourceNotFoundException("User identifier is NULL.");
        }

        User exist = storage.get(user.getId());
        if (exist == null) {
            throw new ResourceNotFoundException("User does not exist!");
        }
        updateFields(user, exist);
        return exist;
    }

    @Override
    public boolean delete(Long id) {
        User exist = storage.remove(id);
        return exist != null;
    }

    @Override
    public List<User> search(LocalDate from, LocalDate to) {
        return storage.values().stream()
                .filter(user -> from.isBefore(user.getBirthDate()) && to.isAfter(user.getBirthDate()))
                .sorted(Comparator
                        .comparing(User::getLastName)
                        .thenComparing(User::getFirstName))
                .collect(Collectors.toList());
    }

    @Override
    public void reset() {
        storage.clear();
    }

    private void updateFields(User source, User target) {
        if (source.getEmail() != null) {
            target.setEmail(source.getEmail());
        }
        if (source.getFirstName() != null){
            target.setFirstName(source.getFirstName());
        }

        if (source.getLastName() != null){
            target.setLastName(source.getLastName());
        }

        if (source.getBirthDate() != null){
            target.setBirthDate(source.getBirthDate());
        }

        if (source.getAddress() != null){
            target.setAddress(source.getAddress());
        }

        if (source.getPhoneNumber() != null){
            target.setPhoneNumber(source.getPhoneNumber());
        }
    }
}
