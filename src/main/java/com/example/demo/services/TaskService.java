package com.example.demo.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.example.demo.model.enums.ProfileEnum;
import com.example.demo.repositories.TaskRepository;
import com.example.demo.security.UserSpringSecurity;
import com.example.demo.services.exceptions.AuthorizationException;
import com.example.demo.services.exceptions.DataBindingViolationException;

import jakarta.transaction.Transactional;

@Service
public class TaskService {
	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private UserService userService;
	
	public Task findById(Long id) {
		Task task = this.taskRepository.findById(id)
				.orElseThrow(()->new RuntimeException(
				"Tarefa não encontrada"));
		
		UserSpringSecurity userSpringSecurity = UserService.authenticated();
		
		if(Objects.isNull(userSpringSecurity) 
				|| !userSpringSecurity.hasRole(ProfileEnum.ADMIN)
				&& !userHasTask(userSpringSecurity,task))
			throw new AuthorizationException("Acesso negado");
		
		return task;
	}
	
	public List<Task> findAllByUserId(Long userId){
		List<Task> task = this.taskRepository.findByUsuario_Id(userId);
		return task;
	}
	
	@Transactional
	public Task create(Task obj) {
		User user = this.userService.findById(obj.getUsuario().getId());
		obj.setId(null);
		obj.setUsuario(user);
		obj = this.taskRepository.save(obj);
		return obj;
	
	}
	@Transactional
	public Task update(Task obj) {
		Task newObj = findById(obj.getId());
		newObj.setDescription(obj.getDescription());
		return this.taskRepository.save(newObj);
	}
	public void delete(Long id) {
		findById(id);
		try {
			this.taskRepository.deleteById(id);
		}catch(Exception e) {
			throw new DataBindingViolationException("Não é possível excluir pois há entidades relacionadas!");
		}
	}
	private Boolean userHasTask(UserSpringSecurity userSpringSecurity,Task task) {
		return task.getUsuario().getId().equals(userSpringSecurity.getId());
	}
	
	
}
