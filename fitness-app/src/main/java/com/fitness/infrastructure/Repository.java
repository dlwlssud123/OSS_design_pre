package com.fitness.infrastructure;

public interface Repository {
    void save(Object data);
    Object load();
}