package nl.bioinf.data_management;

import java.nio.file.Path;

public record ConfigGroup(String groupName, Path groupPath, SampleGroup sampleGroup) {
}
