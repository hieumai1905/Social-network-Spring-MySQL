package com.socialnetwork.socialnetworkjavaspring.services;

import java.util.Optional;

public interface IGeneralService<T, K> {
    Optional<T> save(T object);

    Optional<T> delete(T object);
}
