package com.controllers;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.common.ResultPage;
import com.entities.shop.Shop;
import com.entities.shop.ShopLikingStatus;
import com.services.ShopService;
import com.utils.EntityHelper;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/shops", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ShopController {
	@Autowired
	private ShopService shopService;

	@GetMapping("/geoSorted")
	public ResultPage<Shop> getShopsSortedByDistance(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "10") Integer size,
			@RequestParam(value = "longitude" ,required = true) Double longitude, @RequestParam(value = "latitude" ,required = true) Double latitude) {
		return shopService.nearByShops(longitude, latitude, page, size);
	}

	@GetMapping("/prefered")
	public ResultPage<Shop> getPreferedShops(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "10") Integer size,
			@RequestParam(value = "longitude", required = true) Double longitude,
			@RequestParam(value = "latitude", required = true) Double latitude)

	{

		return shopService.findByLikingStatusAndLocation(longitude, latitude ,ShopLikingStatus.LIKED, page, size);
	}

	@PutMapping("/likeStatus")
	public Shop updateShop(@RequestParam(value = "likeStatus") String likeStatus,
			@RequestParam(value = "shopId") String shopId) {
		Shop shopDb;
		Shop shop ;
		try {
			shopDb = shopService.getShopById(shopId);
			shop = shopService.getShopById(shopId);
			if ("LIKED".equals(likeStatus) || "DISLIKED".equals(likeStatus) || "NEUTRAL".equals(likeStatus)) {
				shop.setLikeStatus(ShopLikingStatus.valueOf(likeStatus));
				EntityHelper.updateEntityDatesInformation(shop, shopDb);
				
			} else {
				String message = "Wrong like status parameter . the value should be : LIKED ,DISLIKED or NEUTRAL ";
				log.warn(message);
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
			}
		} catch (NoSuchElementException e) {
			String message = "no such shop ! the shop you are trying to update does not exist ";
			log.warn(message);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
		}
		return shopService.updateShop(shop);
	}

}
