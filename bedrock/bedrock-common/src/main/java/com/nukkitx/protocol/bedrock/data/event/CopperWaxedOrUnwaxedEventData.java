package com.nukkitx.protocol.bedrock.data.event;

import lombok.Data;

@Data
public class CopperWaxedOrUnwaxedEventData implements EventData {
    private final BlockDefinition definition;

    @Override
    public EventDataType getType() {
        return EventDataType.COPPER_WAXED_OR_UNWAXED;
    }
}
