package org.example.message;

import io.netty.util.AttributeKey;

public class ResponseAttrKey {

    public static final AttributeKey<Message> INSTANCE = AttributeKey.newInstance("response");

}
