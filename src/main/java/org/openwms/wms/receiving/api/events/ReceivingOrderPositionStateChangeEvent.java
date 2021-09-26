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
package org.openwms.wms.receiving.api.events;

import org.openwms.core.event.RootApplicationEvent;
import org.openwms.wms.order.OrderState;
import org.openwms.wms.receiving.impl.BaseReceivingOrderPosition;

import java.io.Serializable;

/**
 * A ReceivingOrderPositionStateChangeEvent.
 * 
 * @author Heiko Scherrer
 */
public class ReceivingOrderPositionStateChangeEvent<T extends BaseReceivingOrderPosition> extends RootApplicationEvent implements Serializable {

    public OrderState state;

    public ReceivingOrderPositionStateChangeEvent(T source, OrderState state) {
        super(source);
        this.state = state;
    }

    @Override
    public T getSource() {
        return (T) super.getSource();
    }

    public OrderState getState() {
        return state;
    }
}