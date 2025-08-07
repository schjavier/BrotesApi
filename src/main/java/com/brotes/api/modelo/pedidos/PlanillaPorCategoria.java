package com.brotes.api.modelo.pedidos;

import java.util.List;

public record PlanillaPorCategoria(String categoria, List<ItemPlanillaProduccion> items) {
}
