/* Generated by camel build tools - do NOT edit this file! */
package org.apache.camel.component.iec60870.client;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.spi.GeneratedPropertyConfigurer;
import org.apache.camel.spi.PropertyConfigurerGetter;
import org.apache.camel.util.CaseInsensitiveMap;
import org.apache.camel.support.component.PropertyConfigurerSupport;

/**
 * Generated by camel build tools - do NOT edit this file!
 */
@SuppressWarnings("unchecked")
public class ClientEndpointConfigurer extends PropertyConfigurerSupport implements GeneratedPropertyConfigurer, PropertyConfigurerGetter {

    @Override
    public boolean configure(CamelContext camelContext, Object obj, String name, Object value, boolean ignoreCase) {
        ClientEndpoint target = (ClientEndpoint) obj;
        switch (ignoreCase ? name.toLowerCase() : name) {
        case "acknowledgewindow":
        case "acknowledgeWindow": target.getConnectionOptions().setAcknowledgeWindow(property(camelContext, short.class, value)); return true;
        case "adsuaddresstype":
        case "adsuAddressType": target.getConnectionOptions().setAdsuAddressType(property(camelContext, org.eclipse.neoscada.protocol.iec60870.ASDUAddressType.class, value)); return true;
        case "basicpropertybinding":
        case "basicPropertyBinding": target.setBasicPropertyBinding(property(camelContext, boolean.class, value)); return true;
        case "bridgeerrorhandler":
        case "bridgeErrorHandler": target.setBridgeErrorHandler(property(camelContext, boolean.class, value)); return true;
        case "causeoftransmissiontype":
        case "causeOfTransmissionType": target.getConnectionOptions().setCauseOfTransmissionType(property(camelContext, org.eclipse.neoscada.protocol.iec60870.CauseOfTransmissionType.class, value)); return true;
        case "causesourceaddress":
        case "causeSourceAddress": target.getConnectionOptions().setCauseSourceAddress(property(camelContext, byte.class, value)); return true;
        case "connectionid":
        case "connectionId": target.setConnectionId(property(camelContext, java.lang.String.class, value)); return true;
        case "connectiontimeout":
        case "connectionTimeout": target.getConnectionOptions().setConnectionTimeout(property(camelContext, int.class, value)); return true;
        case "datamoduleoptions":
        case "dataModuleOptions": target.getConnectionOptions().setDataModuleOptions(property(camelContext, org.eclipse.neoscada.protocol.iec60870.client.data.DataModuleOptions.class, value)); return true;
        case "exceptionhandler":
        case "exceptionHandler": target.setExceptionHandler(property(camelContext, org.apache.camel.spi.ExceptionHandler.class, value)); return true;
        case "exchangepattern":
        case "exchangePattern": target.setExchangePattern(property(camelContext, org.apache.camel.ExchangePattern.class, value)); return true;
        case "ignorebackgroundscan":
        case "ignoreBackgroundScan": target.getConnectionOptions().setIgnoreBackgroundScan(property(camelContext, boolean.class, value)); return true;
        case "ignoredaylightsavingtime":
        case "ignoreDaylightSavingTime": target.getConnectionOptions().setIgnoreDaylightSavingTime(property(camelContext, boolean.class, value)); return true;
        case "informationobjectaddresstype":
        case "informationObjectAddressType": target.getConnectionOptions().setInformationObjectAddressType(property(camelContext, org.eclipse.neoscada.protocol.iec60870.InformationObjectAddressType.class, value)); return true;
        case "lazystartproducer":
        case "lazyStartProducer": target.setLazyStartProducer(property(camelContext, boolean.class, value)); return true;
        case "maxunacknowledged":
        case "maxUnacknowledged": target.getConnectionOptions().setMaxUnacknowledged(property(camelContext, short.class, value)); return true;
        case "protocoloptions":
        case "protocolOptions": target.getConnectionOptions().setProtocolOptions(property(camelContext, org.eclipse.neoscada.protocol.iec60870.ProtocolOptions.class, value)); return true;
        case "statelistener":
        case "stateListener": target.getConnectionOptions().setStateListener(property(camelContext, org.eclipse.neoscada.protocol.iec60870.client.AutoConnectClient.StateListener.class, value)); return true;
        case "synchronous": target.setSynchronous(property(camelContext, boolean.class, value)); return true;
        case "timezone":
        case "timeZone": target.getConnectionOptions().setTimeZone(property(camelContext, java.util.TimeZone.class, value)); return true;
        case "timeout1": target.getConnectionOptions().setTimeout1(property(camelContext, int.class, value)); return true;
        case "timeout2": target.getConnectionOptions().setTimeout2(property(camelContext, int.class, value)); return true;
        case "timeout3": target.getConnectionOptions().setTimeout3(property(camelContext, int.class, value)); return true;
        default: return false;
        }
    }

