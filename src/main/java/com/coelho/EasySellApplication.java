package com.coelho;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class EasySellApplication {

    public static void main(String ... args) {
        Quarkus.run(args);
    }
}
