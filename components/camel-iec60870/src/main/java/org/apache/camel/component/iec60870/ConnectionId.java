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
package org.apache.camel.component.iec60870;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ConnectionId {

    private final String serverUrls;
    private final String connectionId;
    private final Map<String, Integer> servers = new HashMap<>();

    public ConnectionId(final String serverUrls, final String connectionId) {
        Objects.requireNonNull(serverUrls);

        this.serverUrls = serverUrls;
        this.connectionId = connectionId;

        parseServerUrls();
    }

    public String getServerUrls() {
        return this.serverUrls;
    }

    public String getConnectionId() {
        return this.connectionId;
    }

    public Map<String, Integer> getServers() {
        return Collections.unmodifiableMap(this.servers);
    }

    final void parseServerUrls() {
        String[] serverUrlTokens = serverUrls.split(",");

        if (serverUrlTokens.length == 0) {
            throw new IllegalArgumentException(String.format("At least one connection is mandatory in '%s'.", serverUrls));
        }

        for (String serverUrl : serverUrlTokens) {
            String[] hostnameIp = serverUrl.split(":");

            if (hostnameIp.length != 2) {
                throw new IllegalArgumentException(String.format("Wrong format of hostname:ip '%s'.", serverUrl));
            }

            String host = hostnameIp[0];
            Integer port = Integer.parseInt(hostnameIp[1]);

            if (port <= 0) {
                throw new IllegalArgumentException("Port must be greater than 0");
            }

            this.servers.put(host, port);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.connectionId == null ? 0 : this.connectionId.hashCode());
        result = prime * result + (this.serverUrls == null ? 0 : this.serverUrls.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ConnectionId other = (ConnectionId)obj;
        if (this.connectionId == null) {
            if (other.connectionId != null) {
                return false;
            }
        } else if (!this.connectionId.equals(other.connectionId)) {
            return false;
        }
        if (this.serverUrls == null) {
            if (other.serverUrls != null) {
                return false;
            }
        } else if (!this.serverUrls.equals(other.serverUrls)) {
            return false;
        }
        return true;
    }

}
