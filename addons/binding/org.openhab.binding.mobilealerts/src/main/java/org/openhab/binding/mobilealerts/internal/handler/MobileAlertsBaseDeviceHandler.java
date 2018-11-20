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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.openhab.binding.mobilealerts.internal.protocol.MobileAlertsDeviceData;
import org.slf4j.Logger;

/**
 *
 * @author Stefan Mueller - Initial contribution
 *
 */
public abstract class MobileAlertsBaseDeviceHandler extends BaseThingHandler {

    public MobileAlertsBaseDeviceHandler(Thing thing, Logger logger) {
        super(thing);
        this.logger = logger;
        // TODO Auto-generated constructor stub
    }

    protected Date timestamp;
    protected Boolean batteryLow;
    protected final Logger logger;

    public void updateChannels(MobileAlertsDeviceData data) {
        for (Map.Entry<String, String> property : data.Properties.entrySet()) {
            switch (property.getKey()) {
                case PROPERTY_TIMESTAMP:
                    try {
                        timestamp = parseTimeStampFromString(property.getValue());
                    } catch (ParseException e) {
                        logger.debug("MobileAlerts: Error updateing timestamp " + e.getMessage());
                    }
                    break;
                case PROPERTY_BATTERLOW:
                    // TODO: update Battery state
                    break;
                default:
                    updateChannel(property);
                    break;
            }
        }
    }

    public void updateDeviceStatus() {
        Date expired = DateUtils.addHours(timestamp, OFFLINE_THRESHOLD);
        Date now = Calendar.getInstance().getTime();

        if (expired.before(now)) {
            updateStatus(ThingStatus.OFFLINE);
        } else {
            updateStatus(ThingStatus.ONLINE);
        }
    }

    @Override
    public void initialize() {
        updateStatus(ThingStatus.OFFLINE);

    }

    protected abstract void updateChannel(Map.Entry<String, String> property);

    protected Date parseTimeStampFromString(String timestamp) throws ParseException {
        DateFormat df = new SimpleDateFormat(PARSE_TIMESTAMP, Locale.ENGLISH);
        return df.parse(timestamp);
    }

    protected float parseFloatFromString(String number) {
        return Float.parseFloat(number.replaceAll("[^\\d.]", ""));
    }

}
