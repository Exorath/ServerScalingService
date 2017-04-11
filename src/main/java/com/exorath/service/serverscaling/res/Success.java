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

/**
 * Created by toonsev on 4/1/2017.
 */
public class Success {
    private boolean success;
    private Integer code;
    private String error;

    public Success(boolean success) {
        this.success = success;
    }

    public Success(boolean success, Integer code, String error) {
        this.success = success;
        this.code = code;
        this.error = error;
    }

    public Success(Integer code, String error) {
        this.code = code;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public Integer getCode() {
        return code;
    }

    public String getError() {
        return error;
    }
}
