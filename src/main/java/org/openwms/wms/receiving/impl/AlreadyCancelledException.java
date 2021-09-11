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
package org.openwms.wms.receiving.impl;

import org.ameba.exception.BusinessRuntimeException;
import org.ameba.i18n.Translator;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;

/**
 * A AlreadyCancelledException.
 *
 * @author Heiko Scherrer
 */
@ResponseStatus(HttpStatus.GONE)
public class AlreadyCancelledException extends BusinessRuntimeException {

    public AlreadyCancelledException(Translator translator, String messageKey, Serializable[] data, Object... param) {
        super(translator, messageKey, data, param);
    }
}
