/*
 * Copyright 2005-2022 the original author or authors.
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
package org.openwms.wms.receiving.spi.wms.inventory;

import org.ameba.annotation.Measured;
import org.openwms.core.SpringProfiles;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * A AsyncPackagingUnitApiImpl.
 *
 * @author Heiko Scherrer
 */
@Profile(SpringProfiles.ASYNCHRONOUS_PROFILE)
@Component
class AsyncPackagingUnitApiImpl implements AsyncPackagingUnitApi {

    private final AmqpTemplate amqpTemplate;
    private final String exchangeName;
    private final String routingKey;

    AsyncPackagingUnitApiImpl(
            AmqpTemplate amqpTemplate,
            @Value("${owms.commands.inventory.pu.exchange-name}") String exchangeName,
            @Value("${owms.commands.inventory.pu.routing-key}") String routingKey) {
        this.amqpTemplate = amqpTemplate;
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public void create(CreatePackagingUnitCommand command) {
        amqpTemplate.convertAndSend(exchangeName, routingKey, command);
    }
}
