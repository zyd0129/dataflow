package com.zyd.learn.stream.simplesink.bindings.intf;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface Transform {
    @Input
    SubscribableChannel transformInput();

    @Output
    MessageChannel transformOutput();
}
