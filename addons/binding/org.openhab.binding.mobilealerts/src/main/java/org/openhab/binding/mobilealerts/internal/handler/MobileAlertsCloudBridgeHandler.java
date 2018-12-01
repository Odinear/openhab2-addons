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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.smarthome.config.core.Configuration;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.mobilealerts.internal.protocol.MobileAlertsCloudConnection;
import org.openhab.binding.mobilealerts.internal.protocol.MobileAlertsDeviceData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Stefan Mueller - Initial contribution
 *
 */
public class MobileAlertsCloudBridgeHandler extends MobileAlertsBridgeHandler {

    private final Logger logger = LoggerFactory.getLogger(MobileAlertsCloudBridgeHandler.class);
    private ScheduledFuture<?> refreshTimer;

    public MobileAlertsCloudBridgeHandler(Bridge bridge) {
        super(bridge);
    }

    @Override
    public void handleCommand(@NonNull ChannelUID channelUID, @NonNull Command command) {

    }

    @Override
    public void initialize() {
        logger.debug("Mobile Alerts Cloud Bridge: initializing bridge handler");
        Configuration config = getConfig();
        String phoneid = (String) config.get(CONFIG_PHONEID);
        Integer interval = ((BigDecimal) config.get(CONFIG_POLLINGINTERVAL)).intValueExact();

        try {
            connection = new MobileAlertsCloudConnection(phoneid);
            connection.startCommunication();
            updateOnlineDevices();
            setupRefreshTimer(interval);

        } catch (Exception e) {
            logger.debug("Mobile Alerts: cannot initialize connection: " + e.getMessage());
            bridgeOffline();
            return;
        }
        bridgeOnline();
    }

    private void setupRefreshTimer(Integer refreshInterval) {
        if (this.refreshTimer != null) {
            this.refreshTimer.cancel(true);
            this.refreshTimer = null;
        }

        if ((refreshInterval == null) || (refreshInterval == 0)) {
            return;
        }

        logger.debug("Mobile Alerts: check for new data every {} min", refreshInterval);
        this.refreshTimer = scheduler.scheduleWithFixedDelay(() -> {
            logger.debug("Mobile Alerts: check for new data");
            updateOnlineDevices();
        }, refreshInterval, refreshInterval, TimeUnit.MINUTES);
    }

    private void updateOnlineDevices() {
        logger.debug("Mobile Alerts Cloud Bridge: initializing bridge handler");
        Map<String, MobileAlertsDeviceData> data;

        try {
            connection.updateDeviceList();
        } catch (Exception e) {
            logger.debug("Mobile Alerts: can not read from cloud service");
            bridgeOffline();
        }
        data = connection.getDevices();
        List<Thing> things = this.getThing().getThings();
        for (Thing thing : things) {
            updateDeviceData(thing, data);
        }
    }

    @Override
    public void dispose() {
        if (this.refreshTimer != null) {
            this.refreshTimer.cancel(true);
        }
        this.refreshTimer = null;
    }

    @Override
    public void childHandlerInitialized(@NonNull ThingHandler childHandler, @NonNull Thing childThing) {
        updateDeviceData(childThing, connection.getDevices());
        super.childHandlerInitialized(childHandler, childThing);
    }

    private void updateDeviceData(Thing thing, Map<String, MobileAlertsDeviceData> data) {
        String id = thing.getConfiguration().get(CONFIG_DEVICEID).toString();
        if (data.containsKey(id)) {
            MobileAlertsDeviceData devdata = data.get(id);
            MobileAlertsBaseDeviceHandler device = (MobileAlertsBaseDeviceHandler) thing.getHandler();
            if (device != null) {
                device.updateChannels(devdata);
                device.updateDeviceStatus();
            }
        }
    }
}
