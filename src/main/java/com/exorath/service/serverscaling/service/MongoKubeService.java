/*
 * Copyright 2017 Exorath
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.exorath.service.serverscaling.service;

import com.exorath.service.serverscaling.Service;
import com.exorath.service.serverscaling.res.Bungee;
import com.exorath.service.serverscaling.res.GameType;
import com.exorath.service.serverscaling.res.Success;
import com.mongodb.MongoClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.Morphia;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toonsev on 4/11/2017.
 */
public class MongoKubeService implements Service {
    private static final Morphia morphia = new Morphia();

    static {
        morphia.mapPackage("com.exorath.service.serverscaling.res");
    }

    private KubernetesClient kubernetesClient;
    private Datastore datastore;

    public MongoKubeService(KubernetesClient kubernetesClient, MongoClient mongoClient, String dbName) {
        this.kubernetesClient = kubernetesClient;
        this.datastore = morphia.createDatastore(mongoClient, dbName);
    }

    public Success registerGameType(GameType gameType) {
        Key<GameType> key = datastore.save(gameType);
        if (key == null || key.getId() == null)
            return new Success(-1, "no id key was returned");
        if (!key.getId().equals(gameType.getName()))
            return new Success(-1, "Wrong id was returned");
        return new Success(true);
    }

    public Success registerBungee(Bungee bungee) {
        Key<Bungee> key = datastore.save(bungee);
        if (key == null || key.getId() == null)
            return new Success(-1, "no id key was returned");
        if (!key.getId().equals(bungee.getName()))
            return new Success(-1, "Wrong id was  returned");
        return new Success(true);
    }

    @Override
    public Bungee getBungee() {
        return datastore.find(Bungee.class).get();
    }

    @Override
    public GameType getGameType(String name) {
        return datastore.find(GameType.class).field("_id").equal(name).get();
    }

    @Override
    public String[] getGameTypes() {
        List<String> gameTypes = new ArrayList<>();
        datastore.find(GameType.class).project("_id", true).forEach(gameType -> gameTypes.add(gameType.getName()));
        return gameTypes.toArray(new String[gameTypes.size()]);
    }
}
