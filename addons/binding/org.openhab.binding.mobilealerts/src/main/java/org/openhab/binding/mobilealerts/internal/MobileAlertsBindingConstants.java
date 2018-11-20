/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mobilealerts.internal;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The {@link MobileAlertsBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Stefan Mueller - Initial contribution
 */
@NonNullByDefault
public class MobileAlertsBindingConstants {

    private static final String BINDING_ID = "mobilealerts";

    // List all Thing Type UIDs, related to the Hue Binding

    // bridge
    public static final ThingTypeUID BRIDGE_THING_TYPE_CLOUDBRIDGE = new ThingTypeUID(BINDING_ID, "cloudbridge");
    public static final ThingTypeUID BRIDGE_THING_TYPE_LOCALBRIDGE = new ThingTypeUID(BINDING_ID, "localbridge");

    // generic thing types
    public static final ThingTypeUID THING_TYPE_TEMPHYGRO = new ThingTypeUID(BINDING_ID, "temphygrosensor");
    public static final ThingTypeUID THING_TYPE_RAIN = new ThingTypeUID(BINDING_ID, "rainsensor");
    public static final ThingTypeUID THING_TYPE_WIND = new ThingTypeUID(BINDING_ID, "windsensor");
    public static final ThingTypeUID THING_TYPE_TEMPHYGROPRO = new ThingTypeUID(BINDING_ID, "temphygroprosensor");
    public static final ThingTypeUID THING_TYPE_HUMIDITYGUARD = new ThingTypeUID(BINDING_ID, "humidityguardsensor");
    public static final ThingTypeUID THING_TYPE_CONTACT = new ThingTypeUID(BINDING_ID, "contactsensor");

    // thing type sets
    public static final Set<ThingTypeUID> BRIDGE_THING_TYPES_UIDS = Collections.unmodifiableSet(
            Stream.of(BRIDGE_THING_TYPE_CLOUDBRIDGE, BRIDGE_THING_TYPE_LOCALBRIDGE).collect(Collectors.toSet()));

    public static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Collections
            .unmodifiableSet(Stream.of(THING_TYPE_TEMPHYGRO, THING_TYPE_WIND, THING_TYPE_RAIN, THING_TYPE_TEMPHYGROPRO,
                    THING_TYPE_HUMIDITYGUARD, THING_TYPE_CONTACT).collect(Collectors.toSet()));

    // List all channels
    public static final String CHANNEL_TEMPERATURE = "temperature";
    public static final String CHANNEL_HUMIDITY = "humidity";
    public static final String CHANNEL_BATTERY = "battery";
    public static final String CHANNEL_WINDSPEED = "wind";
    public static final String CHANNEL_WINDGUST = "gust";
    public static final String CHANNEL_WINDDIRECTION = "direction";
    public static final String CHANNEL_RAIN = "rain";
    public static final String CHANNEL_TEMPERATURE_EXTERNAL = "temperatureext";
    public static final String CHANNEL_HUMIDITY_3H = "humidity3h";
    public static final String CHANNEL_HUMIDITY_24H = "humidity24h";
    public static final String CHANNEL_HUMIDITY_7D = "humidity7d";
    public static final String CHANNEL_HUMIDITY_30D = "humidity30d";
    public static final String CHANNEL_CONTACT = "contact";

    // Bridge config properties
    public static final String CONFIG_PHONEID = "phoneId";
    public static final String CONFIG_POLLINGINTERVAL = "pollingInterval";
    public static final Integer CONFIG_UDPPORT = 8003;

    // Device config properties
    public static final String CONFIG_DEVICEID = "deviceId";

    // General Sensor Properties
    public static final String PROPERTY_TIMESTAMP = "Timestamp";
    public static final String PROPERTY_BATTERLOW = "unknown";

    // TempGygroSensor
    public static final String PROPERTY_TEMPERATURE = "Temperature";
    public static final String PROPERTY_HUMIDITY = "Humidity";
    public static final String PROPERTY_TEMPERATURE_EXTERNAL = "Temperature Probe";

    // WindSensor
    public static final String PROPERTY_WIND = "Windspeed";
    public static final String PROPERTY_GUST = "Gust";
    public static final String PROPERTY_DIRECTION = "Wind Direction";

    // RainSensor
    public static final String PROPERTY_RAIN = "Rain";

    // Humidity Guard
    public static final String PROPERTY_AVG_HUMIDITY_3H = "Avg Humidity 3H";
    public static final String PROPERTY_AVG_HUMIDITY_24H = "Avg Humidity 24H";
    public static final String PROPERTY_AVG_HUMIDITY_7D = "Avg Humidity 7D";
    public static final String PROPERTY_AVG_HUMIDITY_30D = "Avg Humidity 30D";

    // Contact
    public static final String PROPERTY_CONTACT = "Contact Sensor";

    // Gateway
    public static final String PROPERTY_DHCP_IP = "DHCP_IP";
    public static final String PROPERTY_USE_DHCP = "DHCP_USED";
    public static final String PROPERTY_FIXED_IP = "FIXED_IP";
    public static final String PROPERTY_DHCP_NETMASK = "DHCP_NETMASK";
    public static final String PROPERTY_FIXED_GATEWAY = "FIXED_GATEWAY";
    public static final String PROPERTY_SERVER = "SERVER";
    public static final String PROPERTY_USE_PROXY = "USE_PROXY";
    public static final String PROPERTY_PROXY = "PROXY";
    public static final String PROPERTY_PROXY_PORT = "PROXY_PORT";
    public static final String PROPERTY_FIXED_DNS = "FIXED_DNS";

    public static final String PARSE_TIMESTAMP = "MM/dd/yyyy KK:mm:ss aa";
    public static final Integer OFFLINE_THRESHOLD = 6;

    /*
     * public class DEVICES {
     * static final short MA10000 = 0x00, // Gateway
     * // TempSensor
     * MA10120 = 0x01, // Pro Temperature sensor with ext. cable probe -29,9…+59,9 °C, ±1°C 7 min
     * MA10100 = 0x02, // Temperature sensor -29.9°C…+59.9°C, ±1°C 7 min
     * MA10101 = 0x02, // Temperature sensor with cable probe –29.9°C…+59.9°C 7 min
     *
     * // TempHygro
     * MA10200 = 0x03, // Thermo-hygro-sensor –39.9°C…+59.9°C, 20%…99%, ±4% 7 min
     * MA10250 = 0x03, // Thermo-Hygro Outdoor –39.9°C…+59.9°C, ±1°C, 20%…99% ±5% 7 min
     * MA10300 = 0x03, // Thermo-hygro-sensor with cable probe –39.9°C…+59.9°C, 20%…99%, ±4% 7 min
     *
     * // Hygro
     * MA10350 = 0x04, // Thermo-hygro-sensor with water detector –39.9°C…+59.9°C, 20%…99%, ±4%
     * /7min
     *
     * // AirQ
     * WL2000 = 0x05, // Air quality monitor indoor: -9.5°C…+59.9°C ±1°C, 20%…95% ±4%, outdoor -39.9°C…+59.9°C
     * // ±1°C, outdoor 1%…99%, CO²-equivalent: 450ppm…3950ppm ±50ppm 7 min
     * // TempHygroPool
     * MA10700 = 0x06, // Thermo-hygro-sensor with pool sensor –39.9°C…+59.9°C ±1°C, 20%…99%, ±4%
     * // Pool:0°C…+59.9°C ±1°C
     * // Weather
     * MA10410 = 0x07, // Weather Station indoor: -9.9°C…+59.9°C ±1°C, 20%…95% ±4%, outdoor -39.9°C…+59.9°C
     * // ±1°C, outdoor 1%…99% ±4% 6 min
     * // Rain
     * MA10650 = 0x08, // Rain meter 0mm/h…300mm/h, 0.258mm during rain up to every second, or every 2 hours
     * // during idle
     *
     * // TempHygroPro
     * MA10320 = 0x09, // Pro Thermo-hygro-sensor with ext. cable probe –39.9°C…+59.9°C, ±1°C, cable probe
     * // –50°C…+110°C, ±0,5°C, 20%…99% ±5% 3.5 min
     * // Acustic
     * MA10860 = 0x0a, // Sensor for acoustical observation of detectors ? ?
     *
     * // Wind
     * MA10660 = 0x0B, // Wind speed and wind direction display wind and gusts: 0-50 m/s, ±5% or ±0.5 m/s,
     * // 0,02s gusts, direction 22,5° resolution 6 min
     *
     * TFA30331202 = 0x0E, // Thermo-hygro-sensor –40.0°C…+60.0°C, 0.0%…99.0% ?
     *
     * TEMPEXT = 0x0F, // Temperature sensor with ext. cable probe
     * MA10450 = 0x0F, // Weather Station indoor: -9.9°C…+59.9°C ±1°C, outdoor -39.9°C…+59.9°C ±1°C 7min
     *
     * // Contact
     * MA10800 = 0x10, // Contact sensor state change, or every 6 hours during idle
     *
     * //
     * TFA30306001 = 0x11, // 4 Thermo-hygro-sensors indoor: -10°C…+60°C ±1°C, outdoor -40°C…+60°C ±1°C, 1%…99%
     * // ±3%
     *
     * MA10230 = 0x12 // Indoor Climate Status –39.9°C…+59.9°C, ±0.8°C, 1%…99%, ±3%
     * ;
     * }
     */
}
