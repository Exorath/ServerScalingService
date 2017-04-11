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

package com.exorath.service.serverscaling.res;

import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

import java.util.HashMap;
import java.util.List;

/**
 * Created by toonsev on 4/11/2017.
 */
public class GameType {
    @Id
    private String name;
    @Property("image")
    private String image;
    @Property("grace")
    private int terminationGracePeriodSeconds = 60;
    @Property("env")
    private HashMap<String, String> env;
    @Property("labels")
    private List<String> labels;

    public GameType() {}

    public GameType(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public GameType setName(String name) {
        this.name = name;
        return this;
    }

    public String getImage() {
        return image;
    }

    public GameType setImage(String image) {
        this.image = image;
        return this;
    }

    public int getTerminationGracePeriodSeconds() {
        return terminationGracePeriodSeconds;
    }

    public GameType setTerminationGracePeriodSeconds(int terminationGracePeriodSeconds) {
        this.terminationGracePeriodSeconds = terminationGracePeriodSeconds;
        return this;
    }

    public HashMap<String, String> getEnv() {
        return env;
    }

    public GameType setEnv(HashMap<String, String> env) {
        this.env = env;
        return this;
    }

    public List<String> getLabels() {
        return labels;
    }

    public GameType setLabels(List<String> labels) {
        this.labels = labels;
        return this;
    }
}
