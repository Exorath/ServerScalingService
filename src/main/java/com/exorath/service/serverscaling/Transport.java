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

package com.exorath.service.serverscaling;

import com.exorath.service.commons.portProvider.PortProvider;
import com.exorath.service.serverscaling.res.Bungee;
import com.exorath.service.serverscaling.res.GameType;
import com.exorath.service.serverscaling.res.Success;
import com.google.gson.Gson;
import spark.Route;

import static spark.Spark.port;
import static spark.Spark.post;

/**
 * Created by toonsev on 4/11/2017.
 */
public class Transport {
    private static final Gson GSON = new Gson();

    public static void setup(Service service, PortProvider portProvider) {
        port(portProvider.getPort());

        post("/games/register", getRegisterGameRoute(service), GSON::toJson);
        post("/bungee", getRegisterBungeeRoute(service), GSON::toJson);
    }


    private static Route getRegisterGameRoute(Service service) {
        return (req, res) -> {
            try {
               return service.registerGameType(GSON.fromJson(req.body(), GameType.class));
            }catch(Exception e){
                e.printStackTrace();
                return new Success(-1, e.getMessage());
            }
        };
    }


    private static Route getRegisterBungeeRoute(Service service) {
        return (req, res) -> {
            try {
                return service.registerBungee(GSON.fromJson(req.body(), Bungee.class));
            }catch(Exception e){
                e.printStackTrace();
                return new Success(-1, e.getMessage());
            }
        };
    }

}
