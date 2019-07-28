package com.entities.auth;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.entities.AbstractStaticObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "role")
public class Role extends AbstractStaticObject {

	@Id
	private String id;
	@Indexed(unique = true, direction = IndexDirection.DESCENDING, dropDups = true)
	private String roleName;

}