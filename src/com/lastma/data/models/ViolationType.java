package com.lastma.data.models;

public enum ViolationType {

    TRAFFIC_CONTROL_VIOLATION(
            "Traffic Control Violation",
            "Disobeying traffic control personnel",
            2, 20_000, 30_000,
            "Vehicle impoundment & LASDRI training"),

    ILLEGAL_PARKING(
            "Illegal Parking",
            "Parking on a yellow line on a public highway / illegal parking",
            4, 20_000, 30_000,
            "Vehicle impoundment & LASDRI training"),

    LINE_VIOLATION(
            "Line Violation",
            "Vehicle crossing double yellow line / centre line",
            4, 20_000, 30_000,
            "Vehicle impoundment & LASDRI training"),

    OFFSIDE_RULE_VIOLATION(
            "Offside Rule Violation",
            "Staying within the yellow junction box (offside rule)",
            3, 20_000, 30_000,
            "Vehicle impoundment & LASDRI training"),

    ZEBRA_CROSSING_VIOLATION(
            "Zebra Crossing Violation",
            "Failure to yield to pedestrians at a zebra crossing",
            3, 20_000, 30_000,
            "Vehicle impoundment & LASDRI training"),

    ROUNDABOUT_VIOLATION(
            "Roundabout Violation",
            "Failure to give way to traffic on the left at a roundabout",
            2, 20_000, 30_000,
            "Vehicle impoundment & LASDRI training"),

    MIRROR_LIGHT_VIOLATION(
            "Mirror/Light Violation",
            "Driving with missing side mirrors, indicators, brake lights or rear lights",
            2, 20_000, 30_000,
            "Fix/install before release + LASDRI counselling"),

    TAILGATING_EMERGENCY_VEHICLE(
            "Tailgating Emergency Vehicle",
            "Tailgating an emergency vehicle",
            3, 20_000, 30_000,
            "3 years imprisonment or fine + prison"),

    ILLEGAL_U_TURN(
            "Illegal U-turn",
            "Illegal U-turn",
            3, 20_000, 30_000,
            "Driver training"),

    DRIVING_ON_WALKWAY_KERBS(
            "Driving on Walkway/Kerbs",
            "Driving on walkway or kerbs",
            4, 50_000, 0,
            "Vehicle impoundment + 3 months imprisonment"),

    PARKING_ON_WALKWAY_KERBS(
            "Parking on Walkway/Kerbs",
            "Parking on walkway or kerbs",
            3, 20_000, 0,
            "Vehicle impoundment + 3 months imprisonment"),

    ABANDONED_VEHICLE_ON_HIGHWAY(
            "Abandoned Vehicle on Highway",
            "Abandoned vehicle on highway",
            2, 50_000, 0,
            "Towing fee applies + 3 months imprisonment"),

    BREAKDOWN_HIGHWAY_OBSTRUCTION(
            "Highway Obstruction",
            "Causing obstruction on highway if broken down",
            3, 25_000, 0,
            "Towing fee applies"),

    USE_OF_PHONE_CALL(
            "Use of Phone (Call)",
            "Making/receiving calls while driving without hands-free",
            2, 20_000, 30_000,
            "3 months community service"),

    USE_OF_PHONE_TEXT(
            "Use of Phone (Text)",
            "Texting/reading a text while driving",
            2, 20_000, 30_000,
            "6 months community service"),

    SEATBELT_VIOLATION(
            "Seatbelt Violation",
            "Driving without a strapped seat belt for driver/passengers",
            2, 20_000, 30_000,
            "Vehicle impoundment / strap on belt"),

    REFLECTIVE_WARNING_VIOLATION(
            "Reflective Warning Violation",
            "Failure to display reflective warning sign at a breakdown",
            3, 20_000, 30_000,
            "Obtain sign before release"),

    RESISTING_ARREST(
            "Resisting Arrest",
            "Resisting arrest",
            2, 20_000, 30_000,
            "Vehicle impoundment & LASDRI training"),

    BRT_LANE_VIOLATION(
            "BRT Lane Violation",
            "Use of BRT Lite corridor",
            3, 20_000, 30_000,
            "6 months prison or 1 year community service + LASDRI"),

    WILLFUL_HIGHWAY_OBSTRUCTION(
            "Highway Obstruction",
            "Willful obstruction on highway",
            2, 50_000, 0,
            "Towing fee applies + 3 months prison, or both fine and prison"),

    ROUTE_VIOLATION(
            "Route Violation",
            "Operating on restricted routes or beyond approved hours",
            3, 50_000, 0,
            "Or vehicle impoundment; optional 6 months prison / 1 year community service"),

    DRIVING_UNDER_INFLUENCE(
            "Driving Under Influence",
            "Driving under the influence of alcohol/drugs",
            3, 100_000, 0,
            "1 year's imprisonment"),

    TRAFFIC_LIGHT_VIOLATION(
            "Traffic Light Violation",
            "Disobeying a traffic light",
            2, 10_000, 0,
            "Optional 3 months prison / 6 months community service"),

    VIOLATION_FINES_PAYMENT_DEFAULT(
            "Violation Fines Payment Default",
            "Failure to pay a penalty within the prescribed time",
            0, 0, 0,
            "Double the initial penalty (per FAQ)");

    private final String offenceName;
    private final String description;
    private final int points;
    private final int firstOffenceFine;
    private final int subsequentOffenceFine;
    private final String additionalPenalty;

    ViolationType(String offenceName,
                  String description,
                  Integer points,
                  Integer firstOffenceFine,
                  Integer subsequentOffenceFine,
                  String additionalPenalty) {
        this.offenceName = offenceName;
        this.description = description;
        this.points = points;
        this.firstOffenceFine = firstOffenceFine;
        this.subsequentOffenceFine = subsequentOffenceFine;
        this.additionalPenalty = additionalPenalty;
    }

    public String getOffenceName() {
        return offenceName;
    }

    public String getDescription() {
        return description;
    }

    public int getPoints() {
        return points;
    }

    public int getFirstOffenceFine() {
        return firstOffenceFine;
    }

    public int getSubsequentOffenceFine() {
        return subsequentOffenceFine;
    }

    public String getAdditionalPenalty() {
        return additionalPenalty;
    }

    public Integer getFine(boolean subsequentOffender) {
        if (subsequentOffender) {
            return subsequentOffenceFine;
        }
        return firstOffenceFine;
    }

    @Override
    public String toString() {
        return offenceName;
    }
}