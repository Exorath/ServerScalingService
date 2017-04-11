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

import com.exorath.service.serverscaling.res.Bungee;
import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.api.model.ServicePortBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

/**
 * Created by toonsev on 4/11/2017.
 */
public class KubernetesScaler implements Runnable {
    private com.exorath.service.serverscaling.Service service;
    private KubernetesClient kubernetesClient;

    private KubernetesScaler(com.exorath.service.serverscaling.Service service, KubernetesClient kubernetesClient) {
        this.service = service;
        this.kubernetesClient = kubernetesClient;
    }

    @Override
    public void run() {
        scaleBungee();
        for (String game : service.getGameTypes())
            scaleGame(game);
    }

    private void scaleGame(String game) {

    }

    private void scaleBungee() {
        Bungee bungee = service.getBungee();
        if (bungee == null) {
            System.out.println("No bungee registered");
            return;
        }
        kubernetesClient.services().createOrReplace(getService(bungee));
        //Replace bungee instances...
    }

    private Service getService(Bungee bungee) {
        return new ServiceBuilder()
                .editMetadata().addToLabels("app", "server-bc").addToLabels("type", "bc").withNamespace("servers").withName("server-bc").and()
                .editSpec().addToSelector("app", "server-bc").addNewPortLike(new ServicePortBuilder().withPort(25565).withNewTargetPort(25565).build()).and()
                .withType("NodePort").and().build();
    }
}
