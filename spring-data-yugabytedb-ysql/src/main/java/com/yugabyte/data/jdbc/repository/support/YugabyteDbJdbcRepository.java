/*
 * Copyright (c) Yugabyte, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except 
 * in compliance with the License.  You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License 
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express 
 * or implied.  See the License for the specific language governing permissions and limitations 
 * under the License.
*/
package com.yugabyte.data.jdbc.repository.support;

import org.springframework.data.jdbc.repository.support.SimpleJdbcRepository;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.transaction.annotation.Transactional;

import com.yugabyte.data.jdbc.core.YugabyteDbYsqlOperations;

/**
 * The YugabyteDB repository implementation to support YSQL distributed SQL operations.
 *
 * @author Nikhil Chandrappa
 */
@Transactional(readOnly = true)
public class YugabyteDbJdbcRepository<T, ID> extends SimpleJdbcRepository<T, ID> {
	
	
	private final YugabyteDbYsqlOperations entityOperations;
	private final PersistentEntity<T, ?> entity;

	public YugabyteDbJdbcRepository(YugabyteDbYsqlOperations entityOperations, PersistentEntity<T, ?> entity) {
		super(entityOperations, entity);
		this.entityOperations = entityOperations;
		this.entity = entity;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.CrudRepository#count()
	 */
	@Override
	public long count() {
		return entityOperations.count(entity.getType());
	}
	
	

}
