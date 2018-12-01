/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mobilealerts.internal.handler;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.mobilealerts.internal.protocol.MobileAlertsLocalConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Stefan Mueller - Initial contribution
 *
 */
public class MobileAlertsLocalBridgeHandler extends MobileAlertsBridgeHandler {

    private final Logger logger = LoggerFactory.getLogger(MobileAlertsLocalBridgeHandler.class);

    public MobileAlertsLocalBridgeHandler(Bridge bridge) {
        super(bridge);
    }

    @Override
    public void handleCommand(@NonNull ChannelUID channelUID, @NonNull Command command) {
        // TODO Auto-generated method stub

    }

    @Override
    public void initialize() {
        logger.debug("Mobile Alerts Local Bridge: initializing bridge handler");
        try {
            connection = new MobileAlertsLocalConnection();
            connection.startCommunication();
            connection.updateDeviceList();

        } catch (Exception e) {
            logger.debug("Mobile Alerts: cannot initialize connection: " + e.getMessage());
            bridgeOffline();
            return;
        }

        bridgeOnline();
    }

}
