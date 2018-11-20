/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mobilealerts.internal.handler;

import static org.openhab.binding.mobilealerts.internal.MobileAlertsBindingConstants.*;

import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.types.Command;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Stefan Mueller - Initial contribution
 *
 */
public class MobileAlertsHumidityGuardSensorHandler extends MobileAlertsBaseDeviceHandler {

    public MobileAlertsHumidityGuardSensorHandler(Thing thing) {
        super(thing, LoggerFactory.getLogger(MobileAlertsHumidityGuardSensorHandler.class));
        // TODO Auto-generated constructor stub
    }

    @Override
    public void handleCommand(@NonNull ChannelUID channelUID, @NonNull Command command) {
        String deviceId = this.getConfig().get(CONFIG_DEVICEID).toString();

        Bridge maBridge = getBridge();
        if (maBridge == null) {
            updateStatus(ThingStatus.UNINITIALIZED, ThingStatusDetail.BRIDGE_UNINITIALIZED,
                    "Mobile Alerts: no bridge initialized when trying to querry command " + deviceId);
            return;
        }
        MobileAlertsCloudBridgeHandler maHandler = (MobileAlertsCloudBridgeHandler) maBridge.getHandler();
        if (maHandler == null) {
            updateStatus(ThingStatus.UNINITIALIZED, ThingStatusDetail.BRIDGE_UNINITIALIZED,
                    "Mobile Aletrs: no bridge initialized when trying to querry command " + deviceId);
            return;
        }

    }

    @Override
    public void updateChannel(Map.Entry<String, String> property) {
        switch (property.getKey()) {
            case PROPERTY_TEMPERATURE:
                float temperature = parseFloatFromString(property.getValue());
                updateState(CHANNEL_TEMPERATURE, new DecimalType(temperature));
                break;
            case PROPERTY_HUMIDITY:
                float humidity = parseFloatFromString(property.getValue());
                updateState(CHANNEL_HUMIDITY, new DecimalType(humidity)); // TODO: PercentType?
                break;
            case PROPERTY_AVG_HUMIDITY_3H:
                float humidity3h = parseFloatFromString(property.getValue());
                updateState(CHANNEL_HUMIDITY_3H, new DecimalType(humidity3h)); // TODO: PercentType?
                break;
            case PROPERTY_AVG_HUMIDITY_24H:
                float humidity24h = parseFloatFromString(property.getValue());
                updateState(CHANNEL_HUMIDITY_24H, new DecimalType(humidity24h)); // TODO: PercentType?
                break;
            case PROPERTY_AVG_HUMIDITY_7D:
                float humidity7d = parseFloatFromString(property.getValue());
                updateState(CHANNEL_HUMIDITY_7D, new DecimalType(humidity7d)); // TODO: PercentType?
                break;
            case PROPERTY_AVG_HUMIDITY_30D:
                float humidity30d = parseFloatFromString(property.getValue());
                updateState(CHANNEL_HUMIDITY_30D, new DecimalType(humidity30d)); // TODO: PercentType?
                break;
        }

    }

    @Override
    public void initialize() {
        logger.debug("Mobile Alerts: Start initializing Temperatur Hygro sensor!");
        super.initialize();
    }

}
