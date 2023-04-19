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

import co.bitshifted.reflex.RFXRedirectPolicy;
import co.bitshifted.reflex.ReflexClient;
import co.bitshifted.reflex.ReflexClientConfiguration;
import co.bitshifted.reflex.impl.jdk11.JdkReflexClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class HttpClientLoaderTest {

    @Test
    void loadDefaultHttpClientSuccess() {
       var result = HttpClientLoader.loadDefaultClient();
        assertTrue(result.isPresent());
        var client = result.get();
        assertTrue(client instanceof JdkReflexClient);
    }

    @Test
    void loadDefaultClientWithConfigSuccess() {
        var config =new  ReflexClientConfiguration(Duration.of(5, ChronoUnit.SECONDS), RFXRedirectPolicy.NORMAL);
        var result = HttpClientLoader.loadDefaultClient(config);
        assertTrue(result.isPresent());
        var client = result.get();
        assertTrue(client instanceof JdkReflexClient);
    }
}