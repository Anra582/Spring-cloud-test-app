package com.anradev.licenseservice.repository;

public interface DataCacheRepository<T> {
    boolean add(String collection, String hkey, T object);
    boolean delete(String collection, String hkey);
    T find(String collection, String hkey, Class<T> tClass);
    Boolean isAvailable();
}
