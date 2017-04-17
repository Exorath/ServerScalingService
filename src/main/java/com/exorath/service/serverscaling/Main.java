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

import com.exorath.service.commons.mongoProvider.MongoProvider;
import com.exorath.service.commons.portProvider.PortProvider;
import com.exorath.service.commons.tableNameProvider.TableNameProvider;
import com.exorath.service.connector.api.ConnectorServiceAPI;
import com.exorath.service.serverscaling.service.ConnectorServiceProvider;
import com.exorath.service.serverscaling.service.KubernetesScaler;
import com.exorath.service.serverscaling.service.MongoService;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by toonsev on 4/11/2017.
 */
public class Main {
    private Service service;
    private KubernetesScaler kubernetesScaler;
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(2);
    private static final Logger LOG = LoggerFactory.getLogger(com.exorath.service.connector.Main.class);

    public Main() {
        this.service = new MongoService(MongoProvider.getEnvironmentMongoProvider().getClient(), TableNameProvider.getEnvironmentTableNameProvider("DB_NAME").getTableName());
        this.kubernetesScaler = new KubernetesScaler(service, getKubernetesClient(), getConnectorServiceProvider());
        scheduler.scheduleAtFixedRate(kubernetesScaler, 0, 10, TimeUnit.SECONDS);
        LOG.info("Service " + this.service.getClass() + " instantiated");
        Transport.setup(service, PortProvider.getEnvironmentPortProvider());
        LOG.info("HTTP Transport initiated");
    }

    private ConnectorServiceProvider getConnectorServiceProvider() {
        return new ConnectorServiceProvider(new ConnectorServiceAPI(TableNameProvider.getEnvironmentTableNameProvider("CONNECTOR_SERVICE_ADDRESS").toString()));
    }

    private KubernetesClient getKubernetesClient() {
        return new DefaultKubernetesClient(TableNameProvider.getEnvironmentTableNameProvider("KUBERNETES_ADDRESS").getTableName());
    }

    public static void main(String[] args) {
        new Main();
    }
}
