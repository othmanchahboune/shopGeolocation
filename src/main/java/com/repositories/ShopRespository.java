package com.repositories;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.entities.shop.Shop;
import com.entities.shop.ShopLikingStatus;
public interface ShopRespository extends MongoRepository<Shop, String> {

	Page<Shop> findByLocationNear(GeoJsonPoint userLocation, Pageable pageable);
	Page<Shop> findByLocationNearAndLikeStatus(GeoJsonPoint userLocation,ShopLikingStatus status, Pageable pageable);
    Page<Shop> findByLikeStatusAndLikeStatusDateLessThan(ShopLikingStatus status,LocalDateTime date, Pageable pageable);
}
