package com.entities.shop;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;

import com.entities.AbstractStaticObject;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.utils.GeoJsonPointDeserializer;
import com.utils.GeoJsonPointSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "shop")
public class Shop extends AbstractStaticObject {
	@Id
	private String id;
	private String name;
	private String description;
	private ShopLikingStatus likeStatus;
	@JsonSerialize(using = GeoJsonPointSerializer.class)
	@JsonDeserialize(using = GeoJsonPointDeserializer.class)
	private GeoJsonPoint location;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    private LocalDateTime likeStatusDate;
}
