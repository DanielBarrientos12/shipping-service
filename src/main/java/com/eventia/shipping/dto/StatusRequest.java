package com.eventia.shipping.dto;

public record StatusRequest(String status, String trackingNumber, String carrier, String correlationId) {
}