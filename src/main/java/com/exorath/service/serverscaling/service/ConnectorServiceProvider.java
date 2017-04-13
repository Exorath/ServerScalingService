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

import com.exorath.service.connector.api.ConnectorServiceAPI;
import com.exorath.service.connector.res.Filter;
import com.exorath.service.connector.res.ServerInfo;
import com.exorath.service.serverscaling.res.GameType;

/**
 * Created by toonsev on 4/13/2017.
 */
public class ConnectorServiceProvider {
    private ConnectorServiceAPI connectorServiceAPI;

    public ConnectorServiceProvider(ConnectorServiceAPI connectorServiceAPI) {
        this.connectorServiceAPI = connectorServiceAPI;
    }

    public ServerInfo getInfo(GameType gameType){
        return connectorServiceAPI.getServerInfo(new Filter(gameType.getName()), System.currentTimeMillis());
    }
}
