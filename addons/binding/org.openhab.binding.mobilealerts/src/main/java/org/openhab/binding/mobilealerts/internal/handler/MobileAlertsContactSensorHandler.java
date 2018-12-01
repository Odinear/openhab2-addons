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
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.types.Command;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Stefan Mueller - Initial contribution
 *
 */
public class MobileAlertsContactSensorHandler extends MobileAlertsBaseDeviceHandler {

    public MobileAlertsContactSensorHandler(Thing thing) {
        super(thing, LoggerFactory.getLogger(MobileAlertsContactSensorHandler.class));
        // TODO Auto-generated constructor stub
    }

    @Override
    public void handleCommand(@NonNull ChannelUID channelUID, @NonNull Command command) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void updateChannel(Entry<String, String> property) {
        switch (property.getKey()) {
            case PROPERTY_CONTACT:
                Boolean state = parseSwitchFromString(property.getValue());
                updateState(CHANNEL_CONTACT, state ? OnOffType.OFF : OnOffType.ON);
                break;
        }
    }

    private Boolean parseSwitchFromString(String state) {
        return state.toLowerCase() != "closed";
    }

}