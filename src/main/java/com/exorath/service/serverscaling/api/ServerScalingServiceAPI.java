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

package com.exorath.service.serverscaling.api;

import com.exorath.service.serverscaling.Service;
import com.exorath.service.serverscaling.res.Bungee;
import com.exorath.service.serverscaling.res.GameType;
import com.exorath.service.serverscaling.res.Success;
import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;

/**
 * Created by toonsev on 8/16/2017.
 */
public class ServerScalingServiceAPI implements Service{
    private static final Gson GSON = new Gson();
    private String address;

    public ServerScalingServiceAPI(String address) {
        this.address = address;
    }

    @Override
    public Success registerGameType(GameType gameType) {
        try {
            String res = Unirest.post(url("/games/register"))
                    .body(GSON.toJson(gameType)).asString().getBody();
            return GSON.fromJson(res, Success.class);
        }catch (Exception e){
            e.printStackTrace();
            return new Success(-1, e.getMessage());
        }
    }

    @Deprecated
    @Override
    public Success registerBungee(Bungee bungee) {
        return null;
    }

    @Deprecated
    @Override
    public Bungee getBungee() {
        return null;
    }

    @Deprecated
    @Override
    public GameType getGameType(String name) {
        return null;
    }

    @Deprecated
    @Override
    public String[] getGameTypes() {
        return new String[0];
    }

    private String url(String endpoint) {
        return address + endpoint;
    }
}
