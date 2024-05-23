package com.example.demo.services;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.model.enums.ProfileEnum;
import com.example.demo.repositories.TaskRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.exceptions.DataBindingViolationException;
import com.example.demo.services.exceptions.ObjectNotFoundException;

import jakarta.transaction.Transactional;


@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public User findById(Long id) {
		Optional<User> user = this.userRepository.findById(id);
		return user.orElseThrow(()->new ObjectNotFoundException(
                "Usuário não encontrado! Id: " + id + ", Tipo: " + User.class.getName()));
	}
	@Transactional
	public User create(User obj) {
		obj.setId(null);
		obj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
		obj.setProfiles(Stream.of(ProfileEnum.USER.getCode()).collect(Collectors.toSet()));
		obj = this.userRepository.save(obj);
		this.taskRepository.saveAll(obj.getTasks());
		return obj;
	}
	@Transactional
	public User update(User obj) {
		User newObj = findById(obj.getId());
		newObj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
		return this.userRepository.save(newObj);
	}
	
	public void delete(Long id) {
		User user = this.findById(id);
		try {
			this.userRepository.deleteById(id);
		}catch (Exception e) {
			throw new DataBindingViolationException("Não é possível excluir pois há entidades relacionadas!");
		}
	}
}
