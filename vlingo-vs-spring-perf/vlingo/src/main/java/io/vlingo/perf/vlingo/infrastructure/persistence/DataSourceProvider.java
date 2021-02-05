package io.vlingo.perf.vlingo.infrastructure.persistence;

import io.vlingo.symbio.store.DataFormat;
import io.vlingo.symbio.store.common.jdbc.Configuration;
import io.vlingo.symbio.store.common.jdbc.postgres.PostgresConfigurationProvider;

public class DataSourceProvider {
	
	
	private static DataSourceProvider instance;
	public final Configuration configuration;
	
	public static DataSourceProvider instance() throws Exception{
		if(instance != null) return instance;
		
		final Configuration configuration = PostgresConfigurationProvider.configuration(
				DataFormat.Native, 
				"jdbc:postgresql://localhost/", 
				System.getenv("vlingoDB_NAME"), 
				System.getenv("vlingoDB_USER"), 
				System.getenv("vlingoDB_PASS"), "", true);

		instance = new DataSourceProvider(configuration);
		return instance;
	}
	
	private DataSourceProvider (final Configuration configuration) {
		this.configuration = configuration;
	}

}
