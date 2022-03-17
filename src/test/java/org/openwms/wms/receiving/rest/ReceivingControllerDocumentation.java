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
package org.openwms.wms.receiving.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openwms.core.units.api.Piece;
import org.openwms.wms.receiving.AbstractTestBase;
import org.openwms.wms.receiving.ReceivingApplicationTest;
import org.openwms.wms.receiving.ReceivingMessages;
import org.openwms.wms.receiving.api.CaptureRequestVO;
import org.openwms.wms.receiving.api.ProductVO;
import org.openwms.wms.receiving.api.QuantityCaptureRequestVO;
import org.openwms.wms.receiving.api.ReceivingOrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.openwms.wms.receiving.TestData.ORDER1_PKEY;
import static org.openwms.wms.receiving.api.ReceivingOrderVO.MEDIA_TYPE;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * A ReceivingControllerDocumentation.
 *
 * @author Heiko Scherrer
 */
@Sql("classpath:import-TEST.sql")
@ReceivingApplicationTest
@TestPropertySource(properties = "owms.receiving.unexpected-receipts-allowed=false")
class ReceivingControllerDocumentation extends AbstractTestBase {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper om;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(documentationConfiguration(restDocumentation)).build();
    }

    @Test void shall_return_index() throws Exception {
        mockMvc
                .perform(
                        get("/v1/receiving-orders/index")
                )
                .andExpect(status().isOk())
                .andDo(document("get-order-index", preprocessResponse(prettyPrint())))
        ;
    }

    @Transactional
    @Rollback
    @Test void shall_complete_order() throws Exception {
        mockMvc
                .perform(
                        post("/v1/receiving-orders/"+ORDER1_PKEY+"/complete")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("state", is("COMPLETED")))
                .andExpect(jsonPath("positions[0].state", is("COMPLETED")))
                .andExpect(jsonPath("positions[1].state", is("COMPLETED")))
                .andDo(document("order-complete", preprocessResponse(prettyPrint())))
        ;
    }

    @Transactional
    @Rollback
    @Test void shall_cancel_order() throws Exception {
        var toLocation = createOrder("4714");
        var value = new ReceivingOrderVO("4714");
        value.setState("CANCELED");
        mockMvc
                .perform(
                        patch(toLocation)
                                .contentType(MEDIA_TYPE)
                                .content(om.writeValueAsString(value))
                )
                .andExpect(status().isOk())
                .andDo(document("order-cancel", preprocessResponse(prettyPrint())))
        ;
    }

    @Transactional
    @Rollback
    @Test void shall_cancel_cancelled_order() throws Exception {
        var toLocation = createOrder("4715");
        var value = new ReceivingOrderVO("4715");
        value.setState("CANCELED");
        mockMvc
                .perform(
                        patch(toLocation)
                                .contentType(MEDIA_TYPE)
                                .content(om.writeValueAsString(value))
                )
                .andExpect(status().isOk())
        ;
        mockMvc
                .perform(
                        patch(toLocation)
                                .contentType(MEDIA_TYPE)
                                .content(om.writeValueAsString(value))
                )
                .andExpect(status().isGone())
                .andExpect(jsonPath("messageKey", CoreMatchers.is(ReceivingMessages.RO_ALREADY_IN_STATE)))
                .andDo(document("order-cancel-cancelled", preprocessResponse(prettyPrint())))
        ;
    }

    @Test void shall_NOT_cancel_order() throws Exception {
        var toLocation = createOrder("4716");
        var value = new ReceivingOrderVO("4716");
        value.setState("CANCELED");
        mockMvc
                .perform(
                        patch(toLocation)
                                .contentType(MEDIA_TYPE)
                                .content(om.writeValueAsString(value))
                )
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("messageKey", is(ReceivingMessages.RO_CANCELLATION_DENIED)))
                .andDo(document("order-cancel-403", preprocessResponse(prettyPrint())))
        ;
    }

    @Transactional
    @Rollback
    @Test void shall_capture_order() throws Exception {
        var vo = new QuantityCaptureRequestVO();
        vo.setTransportUnitId("4711");
        vo.setLoadUnitLabel("1");
        vo.setLoadUnitType("EURO");
        vo.setQuantityReceived(Piece.of(1));
        vo.setProduct(new ProductVO("C1"));
        mockMvc
                .perform(
                        post("/v1/receiving-orders/{pKey}/capture", ORDER1_PKEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(new CaptureRequestVO[]{vo}))
                )
                .andDo(document("order-capture", preprocessResponse(prettyPrint())))
                .andExpect(status().isOk())
        ;
    }

    @Transactional
    @Rollback
    @Test void shall_capture_order_INSUFFISIENT() throws Exception {
        var vo = new QuantityCaptureRequestVO();
        vo.setTransportUnitId("4711");
        vo.setLoadUnitLabel("1");
        vo.setLoadUnitType("EURO");
        vo.setQuantityReceived(Piece.of(2));
        vo.setProduct(new ProductVO("C1"));
        mockMvc
                .perform(
                        post("/v1/receiving-orders/{pKey}/capture", ORDER1_PKEY)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(om.writeValueAsString(vo))
                )
                .andDo(document("order-capture-500", preprocessResponse(prettyPrint())))
                .andExpect(status().isInternalServerError())
        ;
    }


    public String createOrder(String orderId) throws Exception {
        var result = mockMvc
                .perform(
                        post("/v1/receiving-orders")
                                .contentType(MEDIA_TYPE)
                                .content(om.writeValueAsString(new ReceivingOrderVO(orderId)))
                )
                .andExpect(status().isCreated())
                .andReturn();

        return (String) result.getResponse().getHeaderValue(LOCATION);
    }
}
