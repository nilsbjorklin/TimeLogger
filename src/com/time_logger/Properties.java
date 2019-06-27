package com.time_logger;

public class Properties {

    public enum Encoding {
        TEXT("log"),
        OBJECT("obj");

        private String fileEnd;

        Encoding(String fileEnding) {
            this.fileEnd = fileEnding;
        }

        public String fileEnding() {
            return fileEnd;
        }
    }
}
