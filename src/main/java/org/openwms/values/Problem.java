/*
 * Copyright 2005-2020 the original author or authors.
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
package org.openwms.values;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * A Problem is used to signal an occurred failure.
 *
 * @author Heiko Scherrer
 * @GlossaryTerm
 */
@Embeddable
public class Problem implements Serializable {

    /** Timestamp when the {@code Problem} occurred. */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "C_PROBLEM_OCCURRED")
    private Date occurred;

    /** Message number of the {@code Problem}. */
    @Column(name = "C_PROBLEM_MESSAGE_NO")
    private int messageNo;

    /** Message text about the {@code Problem}. */
    @Column(name = "C_PROBLEM_MESSAGE")
    private String message;

    /* ----------------------------- methods ------------------- */

    /** Creates a new {@code Problem} instance. */
    public Problem() {
        this.occurred = new Date();
    }

    /**
     * Create a new <code>Problem</code> instance with a message text.
     *
     * @param message text as String
     */
    public Problem(String message) {
        this();
        this.message = message;
    }

    /**
     * Create a new <code>Problem</code> instance with a message text and a message number.
     *
     * @param message   text as String
     * @param messageNo message number
     */
    public Problem(String message, int messageNo) {
        this();
        this.message = message;
        this.messageNo = messageNo;
    }

    /**
     * Return the Date when the <code>Problem</code> has occurred.
     *
     * @return Date when occurred.
     */
    public Date getOccurred() {
        return new Date(occurred.getTime());
    }

    /**
     * Set the Date when the <code>Problem</code> occurred.
     *
     * @param occurred The Date to set.
     */
    public void setOccurred(Date occurred) {
        this.occurred = new Date(occurred.getTime());
    }

    /**
     * Get the messageNo.
     *
     * @return The messageNo.
     */
    public int getMessageNo() {
        return messageNo;
    }

    /**
     * Set the messageNo.
     *
     * @param messageNo The messageNo to set.
     */
    public void setMessageNo(int messageNo) {
        this.messageNo = messageNo;
    }

    /**
     * Get the message.
     *
     * @return The message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the message.
     *
     * @param message The message to set.
     */
    public void setMessage(String message) {
        this.message = message;
    }
}