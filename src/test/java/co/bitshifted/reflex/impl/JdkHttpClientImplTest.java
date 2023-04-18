/*
 *
 *  * Copyright (c) 2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.reflex.impl;

import co.bitshifted.reflex.exception.HttpStatusException;
import co.bitshifted.reflex.http.*;
import co.bitshifted.reflex.serialize.PlainTextBodySerializer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Set;

import static co.bitshifted.reflex.Reflex.client;
import static co.bitshifted.reflex.Reflex.context;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@WireMockTest(httpPort = 9000)
public class JdkHttpClientImplTest {

    @Test
    void basicGetRequestSuccess() throws Exception {
        stubFor(get("/test/endpoint").willReturn(ok("test body").withHeader(RFXHttpHeaders.CONTENT_TYPE, "text/plain")));
        context().registerBodySerializer(RFXMimeTypes.TEXT_PLAIN, new PlainTextBodySerializer());
        var response = client().sendHttpRequest(new RFXHttpRequest<>(RFXHttpMethod.GET, new URI("http://localhost:9000/test/endpoint"), Set.of(RFXHttpStatus.OK), null, null));
        assertNotNull(response);
        assertNotNull(response.body());
        var responseBody = response.bodyToValue(String.class);
        assertEquals("test body", responseBody);
    }

    @Test
    void basicPostRequestSuccess() throws Exception {
        stubFor(post("/test/post").willReturn(noContent()));
        context().registerBodySerializer(RFXMimeTypes.TEXT_PLAIN, new PlainTextBodySerializer());
        var response = client().sendHttpRequest(new RFXHttpRequest<>(RFXHttpMethod.POST, new URI("http://localhost:9000/test/post"), Set.of(RFXHttpStatus.NO_CONTENT), null, null));
        assertNotNull(response);
        assertEquals(RFXHttpStatus.NO_CONTENT, response.status());
    }

    @Test
    void invalidResponseStatusShouldThrowException() throws Exception{
        stubFor(get("/test/wrong-status").willReturn(badRequest().withHeader(RFXHttpHeaders.CONTENT_TYPE, "text/plain")));
        assertThrows(HttpStatusException.class, () ->
                client().sendHttpRequest(new RFXHttpRequest<>(RFXHttpMethod.GET, new URI("http://localhost:9000/test/wrong-status"), Set.of(RFXHttpStatus.OK), null, null)));
    }
}
