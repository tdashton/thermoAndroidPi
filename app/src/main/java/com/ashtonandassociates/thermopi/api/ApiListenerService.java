package com.ashtonandassociates.thermopi.api;

import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by theKernel on 13.03.2018.
 */
public class ApiListenerService {

	protected static String TAG = ApiListenerService.class.getSimpleName();

	protected static ApiListenerService instance;

	protected ArrayList<ApiListenerInterface> listeners;

	public static ApiListenerService getInstance() {
		if (ApiListenerService.instance == null) {
			ApiListenerService.instance = new ApiListenerService();
		}
		return ApiListenerService.instance;
	}

	protected ApiListenerService() {
		this.listeners = new ArrayList<>();
	}

	public void registerListener(ApiListenerInterface listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	public void unregisterListener(ApiListenerInterface listener) {
		if (listeners.contains(listener)) {
			listeners.remove(listener);
		}
	}

	public void notifyApiListenersFailure(RetrofitError error) {

	}

	public void notifyApiListeners(Object responseClass) {
		for(ApiListenerInterface frag : this.listeners) {
			if(frag == null) {
				return;
			}
			Class theClass = frag.getClass();
			try	{
				for(Method met : theClass.getMethods()){
					if(met.isAnnotationPresent(com.ashtonandassociates.thermopi.api.annotation.ApiListener.class)) {
						Class annotationResponseClass = met.getAnnotation(com.ashtonandassociates.thermopi.api.annotation.ApiListener.class).value();
						if(annotationResponseClass == responseClass.getClass()) {
							met.invoke(frag, responseClass);
						}
					}
				}
			} catch(IllegalAccessException iae) {
				Log.e(TAG, iae.toString());
			} catch(InvocationTargetException ite) {
				Log.e(TAG, "ite " + ite.toString());
			}
		}
	}
}