    @Override
    public Map<String, Object> getAllOptions(Object target) {
        Map<String, Object> answer = new CaseInsensitiveMap();
        answer.put("acknowledgeWindow", short.class);
        answer.put("adsuAddressType", org.eclipse.neoscada.protocol.iec60870.ASDUAddressType.class);
        answer.put("basicPropertyBinding", boolean.class);
        answer.put("bridgeErrorHandler", boolean.class);
        answer.put("causeOfTransmissionType", org.eclipse.neoscada.protocol.iec60870.CauseOfTransmissionType.class);
        answer.put("causeSourceAddress", byte.class);
        answer.put("connectionId", java.lang.String.class);
        answer.put("connectionTimeout", int.class);
        answer.put("dataModuleOptions", org.eclipse.neoscada.protocol.iec60870.client.data.DataModuleOptions.class);
        answer.put("exceptionHandler", org.apache.camel.spi.ExceptionHandler.class);
        answer.put("exchangePattern", org.apache.camel.ExchangePattern.class);
        answer.put("ignoreBackgroundScan", boolean.class);
        answer.put("ignoreDaylightSavingTime", boolean.class);
        answer.put("informationObjectAddressType", org.eclipse.neoscada.protocol.iec60870.InformationObjectAddressType.class);
        answer.put("lazyStartProducer", boolean.class);
        answer.put("maxUnacknowledged", short.class);
        answer.put("protocolOptions", org.eclipse.neoscada.protocol.iec60870.ProtocolOptions.class);
        answer.put("stateListener", org.eclipse.neoscada.protocol.iec60870.client.AutoConnectClient.StateListener.class);
        answer.put("synchronous", boolean.class);
        answer.put("timeZone", java.util.TimeZone.class);
        answer.put("timeout1", int.class);
        answer.put("timeout2", int.class);
        answer.put("timeout3", int.class);
        return answer;
    }

    @Override
    public Object getOptionValue(Object obj, String name, boolean ignoreCase) {
        ClientEndpoint target = (ClientEndpoint) obj;
        switch (ignoreCase ? name.toLowerCase() : name) {
        case "acknowledgewindow":
        case "acknowledgeWindow": return target.getConnectionOptions().getAcknowledgeWindow();
        case "adsuaddresstype":
        case "adsuAddressType": return target.getConnectionOptions().getAdsuAddressType();
        case "basicpropertybinding":
        case "basicPropertyBinding": return target.isBasicPropertyBinding();
        case "bridgeerrorhandler":
        case "bridgeErrorHandler": return target.isBridgeErrorHandler();
        case "causeoftransmissiontype":
        case "causeOfTransmissionType": return target.getConnectionOptions().getCauseOfTransmissionType();
        case "causesourceaddress":
        case "causeSourceAddress": return target.getConnectionOptions().getCauseSourceAddress();
        case "connectionid":
        case "connectionId": return target.getConnectionId();
        case "connectiontimeout":
        case "connectionTimeout": return target.getConnectionOptions().getConnectionTimeout();
        case "datamoduleoptions":
        case "dataModuleOptions": return target.getConnectionOptions().getDataModuleOptions();
        case "exceptionhandler":
        case "exceptionHandler": return target.getExceptionHandler();
        case "exchangepattern":
        case "exchangePattern": return target.getExchangePattern();
        case "ignorebackgroundscan":
        case "ignoreBackgroundScan": return target.getConnectionOptions().isIgnoreBackgroundScan();
        case "ignoredaylightsavingtime":
        case "ignoreDaylightSavingTime": return target.getConnectionOptions().isIgnoreDaylightSavingTime();
        case "informationobjectaddresstype":
        case "informationObjectAddressType": return target.getConnectionOptions().getInformationObjectAddressType();
        case "lazystartproducer":
        case "lazyStartProducer": return target.isLazyStartProducer();
        case "maxunacknowledged":
        case "maxUnacknowledged": return target.getConnectionOptions().getMaxUnacknowledged();
        case "protocoloptions":
        case "protocolOptions": return target.getConnectionOptions().getProtocolOptions();
        case "statelistener":
        case "stateListener": return target.getConnectionOptions().getStateListener();
        case "synchronous": return target.isSynchronous();
        case "timezone":
        case "timeZone": return target.getConnectionOptions().getTimeZone();
        case "timeout1": return target.getConnectionOptions().getTimeout1();
        case "timeout2": return target.getConnectionOptions().getTimeout2();
        case "timeout3": return target.getConnectionOptions().getTimeout3();
        default: return null;
        }
    }
}

