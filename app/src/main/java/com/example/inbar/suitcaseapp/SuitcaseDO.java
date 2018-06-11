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

@DynamoDBTable(tableName = "projectb-mobilehub-817083040-suitcase")

public class SuitcaseDO {
    private String _suitcaseID;
    private Map<String, String> _locationDateMap;
    private String _ownerEmail;

    @DynamoDBHashKey(attributeName = "suitcaseID")
    @DynamoDBAttribute(attributeName = "suitcaseID")
    public String getSuitcaseID() {
        return _suitcaseID;
    }

    public void setSuitcaseID(final String _suitcaseID) {
        this._suitcaseID = _suitcaseID;
    }
    @DynamoDBAttribute(attributeName = "locationDateMap")
    public Map<String, String> getLocationDateMap() {
        return _locationDateMap;
    }

    public void setLocationDateMap(final Map<String, String> _locationDateMap) {
        this._locationDateMap = _locationDateMap;
    }
    @DynamoDBAttribute(attributeName = "ownerEmail")
    public String getOwnerEmail() {
        return _ownerEmail;
    }

    public void setOwnerEmail(final String _ownerEmail) {
        this._ownerEmail = _ownerEmail;
    }

}
