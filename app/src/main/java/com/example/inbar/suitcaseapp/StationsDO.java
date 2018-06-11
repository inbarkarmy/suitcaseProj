package com.example.inbar.suitcaseapp;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "projectb-mobilehub-817083040-stations")

public class StationsDO {
    private String _stationID;
    private String _city;
    private String _country;

    @DynamoDBHashKey(attributeName = "stationID")
    @DynamoDBAttribute(attributeName = "stationID")
    public String getStationID() {
        return _stationID;
    }

    public void setStationID(final String _stationID) {
        this._stationID = _stationID;
    }
    @DynamoDBAttribute(attributeName = "city")
    public String getCity() {
        return _city;
    }

    public void setCity(final String _city) {
        this._city = _city;
    }
    @DynamoDBAttribute(attributeName = "country")
    public String getCountry() {
        return _country;
    }

    public void setCountry(final String _country) {
        this._country = _country;
    }

}
