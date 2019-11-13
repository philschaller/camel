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

import org.junit.Assert;
import org.junit.Test;

public class ConnectionIdTest {
    @Test
    public void testNotEqual1() {
        ConnectionId id1 = new ConnectionId("host:1202", "id1");
        ConnectionId id2 = new ConnectionId("host:1202", "id2");
        Assert.assertFalse("Must be different", id1.equals(id2));
    }

    @Test
    public void testNotEqual2() {
        ConnectionId id1 = new ConnectionId("host1:1202", "id");
        ConnectionId id2 = new ConnectionId("host2:1202", "id");
        Assert.assertFalse("Must be different", id1.equals(id2));
    }

    @Test
    public void testNotEqual3() {
        ConnectionId id1 = new ConnectionId("host:12021", "id");
        ConnectionId id2 = new ConnectionId("host:12022", "id");
        Assert.assertFalse("Must be different", id1.equals(id2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegal1() {
        new ConnectionId("host:-1", "id");
    }

    @Test
    public void testGetters() {
        ConnectionId id = new ConnectionId("host:1202", "id");
        Assert.assertEquals("host:1202", id.getServerUrls());
        Assert.assertTrue("Must be equal", id.getServers().get("host").equals(1202));
        Assert.assertTrue("Must be equal", id.getConnectionId().equals("id"));
    }

    @Test
    public void testEqual1() {
        ConnectionId id1 = new ConnectionId("host:1202", "id");
        ConnectionId id2 = new ConnectionId("host:1202", "id");
        Assert.assertTrue("Must be equal", id1.equals(id2));
    }

    @Test
    public void testEqual2() {
        ConnectionId id1 = new ConnectionId("host:1202", "id");
        ConnectionId id2 = new ConnectionId("host:1202", "id");

        Assert.assertTrue("Hash code must be equal", id1.hashCode() == id2.hashCode());
    }

    @Test
    public void testMultipleServers() {
        ConnectionId id = new ConnectionId("host1:1202,10.0.0.1:1203", "id");
        Assert.assertEquals("host1:1202,10.0.0.1:1203", id.getServerUrls());
        Assert.assertTrue("Must be equal", id.getServers().get("host1").equals(1202));
        Assert.assertTrue("Must be equal", id.getServers().get("10.0.0.1").equals(1203));
        Assert.assertTrue("Must be equal", id.getConnectionId().equals("id"));
    }

    @Test(expected = NullPointerException.class)
    public void testIllegalMissingServer_Null() {
        ConnectionId id = new ConnectionId(null, "id");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalMissingServer_Blank() {
        ConnectionId id = new ConnectionId("", "id");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalServerSeparator() {
        ConnectionId id = new ConnectionId("host1:1202;10.0.0.1:1203", "id");
    }
}
