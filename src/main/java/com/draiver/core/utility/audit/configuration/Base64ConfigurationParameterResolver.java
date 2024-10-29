package com.draiver.core.utility.audit.configuration;

import java.util.Base64;
import java.util.Base64.Decoder;

public class Base64ConfigurationParameterResolver implements ConfigurationParameterResolver {

	public Base64ConfigurationParameterResolver() {
		// leave empty on purpose
	}

	@Override
	public String resolve(String value) {
		return resolve(null, value);
	}

	@Override
	public String resolve(Object context, String value) {
		Decoder decoder = Base64.getDecoder();
		return new String(decoder.decode(value));
	}

}
