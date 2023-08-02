package algonquin.cst2335.finalproject;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Represents a flight result entity containing information about a specific flight.
 *
 * @author Wan-Hsuan Lee
 * @version 1.0
 */
@Entity
public class FlightResult {

    /** The auto-generated primary key ID for the flight result. */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="ID")
    public long id;
    /** The flight date of the flight result. */
    @ColumnInfo(name="Flight Date")
    protected String flightDate;
    /** The status of the flight result. */
    @ColumnInfo(name="Flight Status")
    protected String status;
    /** The airline of the flight result. */
    @ColumnInfo(name="Airline")
    protected String airline;
    /** The flight number (IATA code) of the flight result. */
    @ColumnInfo(name="Flight Number_iata")
    protected String flightNumber;

    // Departure information:
    @ColumnInfo(name="Departure Airport_iata")
    protected String departureAirport;
    @ColumnInfo(name="Departure Airport")
    protected String departureAirportName;
    @ColumnInfo(name="Departure Timezone")
    protected String departureTimezone;
    @ColumnInfo(name="Departure Terminal")
    protected String departureTerminal;
    @ColumnInfo(name="Departure Gate")
    protected String departureGate;
    @ColumnInfo(name="Departure Scheduled")
    protected String departureTime;
    @ColumnInfo(name="Departure Estimated")
    protected String departureEstimated;
    @ColumnInfo(name="Departure Delay")
    protected int departureDelay;

    // Arrival information:
    @ColumnInfo(name="Arrival Airport_iata")
    protected String arrivalAirport;
    @ColumnInfo(name="Arrival Airport")
    protected String arrivalAirportName;
    @ColumnInfo(name="Arrival Timezone")
    protected String arrivalTimezone;
    @ColumnInfo(name="Arrival Terminal")
    protected String arrivalTerminal;
    @ColumnInfo(name="Arrival Gate")
    protected String arrivalGate;
    @ColumnInfo(name="Arrival Scheduled")
    protected String arrivalTime;
    @ColumnInfo(name="Arrival Estimated")
    protected String arrivalEstimated;
    @ColumnInfo(name="Arrival Delay")
    protected int arrivalDelay;

    /** Default constructor for creating a FlightResult instance. */
    public FlightResult() {}

    /** Getters and setters for the FlightResult fields. */
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(String flightDate) {
        this.flightDate = flightDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public String getDepartureAirportName() {
        return departureAirportName;
    }

    public void setDepartureAirportName(String departureAirportName) {
        this.departureAirportName = departureAirportName;
    }

    public String getDepartureTimezone() {
        return departureTimezone;
    }

    public void setDepartureTimezone(String departureTimezone) {
        this.departureTimezone = departureTimezone;
    }

    public String getDepartureTerminal() {
        return departureTerminal;
    }

    public void setDepartureTerminal(String departureTerminal) {
        this.departureTerminal = departureTerminal;
    }

    public String getDepartureGate() {
        return departureGate;
    }

    public void setDepartureGate(String departureGate) {
        this.departureGate = departureGate;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getDepartureEstimated() {
        return departureEstimated;
    }

    public void setDepartureEstimated(String departureEstimated) {
        this.departureEstimated = departureEstimated;
    }

    public int getDepartureDelay() {
        return departureDelay;
    }

    public void setDepartureDelay(int departureDelay) {
        this.departureDelay = departureDelay;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public String getArrivalAirportName() {
        return arrivalAirportName;
    }

    public void setArrivalAirportName(String arrivalAirportName) {
        this.arrivalAirportName = arrivalAirportName;
    }

    public String getArrivalTimezone() {
        return arrivalTimezone;
    }

    public void setArrivalTimezone(String arrivalTimezone) {
        this.arrivalTimezone = arrivalTimezone;
    }

    public String getArrivalTerminal() {
        return arrivalTerminal;
    }

    public void setArrivalTerminal(String arrivalTerminal) {
        this.arrivalTerminal = arrivalTerminal;
    }

    public String getArrivalGate() {
        return arrivalGate;
    }

    public void setArrivalGate(String arrivalGate) {
        this.arrivalGate = arrivalGate;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getArrivalEstimated() {
        return arrivalEstimated;
    }

    public void setArrivalEstimated(String arrivalEstimated) {
        this.arrivalEstimated = arrivalEstimated;
    }

    public int getArrivalDelay() {
        return arrivalDelay;
    }

    public void setArrivalDelay(int arrivalDelay) {
        this.arrivalDelay = arrivalDelay;
    }
}
