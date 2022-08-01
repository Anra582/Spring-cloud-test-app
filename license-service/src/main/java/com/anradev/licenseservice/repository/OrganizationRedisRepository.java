package com.anradev.licenseservice.repository;

import com.anradev.licenseservice.service.LicenseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.TimeZone;

@Repository
@Qualifier("OrganizationRedisRepository")
public class OrganizationRedisRepository<T> implements DataCacheRepository<T> {

    private static final Logger logger = LoggerFactory.getLogger(LicenseService.class);

    @Autowired
    RedisTemplate redisTemplate;

    private static final ObjectMapper OBJECT_MAPPER;
    private static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone("UTC");

    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.setTimeZone(DEFAULT_TIMEZONE);
    }


    @Override
    public boolean add(String collection, String hkey, T object) {
        try {
            String jsonObject = OBJECT_MAPPER.writeValueAsString(object);
            redisTemplate.opsForHash().put(collection, hkey, jsonObject);
            return true;
        } catch (Exception e) {
            logger.error("Unable to add object of key {} to cache collection '{}' : {}",
                    hkey, collection , e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(String collection, String hkey) {
        try {
            redisTemplate.opsForHash().delete(collection, hkey);
            return true;
        } catch (Exception e) {
            logger.error("Unable to delete entry {} from cache collection '{}' : {}",
                    hkey, collection , e.getMessage());
            return false;
        }
    }

    @Override
    public T find(String collection, String hkey, Class<T> tClass) {
        try {
            String jsonObject = String.valueOf(redisTemplate.opsForHash().get(collection, hkey));
            return OBJECT_MAPPER.readValue(jsonObject, tClass);
        } catch (Exception e) {
            logger.error("Unable to find entry '{}' in cache collection '{}' : {}" ,
                    hkey, collection, e.getMessage());
            return null;
        }
    }

    @Override
    public Boolean isAvailable() {
        try {
            return redisTemplate.getConnectionFactory().getConnection().ping() != null;
        } catch (Exception e) {
            logger.warn("Redis server is not available at this moment. Try later");
            return false;
        }
    }
}
