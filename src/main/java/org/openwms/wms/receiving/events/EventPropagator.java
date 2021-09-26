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
package org.openwms.wms.receiving.events;

import org.ameba.annotation.Measured;
import org.ameba.app.SpringProfiles;
import org.ameba.mapping.BeanMapper;
import org.openwms.wms.receiving.api.events.ReceivingOrderMO;
import org.openwms.wms.receiving.api.events.ReceivingOrderPositionMO;
import org.openwms.wms.receiving.api.events.ReceivingOrderPositionStateChangeEvent;
import org.openwms.wms.receiving.api.events.ReceivingOrderStateChangeEvent;
import org.openwms.wms.receiving.impl.BaseReceivingOrderPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * A EventPropagator is a Spring managed bean, actived in profile ASYNCHRONOUS that propagates internal events to the outer world via AMQP.
 *
 * @author Heiko Scherrer
 */
@Profile(SpringProfiles.ASYNCHRONOUS_PROFILE)
@Component
public class EventPropagator {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventPropagator.class);
    private final AmqpTemplate amqpTemplate;
    private final BeanMapper mapper;
    private final String receivingExchangeName;

    public EventPropagator(AmqpTemplate amqpTemplate,
            BeanMapper mapper, @Value("${owms.events.receiving.exchange-name}") String receivingExchangeName) {
        this.amqpTemplate = amqpTemplate;
        this.mapper = mapper;
        this.receivingExchangeName = receivingExchangeName;
    }

    @Measured
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onEvent(ReceivingOrderStateChangeEvent event) {
        ReceivingOrderMO mo = mapper.map(event.getSource(), ReceivingOrderMO.class);
        switch(event.getState()) {
            case COMPLETED:
                LOGGER.debug("ReceivingOrder [{}] with all positions completed", event.getSource().getPersistentKey());
                LOGGER.debug("Sending ReceivingOrderMO: [{}]", mo);
                amqpTemplate.convertAndSend(receivingExchangeName, "receiving.event.ro.completed", mo);
                break;
            case CANCELED:
                LOGGER.debug("ReceivingOrder [{}] has been cancelled", event.getSource().getPersistentKey());
                LOGGER.debug("Sending ReceivingOrderMO: [{}]", mo);
                amqpTemplate.convertAndSend(receivingExchangeName, "receiving.event.ro.cancelled", mo);
                break;
            default:
                LOGGER.warn("EventType [{}] not supported", event.getState());
        }
    }

    @Measured
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public <T extends BaseReceivingOrderPosition> void onEvent(ReceivingOrderPositionStateChangeEvent<T> event) {
        ReceivingOrderPositionMO mo = mapper.map(event.getSource(), ReceivingOrderPositionMO.class);
        switch(event.getState()) {
            case COMPLETED:
                LOGGER.debug("ReceivingOrderPosition [{}]/[{}] completed",
                        event.getSource().getOrder().getOrderId(), event.getSource().getPosNo());
                LOGGER.debug("Sending ReceivingOrderPositionMO: [{}]", mo);
                amqpTemplate.convertAndSend(receivingExchangeName, "receiving.event.rop.completed", mo);
                break;
            default:
                LOGGER.warn("EventType [{}] not supported", event.getState());
        }
    }
}