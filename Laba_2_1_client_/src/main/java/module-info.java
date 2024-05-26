module com.example.laba_2_1_client_ {
    requires javafx.controls;
    requires javafx.fxml;
    requires io.netty.transport;
    requires io.netty.buffer;
    requires io.netty.common;
    requires io.netty.handler;
    requires io.netty.codec;

    opens com.example.laba_2_1_client_ to javafx.fxml;
    exports com.example.laba_2_1_client_;
}