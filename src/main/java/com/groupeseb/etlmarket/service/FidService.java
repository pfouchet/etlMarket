package com.groupeseb.etlmarket.service;

import com.groupeseb.etlmarket.model.Fid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class FidService {

	private final MongoTemplate mongoTemplate;

	private final String orignalMarket;

	private final String originalLang;

	@Autowired
	public FidService(
			@Value("${market.origin:GS_FR}") String originalMarket,
			@Value("${lang.origin:fr}") String originalLang,
			MongoTemplate mongoTemplate) {
		this.orignalMarket = originalMarket;
		this.originalLang = originalLang;
		this.mongoTemplate = mongoTemplate;
	}

	private Criteria getCriteria(String marketId, String domainId, String langId) {
		return Criteria.where("market.$id").is(marketId).and("domain.$id").is(domainId).and("lang.$id").is(langId);
	}

	public List<Fid> getFids() {

//		Criteria criteria = new Criteria();

//		criteria.orOperator(
//				getCriteria("GS_FR", "PRO_COM", "fr"),
//				getCriteria("GS_DE", "PRO_COM", "de")
//		);
		Query query = new Query();
		query.addCriteria(getCriteria(orignalMarket, "PRO_COM", originalLang)).fields().include("fid").exclude("_id");

		log.info("There are {} recipes to process", mongoTemplate.count(query, Object.class, "recipe"));

		List rawFids = mongoTemplate.find(query, Object.class, "recipe");

		List<Fid> fids = new ArrayList<>();

		for (Object rawFid : rawFids) {
			Map fid = (Map)((Map) rawFid).get("fid");
			fids.add(new Fid((String) fid.get("functionalId"),(String) ((Map)fid.get("sourceSystem")).get("_id"), (String) fid.get("version")));
		}

		return fids;
	}
}
