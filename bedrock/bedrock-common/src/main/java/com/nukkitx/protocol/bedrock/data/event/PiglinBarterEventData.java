package com.nukkitx.protocol.bedrock.data.event;

import lombok.Value;

@Value
public class PiglinBarterEventData implements EventData {
    private final ItemDefinition definition;
    private final boolean targetingPlayer;

    @Override
    public EventDataType getType() {
        return EventDataType.PIGLIN_BARTER;
    }
}
