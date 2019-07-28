package com.config;

import java.io.IOException;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import org.springframework.data.mongodb.core.query.Query;

import com.entities.AbstractStaticObject;
import com.entities.auth.Role;
import com.entities.auth.User;
import com.entities.shop.Shop;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.utils.EntityHelper;

import io.micrometer.core.instrument.util.IOUtils;

@Configuration
@DependsOn("mongoTemplate")
public class CollectionsConfig {

	@Autowired
	private MongoTemplate mongoTemplate;
	

	@PostConstruct
	public void initIndexes() throws IOException {
		mongoTemplate.indexOps(Shop.class)
				.ensureIndex(new GeospatialIndex("location").typed(GeoSpatialIndexType.GEO_2DSPHERE));
		this.populateStaticData(Role[].class);
		this.populateStaticData(Shop[].class);

	}


	private void populateStaticData(Class<? extends AbstractStaticObject[]> type) throws IOException, JSONException {

		ObjectMapper jsonMapper = new ObjectMapper();
		Class<?> classType = type.getComponentType();

		jsonMapper.findAndRegisterModules();

		// generate collection name form type class name minus array type
		char[] name = classType.getSimpleName().toCharArray();
		name[0] = Character.toLowerCase(name[0]);
		String collectionName = new String(name);

		// Check collection existence
		if (mongoTemplate.count(new Query(), classType) == 0) {

			if (!mongoTemplate.getCollectionNames().contains(collectionName)) {
				mongoTemplate.createCollection(collectionName);
			}

			// get object list from JSON resource
			String s = IOUtils.toString(
					this.getClass().getClassLoader().getResourceAsStream("static-data/db." + collectionName + ".json"));
			JSONObject jsonObject = new JSONObject(s);
			JSONArray jsonArray = jsonObject.getJSONArray("data");
			AbstractStaticObject[] staticObjects = jsonMapper.readValue(jsonArray.toString(), type);

			// loop on every object
			Arrays.asList(staticObjects).forEach(staticObject -> {
				// set dates information
				EntityHelper.createEntityDatesInformation(staticObject);
				mongoTemplate.insert(staticObject, collectionName);
			});
		}
	}

}
