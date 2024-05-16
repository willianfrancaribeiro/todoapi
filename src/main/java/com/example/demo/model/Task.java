package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name=Task.TABLE_NAME)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Task {
	public static final String TABLE_NAME = "task";
	
	@Id
	@Column(name = "id",unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(optional=true)
	@JoinColumn(name="user_id",referencedColumnName="id",nullable=true,updatable=false)
	private User usuario;
	
	@Column(name="description",length=255,nullable=false)
	@NotNull
	@NotEmpty
	@Size(min= 1,max=255)
	private String description;
	
}
