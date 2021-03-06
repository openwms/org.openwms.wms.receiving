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

import org.openwms.core.event.RootApplicationEvent;

/**
 * A ProductEvent.
 *
 * @author Heiko Scherrer
 */
public class ProductEvent extends RootApplicationEvent {

    public static enum TYPE {
        CREATED, UPDATED, DELETED
    }

    private TYPE type;

    /**
     * Create a new RootApplicationEvent.
     *
     * @param source The event sender, never {@literal null}
     */
    public ProductEvent(Object source) {
        super(source);
    }

    /**
     * Create a new RootApplicationEvent.
     *
     * @param source The event sender, never {@literal null}
     */
    public ProductEvent(Object source, TYPE type) {
        super(source);
        this.type = type;
    }

    public TYPE getType() {
        return type;
    }
}
