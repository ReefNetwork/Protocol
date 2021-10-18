package com.nukkitx.protocol.bedrock.data.event;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SneakCloseToSkulkSensorEventData implements EventData {
    public static final SneakCloseToSkulkSensorEventData INSTANCE = new SneakCloseToSkulkSensorEventData();

    @Override
    public EventDataType getType() {
        return EventDataType.SNEAK_CLOSE_TO_SCULK_SENSOR;
    }
}