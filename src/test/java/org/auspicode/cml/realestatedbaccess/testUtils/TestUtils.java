package org.auspicode.cml.realestatedbaccess.testUtils;

import jakarta.validation.constraints.NotNull;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class TestUtils {

    public static String readJsonStringFromResourceFile(@NotNull String filePath) throws URISyntaxException, IOException {
        return Files.readString(Paths.get(Objects.requireNonNull(TestUtils.class.getResource(filePath)).toURI()), StandardCharsets.UTF_8);
    }
}
