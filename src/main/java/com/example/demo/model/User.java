package com.example.demo.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.demo.model.enums.ProfileEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
	@Column(name = "password",length = 200,nullable = false)
	@NotNull(groups=CreateUser.class)
	@NotEmpty(groups=CreateUser.class)
	@Size(min=2,max=200)
	private String password;
	
	@OneToMany(mappedBy = "usuario")
	@JsonProperty(access = Access.WRITE_ONLY)
	private List<Task> tasks = new ArrayList<Task>();
	
	@ElementCollection(fetch=FetchType.EAGER)
	@JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
	@CollectionTable(name="user_profile")
	private Set<Integer> profiles = new HashSet<>();
	
	public Set<ProfileEnum> getProfile(){
		return this.profiles.stream().map(x->ProfileEnum.toEnum(x)).collect(Collectors.toSet());
	}
	
	public void addProfile(ProfileEnum profileEnum) {
		this.profiles.add(profileEnum.getCode());
	}
	
}
