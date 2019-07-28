package com.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

import com.common.ResultPage;
import com.entities.shop.Shop;
import com.entities.shop.ShopLikingStatus;
import com.repositories.ShopRespository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ShopService {
	@Autowired
	private ShopRespository shopRespository;

	public ResultPage<Shop> nearByShops(Double longitude, Double latitude, Integer page, Integer size) {
		ResultPage<Shop> resultPage = new ResultPage<>();
		try {
	GeoJsonPoint userLocation =new GeoJsonPoint(longitude,latitude);
		Page<Shop> shopPageDisliked= shopRespository.findByLikeStatusAndLikeStatusDateLessThan( ShopLikingStatus.DISLIKED, LocalDateTime.now().minusHours(2), PageRequest.of(page, size));
	    for (Shop shop : shopPageDisliked) {
	    	shop.setLikeStatus(ShopLikingStatus.NEUTRAL);
	    	shopRespository.save(shop);
	    }
	    Page<Shop> shopPageNeutral = shopRespository.findByLocationNearAndLikeStatus(userLocation,ShopLikingStatus.NEUTRAL,  PageRequest.of(page, size));
		
		resultPage.getItems().addAll(shopPageNeutral.getContent());
		resultPage.setTotal(shopPageNeutral.getTotalElements());
	}
	catch (DataAccessException e) {
		 log.error(String.format("getting geosorted shops has encountred a problem  : %s", e.getMessage()));	
	}
		return resultPage;

	}

	public ResultPage<Shop> findByLikingStatusAndLocation(Double longitude, Double latitude, ShopLikingStatus status,
			Integer page, Integer size) {
		ResultPage<Shop> resultPage = new ResultPage<>();
		try {
			GeoJsonPoint userLocation = new GeoJsonPoint(longitude, latitude);
			Page<Shop> shopPage = shopRespository.findByLocationNearAndLikeStatus(userLocation, status,
					PageRequest.of(page, size));
			resultPage.getItems().addAll(shopPage.getContent());
			resultPage.setTotal(shopPage.getTotalElements());
		} catch (DataAccessException e) {
			log.error(String.format("getting prefered shops has encountred a problem  : %s", e.getMessage()));
		}

		return resultPage;

	}

	public Shop updateShop(Shop shop) {
		try {
			shopRespository.save(shop);
		} catch (DataAccessException e) {
			log.error(
					String.format("updating shop  %s has encountred a problem  : %s", shop.getName(), e.getMessage()));
		}
		return shop;
	}

	public Shop getShopById(String id) {
		Optional<Shop> shop = Optional.empty();
		try {
			shop = shopRespository.findById(id);
		} catch (DataAccessException e) {
			log.error(String.format("updating shop  has encountred a problem  : %s", e.getMessage()));
		}
		return shop.get();

	}

}
