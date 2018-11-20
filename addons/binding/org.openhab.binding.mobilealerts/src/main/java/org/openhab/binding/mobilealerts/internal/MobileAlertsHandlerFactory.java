/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mobilealerts.internal;

import static org.openhab.binding.mobilealerts.internal.MobileAlertsBindingConstants.*;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.config.core.Configuration;
import org.eclipse.smarthome.config.discovery.DiscoveryService;
import org.eclipse.smarthome.core.common.AbstractUID;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
import org.openhab.binding.mobilealerts.internal.discovery.MobileAlertsDiscoveryService;
import org.openhab.binding.mobilealerts.internal.handler.MobileAlertsBridgeHandler;
import org.openhab.binding.mobilealerts.internal.handler.MobileAlertsCloudBridgeHandler;
import org.openhab.binding.mobilealerts.internal.handler.MobileAlertsContactSensorHandler;
import org.openhab.binding.mobilealerts.internal.handler.MobileAlertsHumidityGuardSensorHandler;
import org.openhab.binding.mobilealerts.internal.handler.MobileAlertsRainSensorHandler;
import org.openhab.binding.mobilealerts.internal.handler.MobileAlertsTempHygroProSensorHandler;
import org.openhab.binding.mobilealerts.internal.handler.MobileAlertsTempHygroSensorHandler;
import org.openhab.binding.mobilealerts.internal.handler.MobileAlertsWindSensorHandler;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link MobileAlertsHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Stefan Mueller - Initial contribution
 */
@NonNullByDefault
@Component(configurationPid = "binding.mobilealerts", service = ThingHandlerFactory.class)
public class MobileAlertsHandlerFactory extends BaseThingHandlerFactory {

    private Map<ThingUID, ServiceRegistration<?>> discoveryServiceRegs = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(MobileAlertsHandlerFactory.class);

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID) || BRIDGE_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (BRIDGE_THING_TYPE_CLOUDBRIDGE.equals(thingTypeUID)) {
            MobileAlertsCloudBridgeHandler handler = new MobileAlertsCloudBridgeHandler((Bridge) thing);
            registerDiscoveryService(handler);
            return handler;
        } else if (THING_TYPE_TEMPHYGRO.equals(thingTypeUID)) {
            return new MobileAlertsTempHygroSensorHandler(thing);
        } else if (THING_TYPE_WIND.equals(thingTypeUID)) {
            return new MobileAlertsWindSensorHandler(thing);
        } else if (THING_TYPE_RAIN.equals(thingTypeUID)) {
            return new MobileAlertsRainSensorHandler(thing);
        } else if (THING_TYPE_TEMPHYGROPRO.equals(thingTypeUID)) {
            return new MobileAlertsTempHygroProSensorHandler(thing);
        } else if (THING_TYPE_HUMIDITYGUARD.equals(thingTypeUID)) {
            return new MobileAlertsHumidityGuardSensorHandler(thing);
        } else if (THING_TYPE_CONTACT.equals(thingTypeUID)) {
            return new MobileAlertsContactSensorHandler(thing);
        }

        return null;
    }

    @Override
    public @Nullable Thing createThing(ThingTypeUID thingTypeUID, Configuration configuration,
            @Nullable ThingUID thingUID, @Nullable ThingUID bridgeUID) {

        Thing thing = null;

        if (SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID)) {

            if (BRIDGE_THING_TYPE_CLOUDBRIDGE.equals(thingTypeUID)) {
                thing = super.createThing(thingTypeUID, configuration, thingUID, null);
                logger.debug("Mobile Alerts: created Thing of type {} ", thingTypeUID);

            } else if (BRIDGE_THING_TYPE_LOCALBRIDGE.equals(thingTypeUID)) {
                // TODO: add local bridge
            } else {
                thing = createThing(thingUID, thingTypeUID, configuration, bridgeUID);
            }

        } else {
            throw new IllegalArgumentException(
                    "The thing type " + thingTypeUID + " is not supported by the mobile alerts binding.");
        }

        return thing;
    }

    private @Nullable Thing createThing(@Nullable ThingUID thingUID, ThingTypeUID thingTypeUID,
            Configuration configuration, @Nullable ThingUID bridgeUID) {
        Thing thing = null;
        if (thingUID != null && bridgeUID != null) {
            String id = thingUID.getAsString().split(AbstractUID.SEPARATOR)[3];
            ThingUID device = new ThingUID(thingTypeUID, id);
            thing = super.createThing(thingTypeUID, configuration, device, bridgeUID);
            logger.debug("Mobile Alerts: created Thing of type {} with id {} ", thingTypeUID, id);
        }
        return thing;
    }

    private synchronized void registerDiscoveryService(MobileAlertsBridgeHandler bridgeHandler) {
        MobileAlertsDiscoveryService discoveryService = new MobileAlertsDiscoveryService(bridgeHandler);
        this.discoveryServiceRegs.put(bridgeHandler.getThing().getUID(), this.bundleContext
                .registerService(DiscoveryService.class.getName(), discoveryService, new Hashtable<String, Object>()));
        discoveryService.activate();

    }

    @Override
    protected synchronized void removeHandler(ThingHandler thingHandler) {
        if (thingHandler instanceof MobileAlertsBridgeHandler) {
            ServiceRegistration<?> serviceReg = this.discoveryServiceRegs.remove(thingHandler.getThing().getUID());
            if (serviceReg != null) {
                // remove discovery service, if bridge handler is removed
                MobileAlertsDiscoveryService service = (MobileAlertsDiscoveryService) bundleContext
                        .getService(serviceReg.getReference());
                serviceReg.unregister();
                if (service != null) {
                    service.deactivate();
                }
            }
        }
    }
}
