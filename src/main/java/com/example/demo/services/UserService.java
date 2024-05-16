package com.example.demo.services;

import java.util.Optional;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repositories.TaskRepository;
import com.example.demo.repositories.UserRepository;

import jakarta.transaction.Transactional;


@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TaskRepository taskRepository;
	
	public User findById(Long id) {
		Optional<User> user = this.userRepository.findById(id);
		return user.orElseThrow(()->new RuntimeException("User not found"));
	}
	@Transactional
	public User create(User obj) {
		obj.setId(null);
		obj = this.userRepository.save(obj);
		this.taskRepository.saveAll(obj.getTasks());
		return obj;
	}
	@Transactional
	public User update(User obj) {
		User newObj = findById(obj.getId());
		newObj.setPassword(obj.getPassword());
		return this.userRepository.save(newObj);
	}
	
	public void delete(Long id) {
		User user = this.findById(id);
		try {
			this.userRepository.deleteById(id);
		}catch (Exception e) {
			throw new RuntimeErrorException(new Error("Não é possivel excluir pois há entidades relacionadas"));
		}
	}
}
