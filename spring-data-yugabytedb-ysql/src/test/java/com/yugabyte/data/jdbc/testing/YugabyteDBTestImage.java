package com.yugabyte.data.jdbc.testing;

import org.testcontainers.utility.DockerImageName;

public interface YugabyteDBTestImage {
    DockerImageName YUGABYTEDB_IMAGE = DockerImageName.parse("yugabytedb/yugabyte:2.7.0.0-b17");
}
