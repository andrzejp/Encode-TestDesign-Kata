package org.codingdojo.kata.encode;

import org.junit.jupiter.api.Test;

import java.util.HexFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class EncodeTest {

    @Test
    void messageTest() {
        var command = new SessionModificationCmd(1, 1);
        command.setXyzTimer(XyzTimerUnit.MULTIPLES_OF_HOURS, 23);
        command.setPqvl(1);

        var data = encodeDataWith(command);

        var hex = new HexStringEncoder();
        String hexStr = hex.encode(data);
        System.out.println("Hex: " + hexStr + "\n");
        assertEquals("03010101083791", hexStr);
    }

    @Test
    void multiplesOfMinutes() {
        var command = getCommandWithTimer(XyzTimerUnit.MULTIPLES_OF_MINUTES, 32);

        var data = encodeDataWith(command);

        String hexStr = new HexStringEncoder().encode(data);
        assertEquals("03010101085f91", hexStr);
    }

    @Test
    void timerDeactivated() {
        var command = getCommandWithTimer(XyzTimerUnit.TIMER_DEACTIVATED, 2);

        var data = encodeDataWith(command);

        String hexStr = new HexStringEncoder().encode(data);
        assertEquals("03010101080091", hexStr);
    }

    private static ByteBuffer encodeDataWith(SessionModificationCmd command) {
        var data = new ByteBuffer();
        command.encode(data);
        return data;
    }

    private SessionModificationCmd getCommandWithTimer(XyzTimerUnit timerUnit, int timerValue) {
        var command1 = new SessionModificationCmd(1, 1);
        command1.setPqvl(1);
        var command = command1;
        command.setXyzTimer(timerUnit, timerValue); // outside range(31), expect 31
        return command;
    }

    private static class HexStringEncoder {

        public String encode(ByteBuffer buffer) {
            var hex = new StringBuilder();

            HexFormat format = HexFormat.of();
            while (buffer.getAvailable() > 0) {
                int octet = buffer.read();

                hex.append(format.toHighHexDigit(octet));
                hex.append(format.toLowHexDigit(octet));
            }

            return hex.toString();
        }
    }
}
