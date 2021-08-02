/*
 * Copyright 2005-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openwms.wms.receiving.inventory.impl;

import org.openwms.core.SpringProfiles;
import org.openwms.wms.inventory.api.AsyncPackagingUnitApi;
import org.openwms.wms.inventory.api.CreatePackagingUnitCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * A NoOpAsyncPackagingUnitApiImpl.
 *
 * @author Heiko Scherrer
 */
@Profile("!" + SpringProfiles.ASYNCHRONOUS_PROFILE)
@Component
class NoOpAsyncPackagingUnitApiImpl implements AsyncPackagingUnitApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoOpAsyncPackagingUnitApiImpl.class);

    @Override
    public void create(CreatePackagingUnitCommand command) {
        LOGGER.warn("Command to send [{}]", command);
    }
}
