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

@DynamoDBTable(tableName = "projectb-mobilehub-817083040-users")

public class UsersDO {
    private String _email;
    private String _address;
    private String _name;
    private Double _phone;
    private String _suitcaseID;

    @DynamoDBHashKey(attributeName = "email")
    @DynamoDBAttribute(attributeName = "email")
    public String getEmail() {
        return _email;
    }

    public void setEmail(final String _email) {
        this._email = _email;
    }
    @DynamoDBAttribute(attributeName = "address")
    public String getAddress() {
        return _address;
    }

    public void setAddress(final String _address) {
        this._address = _address;
    }
    @DynamoDBAttribute(attributeName = "name")
    public String getName() {
        return _name;
    }

    public void setName(final String _name) {
        this._name = _name;
    }
    @DynamoDBAttribute(attributeName = "phone")
    public Double getPhone() {
        return _phone;
    }

    public void setPhone(final Double _phone) {
        this._phone = _phone;
    }
    @DynamoDBAttribute(attributeName = "suitcaseID")
    public String getSuitcaseID() {
        return _suitcaseID;
    }

    public void setSuitcaseID(final String _suitcaseID) {
        this._suitcaseID = _suitcaseID;
    }

}
