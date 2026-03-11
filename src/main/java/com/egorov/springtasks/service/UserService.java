package com.egorov.springtasks.service;


import com.egorov.springtasks.dto.UserDto;
import com.egorov.springtasks.entity.User;
import com.egorov.springtasks.exception.DuplicateEmailException;
import com.egorov.springtasks.exception.ResourceNotFoundException;
import com.egorov.springtasks.mapper.UserMapper;
import com.egorov.springtasks.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return UserMapper.toDtoList(userRepository.findAll());
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + id + " - не найден"));
        return UserMapper.toDto(user);
    }

    public UserDto createUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail()))
            throw new DuplicateEmailException("Такая почта уже существует в базе");

        User user = UserMapper.toEntity(userDto);
        final User saved = userRepository.save(user);
        return UserMapper.toDto(saved);
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + id + " - не найден"));


        if (userDto.getEmail() != null && !userDto.getEmail().equals(user.getEmail()) && userRepository.existsByEmail(userDto.getEmail())) {
            throw new DuplicateEmailException("Email already exists: " + userDto.getEmail());
        }

        UserMapper.updateEntity(user, userDto);
        User updatedUser = userRepository.save(user);
        return UserMapper.toDto(updatedUser);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + id + " - не найден"));
        userRepository.delete(user);
    }
}
