/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mobilealerts.internal.discovery;

import static org.openhab.binding.mobilealerts.internal.MobileAlertsBindingConstants.*;

import java.util.Date;
import java.util.Map;

import org.eclipse.smarthome.config.discovery.AbstractDiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.mobilealerts.internal.handler.MobileAlertsBridgeHandler;
import org.openhab.binding.mobilealerts.internal.protocol.MobileAlertsConnection;
import org.openhab.binding.mobilealerts.internal.protocol.MobileAlertsDeviceData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Stefan Mueller - Initial contribution
 *
 */
public class MobileAlertsDiscoveryService extends AbstractDiscoveryService {

    private final Logger logger = LoggerFactory.getLogger(MobileAlertsDiscoveryService.class);

    private static final int TIMEOUT = 5;

    private ThingUID bridgeUID;
    private MobileAlertsBridgeHandler handler;

    public MobileAlertsDiscoveryService(MobileAlertsBridgeHandler handler) {
        super(SUPPORTED_THING_TYPES_UIDS, TIMEOUT, false);
        logger.debug("Mobile Alerts: discovery service {}", handler);
        this.bridgeUID = handler.getThing().getUID();
        this.handler = handler;
    }

    public void activate() {
        this.handler.setMaDiscovery(this);
    }

    @Override
    public void deactivate() {
        removeOlderResults(new Date().getTime());
        this.handler.setMaDiscovery(null);
        this.handler = null;
    }

    @Override
    protected void startScan() {
        discoverDevices();

    }

    public void discoverDevices() {
        MobileAlertsConnection connection = handler.getConnection();
        Map<String, MobileAlertsDeviceData> devices = connection.getDevices();

        for (MobileAlertsDeviceData device : devices.values()) {
            addDevice(device);
        }
    }

    public void addDevice(MobileAlertsDeviceData data) {
        ThingUID uid = getIdFromSerial(data.Id);
        if (uid != null) {
            DiscoveryResult discoveryResult = DiscoveryResultBuilder.create(uid).withBridge(bridgeUID)
                    .withProperty(CONFIG_DEVICEID, data.Id).withLabel(data.Name).build();
            thingDiscovered(discoveryResult);
        }
    }

    private ThingUID getIdFromSerial(String serial) {
        Short typeid = Short.parseShort(serial.substring(0, 2), 16);
        switch (typeid) {
            case 0x03:
                return new ThingUID(THING_TYPE_TEMPHYGRO, this.handler.getThing().getUID(), serial);
            case 0x0B:
                return new ThingUID(THING_TYPE_WIND, this.handler.getThing().getUID(), serial);
            case 0x08:
                return new ThingUID(THING_TYPE_RAIN, this.handler.getThing().getUID(), serial);
            case 0x09:
                return new ThingUID(THING_TYPE_TEMPHYGROPRO, this.handler.getThing().getUID(), serial);
            case 0x10:
                return new ThingUID(THING_TYPE_CONTACT, this.handler.getThing().getUID(), serial);
            case 0x12:
                return new ThingUID(THING_TYPE_HUMIDITYGUARD, this.handler.getThing().getUID(), serial);
            // TODO: add more
            default:
                return null;
        }
    }

    @Override
    protected synchronized void stopScan() {
        super.stopScan();
        removeOlderResults(getTimestampOfLastScan());
    }
}
