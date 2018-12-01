/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mobilealerts.internal.handler;

import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.types.Command;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Stefan Mueller - Initial contribution
 *
 */
public class MobileAlertsGatewayHandler extends MobileAlertsBaseDeviceHandler {

    public MobileAlertsGatewayHandler(Thing thing) {
        super(thing, LoggerFactory.getLogger(MobileAlertsGatewayHandler.class));
        // TODO Auto-generated constructor stub
    }

    @Override
    public void handleCommand(@NonNull ChannelUID channelUID, @NonNull Command command) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void updateChannel(Entry<String, String> property) {

    }
}
