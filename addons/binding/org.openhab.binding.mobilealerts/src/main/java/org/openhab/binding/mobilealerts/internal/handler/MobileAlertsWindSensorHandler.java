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

import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.types.Command;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Stefan Mueller - Initial contribution
 *
 */
public class MobileAlertsWindSensorHandler extends MobileAlertsBaseDeviceHandler {

    private final String[] directions = { "north", "north-northeast", "northeast", "east-northeast", "east",
            "east-southeast", "southeast", "south-southeast", "south", "south-southwest", "southwest", "west-southwest",
            "west", "west-northwest", "northwest", "north-northwest" };

    public MobileAlertsWindSensorHandler(Thing thing) {
        super(thing, LoggerFactory.getLogger(MobileAlertsWindSensorHandler.class));
        // TODO Auto-generated constructor stub
    }

    @Override
    public void handleCommand(@NonNull ChannelUID channelUID, @NonNull Command command) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void updateChannel(Entry<String, String> property) {
        switch (property.getKey()) {
            case PROPERTY_WIND:
                float speed = parseFloatFromString(property.getValue());
                updateState(CHANNEL_WINDSPEED, new DecimalType(speed));
                break;
            case PROPERTY_GUST:
                float gust = parseFloatFromString(property.getValue());
                updateState(CHANNEL_WINDGUST, new DecimalType(gust));
                break;
            case PROPERTY_DIRECTION:
                double direction = parseDirectionFromString(property.getValue());
                updateState(CHANNEL_WINDDIRECTION, new DecimalType(direction));
                break;
        }
    }

    private double parseDirectionFromString(String direction) {
        Integer index = java.util.Arrays.asList(directions).indexOf(direction.toLowerCase());

        return index * 22.5;
    }

}
