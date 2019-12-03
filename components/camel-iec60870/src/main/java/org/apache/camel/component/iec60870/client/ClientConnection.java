/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.iec60870.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.Objects;

import io.netty.channel.ChannelHandlerContext;

import org.apache.camel.component.iec60870.ConnectionId;
import org.apache.camel.component.iec60870.DiscardAckModule;
import org.apache.camel.component.iec60870.ObjectAddress;

import org.eclipse.neoscada.protocol.iec60870.asdu.types.ASDUAddress;
import org.eclipse.neoscada.protocol.iec60870.asdu.types.InformationObjectAddress;
import org.eclipse.neoscada.protocol.iec60870.asdu.types.QualifierOfInterrogation;
import org.eclipse.neoscada.protocol.iec60870.asdu.types.Value;
import org.eclipse.neoscada.protocol.iec60870.client.AutoConnectClient;
import org.eclipse.neoscada.protocol.iec60870.client.AutoConnectClient.ModulesFactory;
import org.eclipse.neoscada.protocol.iec60870.client.AutoConnectClient.State;
import org.eclipse.neoscada.protocol.iec60870.client.AutoConnectClient.StateListener;
import org.eclipse.neoscada.protocol.iec60870.client.data.AbstractDataProcessor;
import org.eclipse.neoscada.protocol.iec60870.client.data.DataHandler;
import org.eclipse.neoscada.protocol.iec60870.client.data.DataModule;
import org.eclipse.neoscada.protocol.iec60870.client.data.DataModuleContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientConnection {

    private static final Logger LOG = LoggerFactory.getLogger(ClientConnection.class);

    @FunctionalInterface
    public interface ValueListener {
        void update(ObjectAddress address, Value<?> value);
    }

    private final StateListener externalStateListener;

    private final DataHandler dataHandler = new AbstractDataProcessor() {

        /**
         * Called when the connection was established
         */
        @Override
        public void activated(final DataModuleContext dataModuleContext, final ChannelHandlerContext ctx) {
            dataModuleContext.requestStartData();
            dataModuleContext.startInterrogation(ASDUAddress.BROADCAST, QualifierOfInterrogation.GLOBAL);
        }

        /**
         * Called when the start data was accepted
         */
        @Override
        public void started() {
        }

        /**
         * Called when the connection broke
         */
        @Override
        public void disconnected() {
        }

        @Override
        protected void fireEntry(final ASDUAddress asduAddress, final InformationObjectAddress address, final Value<?> value) {
            ClientConnection.this.handleData(ObjectAddress.valueOf(asduAddress, address), value);
        }

        @Override
        public void requestStartData() {
        }
    };

    private ValueListener listener;

    private final ConnectionId connectionId;
    private Iterator<Map.Entry<String, Integer>> serverIterator;
    private final ClientOptions options;

    private AutoConnectClient client;

    public ClientConnection(final ConnectionId connectionId, final ClientOptions options, final StateListener stateListener) {
        this.connectionId = connectionId;
        this.options = options;
        this.externalStateListener = stateListener;
    }

    public void start() {
        final DataModule dataModule = new DataModule(this.dataHandler, this.options.getDataModuleOptions());
        final ModulesFactory factory = () -> Arrays.asList(dataModule, new DiscardAckModule());

        // Either start, restart at the beginning or take the next element
        if (serverIterator == null || !serverIterator.hasNext()) {
            this.serverIterator = this.connectionId.getServers().entrySet().iterator();
        }

        final Map.Entry<String, Integer> server = serverIterator.next();

        LOG.info("Connect to '{}'.", server);

        final CountDownLatch latch = new CountDownLatch(1);

        StateListener multiServerStateListener = (final State state, final Throwable e) -> {
            if (state.equals(AutoConnectClient.State.DISCONNECTED)) {
                LOG.info("Close disconnected client. Restart in 10 s.");
                client.close();

                try {
                    Thread.sleep(this.options.getConnectionTimeout());
                } catch (InterruptedException ex) {
                }

                start();
            }

            if (state == State.CONNECTED) {
                latch.countDown();
            }
        };

        final List<StateListener> stateListeners = new ArrayList<>();
        stateListeners.add(multiServerStateListener);

        if (Objects.nonNull(externalStateListener)) {
          stateListeners.add(externalStateListener);
        }

        this.client = new AutoConnectClient(server.getKey(), server.getValue(), this.options.getProtocolOptions(), factory, stateListeners);
        try {
            latch.await(this.options.getConnectionTimeout(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            // ignore
        }
    }

    public void stop() throws Exception {
        this.client.close();
    }

    protected synchronized void handleData(final ObjectAddress address, final Value<?> value) {
        if (listener != null) {
            listener.update(address, value);
        }
    }

    public synchronized void setListener(final ValueListener listener) {
        this.listener = listener;
    }

    public boolean executeCommand(final Object command) {
        return this.client.writeCommand(command);
    }
}
