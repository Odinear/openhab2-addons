/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mobilealerts.internal.protocol;

/**
 *
 * @author Stefan Mueller - Initial contribution
 *
 */
public class MobileAlertsLocalConnection extends MobileAlertsConnection {
    private MobileAlertsUDPCommunication udpConn;

    public MobileAlertsLocalConnection() {
    }

    @Override
    public void startCommunication() throws Exception {
        udpConn = new MobileAlertsUDPCommunication();
    }

    @Override
    public void updateDeviceList() throws Exception {
        if (udpConn.scanForGateways()) {
            devices.putAll(udpConn.getDevices());
        }

    }

}
