package com.rayer.util.provisioner;

import java.io.IOException;

public interface ResourceProvisioner<T> {
	T getResource(String identificator) throws IOException;
	boolean setResource(String identificator, T targetResource);
	boolean dereferenceResource(String identificator);
	boolean clearAllCachedResource();
}