#include <SessionModificationCmd.h>
#include <iostream>
#include "ApprovalTests.hpp"
#include "catch2/catch.hpp"
#include "HexStringEncoder.h"

using namespace std;


TEST_CASE ("SessionModificationCmd") {
    SessionModificationCmd command(1, 1);
    ByteBuffer data;
    command.setXyzTimer(MultiplesOfHours, 23);
    command.setPqvl(1);
    command.encode(data);
    std::string hexStr;
    HexStringEncoder hex;
    hexStr = hex.encode(data);
    cout << "Hex: " << hexStr << endl;
    REQUIRE(hexStr == "03010101083791");

    command.setXyzTimer(MultiplesOfMinutes, 32); // outside range(31), expect 31
    command.encode(data);
    hexStr = hex.encode(data);
    cout << "Hex: " << hexStr << endl;
    REQUIRE(hexStr == "03010101085f91");

    command.setXyzTimer(TimerDeactivated, 2); // deactivated, expect value 0
    command.encode(data);
    hexStr = hex.encode(data);
    cout << "Hex: " << hexStr << endl;
    REQUIRE(hexStr == "03010101080091");

}


