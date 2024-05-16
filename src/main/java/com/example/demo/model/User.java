package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = User.TABLE_NAME)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
	public interface UpdateUser{}
	public interface CreateUser{}
	
	
	public static final String TABLE_NAME = "user";
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",unique = true)
	private Long id;
	
	@Column(name = "username",length = 100,nullable = false,unique = true)
	@NotNull(groups=CreateUser.class)
	@NotEmpty(groups=CreateUser.class)
	@Size(min=2,max=100)
	private String username;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@Column(name = "password",length = 8,nullable = false)
	@NotNull(groups=CreateUser.class)
	@NotEmpty(groups=CreateUser.class)
	@Size(min=2,max=8)
	private String password;
	
	@OneToMany(mappedBy = "usuario")
	@JsonProperty(access = Access.WRITE_ONLY)
	private List<Task> tasks = new ArrayList<Task>();
		
}
