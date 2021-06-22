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
package com.yugabyte.data.jdbc.core;

import org.springframework.data.jdbc.core.JdbcAggregateOperations;

/**
 * Specifies Database operations one can perform on YugabyteDB Distributed SQL, 
 * based on an <em>Domain Type</em>.
 *
 * @author Nikhil Chandrappa
 */
public interface YsqlOperations extends JdbcAggregateOperations {
	
	/**
	 * Counts the number of objects of a given type.
	 * 
	 * Method uses below transaction isolation level for count(*) queries, 
	 * Transaction BEGIN ISOLATION LEVEL SERIALIZABLE READ ONLY DEFERRABLE 
	 * 
	 * @param domainType the type of the aggregates to be counted.
	 * @return the number of instances stored in the YugabyteDB cluster. Guaranteed to be not {@code null}.
	 */
	long count(Class<?> domainType, QueryOptions queryOptions);
	
//	/**
//	 * Insert objects into database using batch semantics.  This skips the test 
//	 * if the objects being inserted is new and makes an insert.
//	 * <p>
//	 * This is useful if the client provides an id for Objects being stored.
//	 * </p>
//	 * 
//	 * @param instancees of objects being inserted into the database.
//	 * @param <T> Type of the objects being inserted.
//	 */
//	 <T> void insertAll(Iterable<T> instances, QueryOptions queryOptions);
	 
	 
//	/**
//	 * Updates or Inserts object into database. 
//	 * <p>
//	 * This is used to support upsert functionality. 
//	 * </p>
//	 * 
//	 * @param instancees of object being Updated or Inserted.
//	 * @param specify action to be performed on conflict during upsert operation.
//	 * @param <T> Type of the objects being updated or inserted.
//	 */
//	<T> T upsert(T instance, QueryOptions queryOptions);

//	/**
//	 * Load an aggregate from the database.
//	 *
//	 * @param id the id of the domain to load. Must not be {@code null}.
//	 * @param domainType the type of the aggregate root. Must not be {@code null}.
//	 * @param <T> the type of the aggregate root.
//	 * @param determines whether data can be read from follower copies.
//	 * @return the loaded aggregate. Might return {@code null}.
//	 */
//	@Nullable
//	<T> T findById(Object id, Class<T> domainType, Boolean readFromFollower);
//
//	/**
//	 * Load all aggregates of a given type that are identified by the given ids.
//	 *
//	 * @param ids of the aggregate roots identifying the aggregates to load. Must not be {@code null}.
//	 * @param domainType the type of the aggregate roots. Must not be {@code null}.
//	 * @param <T> the type of the aggregate roots. Must not be {@code null}.
//	 * @param determines whether data can be read from follower copies.
//	 * @return Guaranteed to be not {@code null}.
//	 */
//	<T> Iterable<T> findAllById(Iterable<?> ids, Class<T> domainType, Boolean readFromFollower);
//
//	/**
//	 * Load all aggregates of a given type.
//	 *
//	 * @param domainType the type of the aggregate roots. Must not be {@code null}.
//	 * @param <T> the type of the aggregate roots. Must not be {@code null}.
//	 * @param determines whether data can be read from follower copies.
//	 * @return Guaranteed to be not {@code null}.
//	 */
//	<T> Iterable<T> findAll(Class<T> domainType, Boolean readFromFollower);
//
//	/**
//	 * Load all aggregates of a given type, sorted.
//	 *
//	 * @param domainType the type of the aggregate roots. Must not be {@code null}.
//	 * @param <T> the type of the aggregate roots. Must not be {@code null}.
//	 * @param sort the sorting information. Must not be {@code null}.
//	 * @param determines whether data can be read from follower copies.
//	 * @return Guaranteed to be not {@code null}.
//	 * @since 2.0
//	 */
//	<T> Iterable<T> findAll(Class<T> domainType, Sort sort, Boolean readFromFollower);
//
//	/**
//	 * Load a page of (potentially sorted) aggregates of a given type.
//	 *
//	 * @param domainType the type of the aggregate roots. Must not be {@code null}.
//	 * @param <T> the type of the aggregate roots. Must not be {@code null}.
//	 * @param pageable the pagination information. Must not be {@code null}.
//	 * @param determines whether data can be read from follower copies.
//	 * @return Guaranteed to be not {@code null}.
//	 * @since 2.0
//	 */
//	<T> Page<T> findAll(Class<T> domainType, Pageable pageable, Boolean readFromFollower);

}
