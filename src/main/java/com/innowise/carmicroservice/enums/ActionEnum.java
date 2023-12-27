package com.innowise.carmicroservice.enums;

public enum ActionEnum {
    GET_CARS("The client received a list of cars"),
    GET_CAR("The client received the car with id = "),
    GET_PAYMENTS("The client received a list of payments"),
    GET_PAYMENT("The client received the payment with id = "),
    GET_PRICES("The client received a list of prices"),
    GET_PRICE("The client received the price with id = "),
    GET_RENTS("The client received a list of rents"),
    GET_RENT("The client received the rent with id = "),
    GET_RESERVATIONS("The client received a list of reservations"),
    GET_RESERVATION("The client received the reservation with id = "),
    CREATE_CAR("The client added a new car with id = "),
    CREATE_PAYMENT("The client added a new payment with id = "),
    CREATE_PRICE("The client added a new price with id = "),
    CREATE_RENT("The client added a new rent with id = "),
    CREATE_RESERVATION("The client added a new reservation with id = "),
    UPDATE_CAR("The client has updated the car data with id = "),
    UPDATE_PAYMENT("The client has updated the payment data with id = "),
    UPDATE_PRICE("The client has updated the price data with id = "),
    UPDATE_RENT("The client has updated the rent data with id = "),
    UPDATE_RESERVATION("The client has updated the reservation data with id = "),
    DELETE_CAR("The client deleted the car with id = "),
    DELETE_PAYMENT("The client deleted the payment with id = "),
    DELETE_PRICE("The client deleted the price with id = "),
    DELETE_RENT("The client deleted the rent with id = "),
    DELETE_RESERVATION("The client deleted the reservation with id = ");

    private String action;

    ActionEnum(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

}

