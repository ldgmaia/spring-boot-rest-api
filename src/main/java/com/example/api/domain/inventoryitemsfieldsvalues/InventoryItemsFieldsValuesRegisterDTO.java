package com.example.api.domain.inventoryitemsfieldsvalues;

import com.example.api.domain.fieldsvalues.FieldValue;
import com.example.api.domain.inventoryitems.InventoryItem;

public record InventoryItemsFieldsValuesRegisterDTO(
        FieldValue fieldValue,
        InventoryItem inventoryItem
) {

}
