/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mobilealerts.internal.protocol;

import static org.openhab.binding.mobilealerts.internal.MobileAlertsBindingConstants.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.openhab.binding.mobilealerts.internal.handler.MobileAlertsCloudBridgeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Stefan Mueller - Initial contribution
 *
 */
public class MobileAlertsUDPCommunication {
    private DatagramSocket socket;
    private static final String BROADCASTID = "000000000000";
    private static final Integer TIMEOUT = 10000;
    private Map<String, MobileAlertsDeviceData> devices = new HashMap<String, MobileAlertsDeviceData>();
    private final Logger logger = LoggerFactory.getLogger(MobileAlertsCloudBridgeHandler.class);

    static class Command {
        public static final short FIND_GATEWAYS = 1, FIND_GATEWAY = 2, GET_CONFIG = 3, SET_CONFIG = 4, REBOOT = 5;
    }

    private byte[] getMessage(short cmd, String gatewayid, byte[] message) throws DecoderException {
        short length = (short) (message.length + 10);
        ByteBuffer buffer = ByteBuffer.allocate(length);
        buffer.putShort(cmd);
        buffer.put(Hex.decodeHex(gatewayid.toCharArray()));
        buffer.putShort(length);
        if (length > 10) {
            buffer.put(message);
        }
        return buffer.array();
    }

    private void parseMessage(DatagramPacket dp) {
        ByteBuffer buffer = ByteBuffer.wrap(dp.getData());
        short cmd = buffer.getShort();
        byte[] idbuffer = new byte[6];
        buffer.get(idbuffer);
        String gatewayid = Hex.encodeHexString(idbuffer);
        short length = buffer.getShort();

        switch (cmd) {
            case Command.FIND_GATEWAYS:
                // we ignore our request
                break;
            case Command.GET_CONFIG:
                MobileAlertsDeviceData data = new MobileAlertsDeviceData();
                data.Id = gatewayid;
                buffer.get();

                data.Properties.put(CONFIG_DHCP_IP, getAddress(buffer));
                data.Properties.put(CONFIG_USE_DHCP, String.valueOf(buffer.get()));
                data.Properties.put(CONFIG_FIXED_IP, getAddress(buffer));
                data.Properties.put(CONFIG_DHCP_NETMASK, getAddress(buffer));
                data.Properties.put(CONFIG_FIXED_GATEWAY, getAddress(buffer));
                byte[] name = new byte[21];
                buffer.get(name);
                data.Name = new String(name);
                byte[] serverBuffer = new byte[65];
                buffer.get(serverBuffer);
                data.Properties.put(CONFIG_SERVER, new String(serverBuffer));
                data.Properties.put(CONFIG_USE_PROXY, String.valueOf(buffer.get()));
                buffer.get(serverBuffer);
                data.Properties.put(CONFIG_PROXY, new String(serverBuffer));
                data.Properties.put(CONFIG_PROXY_PORT, String.valueOf(buffer.getShort()));
                data.Properties.put(CONFIG_FIXED_DNS, getAddress(buffer));

                devices.put(data.Id, data);
                break;
            default:
                // no other commands to receive right now
                break;
        }
    }

    private String getAddress(ByteBuffer buffer) {
        byte[] addressBuffer = new byte[4];
        buffer.get(addressBuffer);
        try {
            return InetAddress.getByAddress(addressBuffer).getHostAddress();
        } catch (UnknownHostException e) {
            return "";
        }
    }

    public MobileAlertsUDPCommunication() throws SocketException {
        socket = new DatagramSocket(CONFIG_UDPPORT);
    }

    public Map<String, MobileAlertsDeviceData> getDevices() {
        return devices;
    }

    public Boolean scanForGateways() {
        devices.clear();
        try {
            socket.setBroadcast(true);
            byte[] message = getMessage(Command.FIND_GATEWAYS, BROADCASTID, new byte[0]);
            DatagramPacket packet = new DatagramPacket(message, message.length,
                    InetAddress.getByName("255.255.255.255"), CONFIG_UDPPORT);
            socket.send(packet);
            socket.setBroadcast(false);
        } catch (Exception ex) {
            return false;
        }

        byte[] in = new byte[200];
        DatagramPacket incomming = new DatagramPacket(in, in.length);
        try {
            socket.setSoTimeout(TIMEOUT);
        } catch (SocketException e1) {
            // TODO Auto-generated catch block
            logger.debug("MobileAlerts: error occured in UDP communication : " + e1.getMessage());
            return false;
        }

        while (true) {
            try {
                socket.receive(incomming);
                parseMessage(incomming);
            } catch (SocketTimeoutException te) {
                break;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                logger.debug("MobileAlerts: error occured in UDP communication : " + e.getMessage());
                return false;
            }
        }
        return devices.size() != 0;
    }
}
