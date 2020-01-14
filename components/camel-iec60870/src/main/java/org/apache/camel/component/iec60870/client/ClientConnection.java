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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelHandlerContext;
import org.apache.camel.component.iec60870.DiscardAckModule;
import org.apache.camel.component.iec60870.ObjectAddress;
import org.eclipse.oneofour.asdu.types.ASDUAddress;
import org.eclipse.oneofour.asdu.types.InformationObjectAddress;
import org.eclipse.oneofour.asdu.types.QualifierOfInterrogation;
import org.eclipse.oneofour.asdu.types.Value;
import org.eclipse.oneofour.client.AutoConnectClient;
import org.eclipse.oneofour.client.AutoConnectClient.ModulesFactory;
import org.eclipse.oneofour.client.AutoConnectClient.State;
import org.eclipse.oneofour.client.AutoConnectClient.StateListener;
import org.eclipse.oneofour.client.data.AbstractDataProcessor;
import org.eclipse.oneofour.client.data.DataHandler;
import org.eclipse.oneofour.client.data.DataModule;
import org.eclipse.oneofour.client.data.DataModuleContext;

public class ClientConnection {

    @FunctionalInterface
    public interface ValueListener {
        void update(ObjectAddress address, Value<?> value);
    }

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

    private final Map<ObjectAddress, Value<?>> lastValue = new HashMap<>();
    private final Map<ObjectAddress, ValueListener> listeners = new HashMap<>();

    private final String host;
    private final int port;
    private final ClientOptions options;

    private AutoConnectClient client;

    public ClientConnection(final String host, final int port, final ClientOptions options) {
        this.host = host;
        this.port = port;
        this.options = options;
    }

    public void start() {
        final DataModule dataModule = new DataModule(this.dataHandler, this.options.getDataModuleOptions());
        final ModulesFactory factory = () -> Arrays.asList(dataModule, new DiscardAckModule());
        final CountDownLatch latch = new CountDownLatch(1);
        this.client = new AutoConnectClient(this.host, this.port, this.options.getProtocolOptions(), factory, new StateListener() {
            @Override
            public void stateChanged(final State state, final Throwable e) {
                if (state == State.CONNECTED) {
                    latch.countDown();
                }
            }
        });
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
        this.lastValue.put(address, value);
        final ValueListener listener = this.listeners.get(address);
        if (listener != null) {
            listener.update(address, value);
        }
    }

    public synchronized void setListener(final ObjectAddress address, final ValueListener listener) {
        if (listener != null) {
            this.listeners.put(address, listener);
            final Value<?> last = this.lastValue.get(address);
            if (last != null) {
                listener.update(address, last);
            }
        } else {
            this.listeners.remove(address);
        }
    }

    public boolean executeCommand(final Object command) {
        return this.client.writeCommand(command);
    }
}
