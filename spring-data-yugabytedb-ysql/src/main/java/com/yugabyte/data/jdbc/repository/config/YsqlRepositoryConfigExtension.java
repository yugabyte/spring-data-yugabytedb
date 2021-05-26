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
package com.yugabyte.data.jdbc.repository.config;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport;
import org.springframework.data.repository.config.RepositoryConfigurationSource;
import org.springframework.util.StringUtils;

import com.yugabyte.data.jdbc.repository.support.YsqlRepositoryFactoryBean;

/**
 * {@link org.springframework.data.repository.config.RepositoryConfigurationExtension} extending the repository
 * registration process by registering YugabyteDB YSQL repositories.
 *
 * @author Nikhil Chandrappa
 */
public class YsqlRepositoryConfigExtension extends RepositoryConfigurationExtensionSupport {
	
	private static final String DEFAULT_TRANSACTION_MANAGER_BEAN_NAME = "transactionManager";

	
	@Override
	public String getModuleName() {
		return "YUGABYTEDB_YSQL";
	}
	
	@Override
	public String getRepositoryFactoryBeanClassName() {

		return YsqlRepositoryFactoryBean.class.getName();
	}

	@Override
	protected String getModulePrefix() {

		return getModuleName().toLowerCase(Locale.US);
	}
	
	@Override
	public void postProcess(BeanDefinitionBuilder builder, RepositoryConfigurationSource source) {

		source.getAttribute("jdbcOperationsRef") //
				.filter(StringUtils::hasText) //
				.ifPresent(s -> builder.addPropertyReference("jdbcOperations", s));

		source.getAttribute("dataAccessStrategyRef") //
				.filter(StringUtils::hasText) //
				.ifPresent(s -> builder.addPropertyReference("dataAccessStrategy", s));

		Optional<String> transactionManagerRef = source.getAttribute("transactionManagerRef");
		builder.addPropertyValue("transactionManager", transactionManagerRef.orElse(DEFAULT_TRANSACTION_MANAGER_BEAN_NAME));
	}
	
	@Override
	protected Collection<Class<? extends Annotation>> getIdentifyingAnnotations() {
		return Collections.singleton(Table.class);
	}

}
