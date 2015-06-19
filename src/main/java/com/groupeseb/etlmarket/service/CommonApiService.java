package com.groupeseb.etlmarket.service;

import com.groupeseb.etlmarket.model.Fid;
import com.groupeseb.etlmarket.model.VariantParameter;
import com.squareup.okhttp.OkHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class CommonApiService {

	private final String endpoint;

	private final String destMarket;

	private final String destLang;

	private final String apiKey;

	private final FidService fidService;

	private final VariantService variantService;

	private RestAdapter restAdapter() {

		OkHttpClient httpClient = new OkHttpClient();
		httpClient.setConnectTimeout(100, TimeUnit.MINUTES);
		httpClient.setReadTimeout(100, TimeUnit.MINUTES);
		httpClient.setWriteTimeout(100, TimeUnit.MINUTES);

		return new RestAdapter.Builder()
				.setEndpoint(endpoint)
				.setRequestInterceptor(new RequestInterceptor() {
					@Override
					public void intercept(RequestFacade request) {
						request.addHeader("apiKey", apiKey);
						request.addHeader("Content-type", "application/json");
					}
				})
				.setClient(new OkClient(httpClient))
				.setLogLevel(RestAdapter.LogLevel.HEADERS_AND_ARGS)
				.setLog(new RestAdapter.Log() {
					@Override
					public void log(String message) {
						log.info(message);
					}
				})
				.build();
	}

	@Autowired
	public CommonApiService(@Value("${commonapi.endpoint:'http://dev.api.openfoodsystem.com'}") String endpoint,
	                        @Value("${market.variant:GS_CH}") String destMarket,
	                        @Value("${lang.variant:fr}") String destLang,
	                        @Value("${apiKey:NX3XWcm0TTgCeShuNciSJjXaP21WRQMW}") String apiKey,
	                        FidService fidService) {
		this.endpoint = endpoint;
		this.destMarket = destMarket;
		this.destLang = destLang;
		this.apiKey = apiKey;
		this.fidService = fidService;
		variantService = restAdapter().create(VariantService.class);
	}

	public void createVariants() {
		int cnt = 0;
		Collection<Fid> fids = fidService.getFids();

		log.info("Creating recipes on the market {}, lang {}", destMarket, destLang);

		for (Fid fid : fids) {
			try {
				Thread.sleep(300l);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			cnt ++;
			log.info("Handling recipe n° {}", cnt);
			variantService.createVariant(fid.getSourceSystem(), fid.getFunctionalId(), new VariantParameter(destMarket, destLang), new Callback<retrofit.client.Response>() {
				@Override
				public void success(retrofit.client.Response response2, Response response) {
					log.info(response.toString());
				}

				@Override
				public void failure(RetrofitError error) {
					log.error("Error contacting {}  : {}",error.getUrl(), error.toString());
				}
			});
		}
	}
}
