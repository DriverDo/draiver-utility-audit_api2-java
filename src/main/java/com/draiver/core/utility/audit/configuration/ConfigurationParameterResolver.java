package com.draiver.core.utility.audit.configuration;

public interface ConfigurationParameterResolver {

	String resolve(String value);

	String resolve(Object context, String value);

}
