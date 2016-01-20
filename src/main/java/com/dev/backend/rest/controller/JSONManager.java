package com.dev.backend.rest.controller;

import com.google.gson.Gson;

/**
 * Common library for converting from and to JSON.
 * @author creddy
 *
 */
public final class JSONManager {
	

    /** shared gson json to object factory */
    private final static Gson gson = new Gson();

    /**
     * Converts JSON text into the given class object.
     * @param json JSON text
     * @param className name of the class
     * @return object.
     */
	public static <T> T fromJson(String json, Class<T> className) {
		return gson.fromJson(json, className);
	}

	/**
	 * Converts object to JSON text.
	 * @param object object to be converted
	 * @return JSON text
	 */
	public static String toJson(Object object) {
		return gson.toJson(object);
	}

}
