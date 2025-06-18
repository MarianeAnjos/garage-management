package org.example.garagemanagementapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Resposta com o total de receita do dia")
public class RevenueResponse {

    @Schema(description = "Valor total arrecadado no dia", example = "75.00")
    private double amount;

    @Schema(description = "Moeda da receita", example = "BRL")
    private String currency;

    @Schema(description = "Horário da geração do relatório")
    private LocalDateTime timestamp;

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
