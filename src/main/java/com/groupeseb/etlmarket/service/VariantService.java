package com.groupeseb.etlmarket.service;

import com.groupeseb.etlmarket.model.VariantParameter;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.Path;

public interface VariantService {

	@POST("/common-api/recipes/{sourceSystem}/{functionalId}/variant")
	public void createVariant(
			@Path("sourceSystem") String sourceSystem,
			@Path("functionalId") String functionalId,
			@Body VariantParameter parameter,
			Callback<retrofit.client.Response> cb
	);

}
