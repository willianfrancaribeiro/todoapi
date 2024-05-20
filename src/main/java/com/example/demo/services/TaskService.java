package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.example.demo.repositories.TaskRepository;

import jakarta.transaction.Transactional;

@Service
public class TaskService {
	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private UserService userService;
	
	public Task findById(Long id) {
		Optional<Task> task = this.taskRepository.findById(id);
		return task.orElseThrow(()->new RuntimeException("Tarefa não encontrada"));
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
			throw new RuntimeException("Não foi possivel deletar a task");
		}
	}
}
