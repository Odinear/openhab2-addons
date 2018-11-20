/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mobilealerts.internal.handler;

import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.binding.BaseBridgeHandler;
import org.openhab.binding.mobilealerts.internal.discovery.MobileAlertsDiscoveryService;
import org.openhab.binding.mobilealerts.internal.protocol.MobileAlertsConnection;

/**
 *
 * @author Stefan Mueller - Initial contribution
 *
 */
public abstract class MobileAlertsBridgeHandler extends BaseBridgeHandler {

    protected MobileAlertsConnection connection;
    protected MobileAlertsDiscoveryService maDiscovery;

    public MobileAlertsBridgeHandler(Bridge bridge) {
        super(bridge);
    }

    public MobileAlertsConnection getConnection() {
        return connection;
    }

    public void setMaDiscovery(MobileAlertsDiscoveryService maDiscovery) {
        this.maDiscovery = maDiscovery;
    }

}
